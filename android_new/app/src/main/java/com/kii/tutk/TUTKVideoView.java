package com.kii.tutk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.AVFrame;
import com.tutk.IOTC.IOTCAPIs;
import com.yankon.smart.model.Event;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import de.greenrobot.event.EventBus;


/**
 * Created by Evan on 15/11/3.
 */
public class TUTKVideoView extends GLSurfaceView {

    private static final String LOG_TAG = TUTKVideoView.class.getSimpleName();
    public static final int ERROE = 0;
    public static final int DISCONNECT = 1;
    public static final int DISPLAY = 2;

    private String mCameraUID = null;
    private boolean isRunning = false;

    Thread videoThread = null;
    Thread audioThread = null;
    Thread IOTCThread = null;

    int lastWidth, lastHeight;
    private static boolean isChangeOrientation = false;
    private ListenIOTCTime listenIOTCTime;

    public void setListenIOTCTime(ListenIOTCTime listenIOTCTime) {
        this.listenIOTCTime = listenIOTCTime;
    }


    public TUTKVideoView(Context context) {
        super(context);
    }

    public TUTKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(String UID) {
        mCameraUID = UID;
        mHandler = new ResizeHandler(this);
        VideoRendererGui.setView(this, null);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isChangeOrientation = true;
        }else{
            isChangeOrientation = false;
        }
        updateSize();
    }

    public void onDestory() {
        VideoRendererGui.dispose();
        stop();
    }

    public void stop() {
        isRunning = false;

        if (videoThread != null) {
            videoThread.interrupt();
        }
        if (audioThread != null) {
            audioThread.interrupt();
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

        System.out.println("StreamClient start...");
        // use which Master base on location, port 0 means to get a random port
        int ret = IOTCAPIs.IOTC_Initialize(0, "m1.iotcplatform.com",
                "m2.iotcplatform.com", "m4.iotcplatform.com",
                "m5.iotcplatform.com");
        System.out.printf("IOTC_Initialize() ret = %d\n", ret);
        if (ret == IOTCAPIs.IOTC_ER_ALREADY_INITIALIZED) {
            IOTCAPIs.IOTC_DeInitialize();
            ret = IOTCAPIs.IOTC_Initialize(0, "m1.iotcplatform.com",
                    "m2.iotcplatform.com", "m4.iotcplatform.com",
                    "m5.iotcplatform.com");
            System.out.printf("After DeInitialize, IOTC_Initialize() ret = %d\n", ret);
        }
        if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
            Log.e(LOG_TAG, "IOTCAPIs_Device exit...!!");
//            startInternal();
            EventBus.getDefault().post(new Event.VideoEvent(ERROE));
            return;
        }

        // alloc 3 sessions for video and two-way audio
        AVAPIs.avInitialize(3);

        long iotcStartTime = System.currentTimeMillis();
        if (listenIOTCTime != null)
            listenIOTCTime.onListenIOTCTime(iotcStartTime);
        int sid = IOTCAPIs.IOTC_Connect_ByUID(mCameraUID);
        System.out.printf("Step 2: call IOTC_Connect_ByUID(%s).......\n", mCameraUID);

        int[] srvType = new int[1];
        int[] resend = new int[1];
        int avIndex = AVAPIs.avClientStart2(sid, "admin", "123456", 20000, srvType, 0, resend);
        System.out.printf("Step 2: call avClientStart(%d).......\n", avIndex);

        if (avIndex < 0) {
            System.out.printf("avClientStart failed[%d]\n", avIndex);
            EventBus.getDefault().post(new Event.VideoEvent(ERROE));
            return;
        }

        if (startIpcamStream(avIndex)) {
            isRunning = true;
            videoThread = new Thread(new VideoThread(avIndex),
                    "Video Thread");
            audioThread = new Thread(new AudioThread(avIndex),
                    "Audio Thread");
            videoThread.start();
            audioThread.start();
            try {
                videoThread.join();
            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
                return;
            }
            try {
                audioThread.join();
            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
                return;
            }
        }

        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
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

    static class ResizeHandler extends Handler {
        public static final int RESIZE = 0;
        WeakReference<SurfaceView> viewRef = null;

        int rep = 0;
        int scale = 0;
        private int mWidth = 0;
        private int mHeight = 0;
        float mMediaWidth = 0;
        float mMediaHeight = 0;
        private DisplayMetrics dm = new DisplayMetrics();
        int lastScale = 0;

        public ResizeHandler(SurfaceView view) {
            this.viewRef = new WeakReference<SurfaceView>(view);
        }

        public void handleMessage(Message msg) {
            if (viewRef == null)
                return;
            SurfaceView view = viewRef.get();
            if (view == null)
                return;
            ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
            mMediaWidth = msg.arg1; //mMediaFormat.getInteger("width");
            mMediaHeight = msg.arg2;//mMediaFormat.getInteger("height");

            if (!isChangeOrientation) {
                scale = (int) (mWidth / mMediaWidth * 100);
                LogUtils.i("TAG", "Portrait: " + "mWidth:" + mWidth + "--" + "mHeight:" + mHeight + "--" +
                        "mMediaWidth:" + mMediaWidth + "--" + "mMediaHeight:" + mMediaHeight + "--" +
                        "rep:" + rep + "--" + "scale:" + scale + "---");
                if (rep != scale) {
                    rep = scale;
                    view.getHolder().setFixedSize(mWidth, (int) (mMediaHeight * scale / 100));
                }
            } else {
                scale = (int) (mHeight / mMediaHeight * 100);
                LogUtils.i("TAG", "Orientation: " + "mWidth:" + mWidth + "--" + "mHeight:" + mHeight + "--" +
                        "mMediaWidth:" + mMediaWidth + "--" + "mMediaHeight:" + mMediaHeight + "--" +
                        "rep:" + rep + "--" + "scale:" + scale + "---");
                if (rep != scale) {
                    rep = scale;
                    view.getHolder().setFixedSize(mWidth, mHeight);
                }
            }
//            int scale = (int) (mWidth / mMediaWidth * 100);
//            if (lastScale != scale) {
//                lastScale = scale;
////                view.getHolder().setFixedSize(mWidth, (int) (mMediaHeight * scale / 100));
//            }

        }
    }

    ResizeHandler mHandler = null;

    void updateSize() {
        Message msg = mHandler.obtainMessage(ResizeHandler.RESIZE, lastWidth, lastHeight);
        mHandler.sendMessage(msg);
    }

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

            long baseTime = 0;
            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] videoBuffer = new byte[VIDEO_BUF_SIZE];
            OpenH264 openH264 = new OpenH264();
            if (openH264.decoderRef == 0)
                return;
            VideoRenderer.Callbacks renderer = VideoRendererGui.create(0,0,100,100, RendererCommon.ScalingType.SCALE_ASPECT_FIT, false);
            boolean stopDecode = false;
            while (isRunning) {
                int[] frameNumber = new int[1];
                int[] outBufSize = new int[1];
                int[] outFrmSize = new int[1];
                int[] outFrmInfoBufSize = new int[1];
                byte[]   pFrmInfoBuf = new byte[VIDEO_BUF_SIZE];
                int ret = av.avRecvFrameData2(avIndex, videoBuffer, VIDEO_BUF_SIZE, outBufSize, outFrmSize, pFrmInfoBuf, VIDEO_BUF_SIZE, outFrmInfoBufSize, frameNumber);
                if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
                    try {
                        Thread.sleep(30);
                        continue;
                    } catch (InterruptedException e) {
                        try {
                            openH264.uninitialize(openH264.decoderRef);
                        } catch (Exception e1) {
                            Log.e(LOG_TAG, Log.getStackTraceString(e));
                        }
                        break;
                    }
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    System.out.printf("[%s] Lost video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    stopDecode = true;
                    continue;
                } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                    System.out.printf("[%s] Incomplete video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    stopDecode = true;
                    continue;
                } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    EventBus.getDefault().post(new Event.VideoEvent(DISCONNECT));
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }

                ////判断是I帧还是P帧如果是I帧才开始解析数据
                if( (pFrmInfoBuf[2] == AVFrame.IPC_FRAME_FLAG_IFRAME)){
                    LogUtils.i(LOG_TAG,"recv I frame"+pFrmInfoBuf[2]);
                    //重新初始化时间
                    stopDecode = false;
                    Global.lastBuffer = new byte[outBufSize[0]];
                    System.arraycopy(videoBuffer, 0, Global.lastBuffer, 0, outBufSize[0]);
                }
