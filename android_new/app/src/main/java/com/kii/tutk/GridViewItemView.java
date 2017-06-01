package com.kii.tutk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.LogRecord;

/**
 * Created by Yue on 15/11/10.
 */
public class GridViewItemView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String LOG_TAG = TUTKVideoView.class.getSimpleName();

    private String mCameraUID = null;

    private boolean isRunning = false;

    Thread videoThread = null;

    Thread IOTCThread = null;

    int surfaceWidth, surfaceHeight;

    Surface mSurface = null;

    private MediaFormat mMediaFormat;

    private DisplayMetrics dm = new DisplayMetrics();

    private int mWidth = 0;

    private int mHeight = 0;

    float mMediaWidth = 0;

    float mMediaHeight = 0;

    public GridViewItemView(Context context) {
        super(context);
    }

    public GridViewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(String UID) {
        mCameraUID = UID;
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.surfaceWidth = width;
        this.surfaceHeight = height;
        mSurface = holder.getSurface();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurface = null;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void stop() {
        isRunning = false;

        if (videoThread != null) {
            videoThread.interrupt();
        }
        try {
            IOTCThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void start() {
        IOTCThread = new Thread(new Runnable() {
            public void run() {
                startInternal();
            }
        });
        IOTCThread.start();

    }

    private void startInternal() {

        int sid = IOTCAPIs.IOTC_Connect_ByUID(mCameraUID);
        System.out.printf("Step 2: call IOTC_Connect_ByUID(%s).......\n", mCameraUID);

        int[] srvType = new int[1];
        int[] bResend = new int[1];
//        int avIndex = AVAPIs.avClientStart(sid, "admin", "888888", 20000, srvType, 0);
        int avIndex = AVAPIs.avClientStart2(sid, "admin", "888888", 20000, srvType, 0,bResend);
        System.out.printf("Step 2: call avClientStart(%d).......\n", avIndex);

        if (avIndex < 0) {
            System.out.printf("avClientStart failed[%d]\n", avIndex);
            return;
        }

        if (startIpcamStream(avIndex)) {
            isRunning = true;
            videoThread = new Thread(new VideoThread(avIndex),
                    "Video Thread");
            videoThread.start();
            try {
                videoThread.join();
            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
                return;
            }

        }

        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
    }

    public static boolean startIpcamStream(int avIndex) {
        AVAPIs av = new AVAPIs();
        int ret = av.avSendIOCtrl(avIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        // This IOTYPE constant and its corrsponsing data structure is defined in
        // Sample/Linux/Sample_AVAPIs/AVIOCTRLDEFs.h
        //
        int IOTYPE_USER_IPCAM_START = 0x1FF;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        int IOTYPE_USER_IPCAM_AUDIOSTART = 0x300;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        return true;
    }

    int rep = 0;

    int scale = 0;

    private static final int RESIZE = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg){
            mWidth = GridViewActivity.mGridViewWidth;
            mMediaWidth = mMediaFormat.getInteger("width");
            mMediaHeight = mMediaFormat.getInteger("height");
            scale = (int)(mWidth/mMediaWidth * 100);
            if(rep != scale) {
                rep = scale;
                getHolder().setFixedSize(mWidth, (int) (mMediaHeight * scale / 100));
            }
        }
    };

    public class VideoThread implements Runnable {

        static final int VIDEO_BUF_SIZE = 1024 * 1024;

        static final int FRAME_INFO_SIZE = 16;

        static final String MIME_TYPE = "video/avc";

        private int avIndex;

        public VideoThread(int avIndex) {
            this.avIndex = avIndex;
        }

        @Override
        public void run() {
            System.out.printf("[%s] Start\n",
                    Thread.currentThread().getName());

            MediaCodec mDecodeMediaCodec = null;
            if (mSurface == null) {
                Log.e(LOG_TAG, "surface equals to null");
                return;
            }
            try {
                mDecodeMediaCodec = MediaCodec.createDecoderByType(MIME_TYPE);
                MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE,
                        surfaceWidth,
                        surfaceHeight);
                mDecodeMediaCodec.configure(format,
                        mSurface,
                        null,
                        0);
                mDecodeMediaCodec.setVideoScalingMode(
                        MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mDecodeMediaCodec.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] videoBuffer = new byte[VIDEO_BUF_SIZE];

            while (isRunning) {
                int[] frameNumber = new int[1];
                int ret = av.avRecvFrameData(avIndex, videoBuffer,
                        VIDEO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                        frameNumber);
                if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
                    try {
                        Thread.sleep(30);
                        continue;
                    } catch (InterruptedException e) {
                        if (mDecodeMediaCodec != null) {
                            try {
                                mDecodeMediaCodec.stop();
                                mDecodeMediaCodec.release();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }
//                        System.out.println(e.getMessage());
                        break;
                    }
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    System.out.printf("[%s] Lost video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                    System.out.printf("[%s] Incomplete video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }

                if (ret > 0) {
                    onFrame(videoBuffer, mDecodeMediaCodec, ret);
                }
            }

            try {
                if (mDecodeMediaCodec != null) {
                    mDecodeMediaCodec.stop();
                    mDecodeMediaCodec.release();
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, Log.getStackTraceString(e));
            }
            mMediaFormat = null;
            System.out.printf("[%s] Exit\n",
                    Thread.currentThread().getName());
        }

        public void onFrame(byte[] buf, MediaCodec mediaCodec, int length) {
            ByteBuffer[] inputBuffers = null;
            int inputBufferIndex = 0;
            MediaCodec.BufferInfo bufferInfo = null;
            int outputBufferIndex = 0;
            try {
                inputBuffers = mediaCodec.getInputBuffers();
                inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
                if (inputBufferIndex >= 0) {
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    inputBuffer.clear();
                    inputBuffer.put(buf, 0, length);
                    mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, 0, 0);
                }
                bufferInfo = new MediaCodec.BufferInfo();
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            } catch (Exception e) {

            }

            if (outputBufferIndex < 0) {
                switch (outputBufferIndex) {
                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        mMediaFormat = mediaCodec.getOutputFormat();
                        mHandler.sendEmptyMessage(RESIZE);
                        break;
                }
            }
            while (outputBufferIndex >= 0) {
                if (mMediaFormat == null) {
                    mMediaFormat = mediaCodec.getOutputFormat();
                    mHandler.sendEmptyMessage(RESIZE);
                }
                mediaCodec.releaseOutputBuffer(outputBufferIndex, true);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            }
        }
    }
}