//                else if(stopDecode){
//                    LogUtils.i(LOG_TAG,"c P frame"+pFrmInfoBuf[2]);
//                    continue;
//                }else {
//
//                }

                long time = 0;
                for (int i = 0; i < 4; i++) {
                    int shift = (4 - 1 - i) * 8;
                    time += (frameInfo[i + 12] & 0x000000FF) << shift;//往高位游
                }
                if (baseTime == 0) {
                    baseTime = time;
                }
                if (ret > 0) {
                    onFrame(videoBuffer, openH264, ret, time - baseTime, renderer);
                }
            }

            try {
                openH264.uninitialize(openH264.decoderRef);
            } catch (Throwable e) {
                Log.e(LOG_TAG, Log.getStackTraceString(e));
            }
            System.out.printf("[%s] Exit\n",
                    Thread.currentThread().getName());
        }
        int strides[] = new int[3];
        public void onFrame(byte[] buf, OpenH264 openH264, int length, long time, VideoRenderer.Callbacks renderer) {
            int result = openH264.decodeFrame(openH264.decoderRef, time, buf, length);
            Log.d(LOG_TAG, "len:" + length + " result:" + result + " w:" + openH264.iWidth
                    + " h:" + openH264.iHeight);
            if (result != 1)
                return;
            if (openH264.iWidth != lastWidth || openH264.iHeight != lastHeight || strides[0] == 0) {
                lastWidth = openH264.iWidth;
                lastHeight = openH264.iHeight;
                strides[0] = lastWidth;
                strides[1] = strides[0] / 2;
                strides[2] = strides[0] / 2;
                updateSize();
            }
            ByteBuffer bufs[] = new ByteBuffer[3];
            bufs[0] = ByteBuffer.allocateDirect(lastWidth * lastHeight);
            bufs[1] = ByteBuffer.allocateDirect(lastWidth * lastHeight / 4);
            bufs[2] = ByteBuffer.allocateDirect(lastWidth * lastHeight / 4);
            bufs[0].put(openH264.pData0);
            bufs[1].put(openH264.pData1);
            bufs[2].put(openH264.pData2);
            bufs[0].position(0);
            bufs[1].position(0);
            bufs[2].position(0);
            VideoRenderer.I420Frame frame = new VideoRenderer.I420Frame(openH264.iWidth, openH264.iHeight,
                    0, strides, bufs, 0);
            renderer.renderFrame(frame);
            EventBus.getDefault().post(new Event.VideoEvent(DISPLAY));
        }
    }

    public class AudioThread implements Runnable {
        static final int AUDIO_BUF_SIZE = 60 * 1024;
        static final int FRAME_INFO_SIZE = 16;

        private int avIndex;
        AudioTrack mAudioTrack = null;

        public AudioThread(int avIndex) {
            this.avIndex = avIndex;
        }

        @Override
        public void run() {
            System.out.printf("[%s] Start\n",
                    Thread.currentThread().getName());

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] audioBuffer = new byte[AUDIO_BUF_SIZE];

            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    8000,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AUDIO_BUF_SIZE,
                    AudioTrack.MODE_STREAM);
            while (isRunning) {
                int ret = av.avCheckAudioBuf(avIndex);

                if (ret < 0) {
                    // Same error codes as below
                    System.out.printf("[%s] avCheckAudioBuf() failed: %d\n",
                            Thread.currentThread().getName(), ret);
                    break;
                } else if (ret < 3) {
                    try {
                        Thread.sleep(120);
                        continue;
                    } catch (InterruptedException e) {
//                        System.out.println(e.getMessage());
                        break;
                    }
                }

                int[] frameNumber = new int[1];
                ret = av.avRecvAudioData(avIndex, audioBuffer,
                        AUDIO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                        frameNumber);

                if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
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
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    //System.out.printf("[%s] Audio frame losed\n",
                    //        Thread.currentThread().getName());
                    continue;
                }
                // Now the data is ready in audioBuffer[0 ... ret - 1]
                // Do something here
                mAudioTrack.write(audioBuffer, 0, ret);
                mAudioTrack.play();
            }
            mAudioTrack.stop();
            mAudioTrack.release();
            System.out.printf("[%s] Exit\n",
                    Thread.currentThread().getName());
        }
    }

    public interface ListenIOTCTime{
        void onListenIOTCTime(long time);
    }
}
