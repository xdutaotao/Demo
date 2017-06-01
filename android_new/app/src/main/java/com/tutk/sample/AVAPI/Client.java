package com.tutk.sample.AVAPI;

import android.util.Log;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;
import com.tutk.LoadListener;
import com.tutk.ffmpeg.FFmpegPlayer;
import com.yankon.smart.utils.LogUtils;

public class Client {
    private final static String TAG = "Client";
    private static Thread videoThread = null;
    private static Thread audioThread = null;
    private static VideoThread runnableVideoThread = null;
    private static AudioThread runnableAudioThread = null;
    private static int testOn = 0;  // Set to 1 to test a mp file
    private volatile static boolean mIsPlaying = false;
    private static int videoThreadFromMpegPlayer = 1;  // set to 0 is no longer supported
    private static int audioThreadFromMpegPlayer = 0;

    private static Thread dummyThread = null;
    private static DummyThread runnableDummyThread = null;

    // Flags added to control the exit better.
    private volatile static boolean mIsStopping = false;
    private volatile static boolean mIsConnecting = false;

    private volatile static boolean mIsRunning = false; // Flag to indicate if client thread is running
    private volatile static boolean mIsStartAllowed = false;   // Flag to indicate if client is allowed to connect to a camera
    static int avIndex = 0;
    private static int sid;
    private static CallbackFromP2P callback;
    private static boolean isShowDialog = false;
//    private static LoadListener listener;


    public Client() {

    }

//    public static void setListener(LoadListener listen) {
//        listener = listen;
//    }

    private static void onErrReturn(FFmpegPlayer aMpegPlayer) {
        int ret = IOTCAPIs.IOTC_DeInitialize();
        mIsRunning = false;
        mIsStartAllowed = false;
//        aMpegPlayer.setMpegListener(null);
        //aMpegPlayer.stop();
    }

    public static void start(String uid, String pwd, final FFmpegPlayer aMpegPlayer, final CallbackFromP2P callbackFromP2P) {
        int cnt_wait_for_start = 0;
//        uid = "8GXLZ4UHM53KN58X111A";
        pwd = "123456";
        try {
            mIsRunning = true;
            callback = callbackFromP2P;
            System.err.println("StreamClient start...");

            // Check if client start is allowed.
            // This must be put after "mIsRunning = true" (that ensures there is only one running).
            while (!mIsStartAllowed && cnt_wait_for_start < 1000) {
                Thread.sleep(10);
                if (cnt_wait_for_start % 100 == 0) {
                    Log.v(TAG, "wait for start " + cnt_wait_for_start);
                }
                cnt_wait_for_start++;
            }

            if (!mIsStartAllowed) {
                Log.v(TAG, "Start Client not allowed");
                mIsRunning = false;
                //mIsStartAllowed = false;  // No need as it is false already,
                return;
            }


            // use which Master base on location, port 0 means to get a random port
            int ret = IOTCAPIs
                    .IOTC_Initialize(0, "50.19.254.134", "122.248.234.207",
                            "m4.iotcplatform.com", "m5.iotcplatform.com");
            System.err.printf("IOTC_Initialize() ret = %d\n", ret);


            if (ret != IOTCAPIs.IOTC_ER_NoERROR || mIsStopping) {
//                callback.message(Constants.TUTK_FAILED);
                callback.enableBackButton(false);
                onErrReturn(aMpegPlayer);
                System.err.printf("IOTCAPIs_Device exit...!! %d \n", ret);
                return;
            }

            mIsConnecting = true;
            // alloc 3 sessions for video and two-way audio
            AVAPIs.avInitialize(3);

            sid = IOTCAPIs.IOTC_Connect_ByUID(uid);
            System.err.printf("Step 2.1: call IOTC_Connect_ByUID(%s).......\n", uid);

            int[] srvType = new int[1];
            //int avIndex = AVAPIs.avClientStart(sid, "admin", "888888", 20000, srvType, 0);
            avIndex = AVAPIs.avClientStart(sid, "admin", pwd, 20, srvType, 0);
            System.err.printf("Step 2.2: call avClientStart(%d).......\n", avIndex);

            if (avIndex < 0 || mIsStopping) {
//                callback.message(Constants.TUTK_FAILED);
                callback.enableBackButton(false);
                System.err.printf("avClientStart failed[%d] DeIni=%d \n", avIndex, ret);
                mIsConnecting = false;
                IOTCAPIs.IOTC_Session_Close(sid);
                System.err.printf("IOTC_Session_Close OK\n");
                AVAPIs.avDeInitialize();
                //IOTCAPIs.IOTC_DeInitialize();
                onErrReturn(aMpegPlayer);
                System.err.printf("StreamClient exit (avIndex < 0)...\n");
                return;
            }
            mIsConnecting = false;

            System.err.printf("Start to play video\n");
            if (testOn == 1) {
                aMpegPlayer.resume(); // This is to play a test file. Need pause() later
            }

            if (startIpcamStream(avIndex) && !mIsStopping) {
                mIsPlaying = true;
                System.err.printf("Camera is ready, start to play video");
                callback.enableBackButton(true);
                callback.streamSucess(true);
                aMpegPlayer.startCamera(avIndex);
                aMpegPlayer.resume();
//                if (listener != null){
//                    listener.onLoadListener(0);
//                }
//                if (videoThreadFromMpegPlayer == 0) {
//                    runnableVideoThread = new VideoThread(avIndex, aMpegPlayer);
//                    videoThread = new Thread(runnableVideoThread,
//                            "Video Thread");
//                }
                if (audioThreadFromMpegPlayer == 0) {
                    runnableAudioThread = new AudioThread(avIndex, aMpegPlayer);
                    audioThread = new Thread(runnableAudioThread,
                            "Audio Thread");
                }
//                if (videoThreadFromMpegPlayer == 0) {
//                    videoThread.start();
//                }
                if (audioThreadFromMpegPlayer == 0) {
                    audioThread.start();
                }
//                if (videoThreadFromMpegPlayer == 0) {
//                    try {
//                        videoThread.join();
//                    } catch (InterruptedException e) {
//                        System.err.println(e.getMessage());
//                        mIsPlaying = false;
//                        mIsRunning = false;
//                        mIsStartAllowed = false;
//                        return;
//                    }
//                }
                if (audioThreadFromMpegPlayer == 0) {
                    try {
                        audioThread.join();
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        mIsPlaying = false;
                        mIsRunning = false;
                        mIsStartAllowed = false;
                        return;
                    }
                }

                // If none of video or audio thread is running, we need a dummyThread here
                // Otherwise, this client thread will stop.
//                if (videoThreadFromMpegPlayer != 0 && audioThreadFromMpegPlayer != 0) {
//                    runnableDummyThread = new DummyThread(avIndex, aMpegPlayer);
//                    dummyThread = new Thread(runnableDummyThread,
//                            "Dummy Thread");
//                    dummyThread.start();
//                    try {
//                        dummyThread.join();
//                    } catch (InterruptedException e) {
//                        System.err.println(e.getMessage());
//                        mIsPlaying = false;
//                        mIsRunning = false;
//                        mIsStartAllowed = false;
//                        return;
//                    }
//                }

            }

//            new AsyncTask<Void, Void, Void>() {
//
//                @Override
//                protected Void doInBackground(Void... voids) {
//
//                    aMpegPlayer.setMpegListener(null);
//                    aMpegPlayer.stop();
//                    AVAPIs av = new AVAPIs();
//                    av.avSendIOCtrlExit(avIndex);
//                    int IOTYPE_USER_IPCAM_STOP = 0x2FF;
//                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_STOP,
//                            new byte[8], 8);
//                    AVAPIs.avClientStop(avIndex);
//                    System.err.printf("avClientStop OK...failed\n");
//                    IOTCAPIs.IOTC_Session_Close(sid);
//                    System.err.printf("IOTC_Session_Close OK.failed\n");
//                    AVAPIs.avDeInitialize();
//                    IOTCAPIs.IOTC_DeInitialize();
//                    try {
//                        Thread.sleep(40000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            }.execute();
            System.out.println("Client.start................failed");
//            callback.message(Constants.TUTK_FAILED);
            callback.enableBackButton(true);
            Log.v(TAG, "unable to connect to Camera");
            aMpegPlayer.setMpegListener(null);
            aMpegPlayer.stop();
            AVAPIs av = new AVAPIs();
            av.avSendIOCtrlExit(avIndex);
            int IOTYPE_USER_IPCAM_STOP = 0x2FF;
            ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_STOP,
                    new byte[8], 8);
            int IOTYPE_USER_IPCAM_AUDIOSTOP     = 0x0301;
            ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTOP,
                    new byte[8], 8);
            AVAPIs.avClientStop(avIndex);
            System.err.printf("avClientStop OK...failed\n");
            IOTCAPIs.IOTC_Session_Close(sid);
            System.err.printf("IOTC_Session_Close OK.failed\n");
            AVAPIs.avDeInitialize();
            IOTCAPIs.IOTC_DeInitialize();
            System.err.printf("StreamClient exit..failed..\n");
            LogUtils.i(TAG, "StreamClient:  " + mIsPlaying);
            mIsPlaying = false;
            mIsRunning = false;
            mIsStartAllowed = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean startIpcamStream(int avIndex) {
        AVAPIs av = new AVAPIs();
        int ret = av.avSendIOCtrl(avIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        if (ret < 0) {
            System.err.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        // This IOTYPE constant and its corrsponsing data structure is defined in
        // Sample/Linux/Sample_AVAPIs/AVIOCTRLDEFs.h
        //
        int IOTYPE_USER_IPCAM_START = 0x1FF;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START,
                new byte[8], 8);
        if (ret < 0) {
            System.err.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        int IOTYPE_USER_IPCAM_AUDIOSTART = 0x300;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART,
                new byte[8], 8);
        if (ret < 0) {
            Log.v(TAG, "startIpcamStream() failed" + ret);
            return false;
        }

        return true;
    }

    public static class VideoThread implements Runnable {
        static final int VIDEO_BUF_SIZE = 100000;
        static final int FRAME_INFO_SIZE = 16;

        private int avIndex;
        private FFmpegPlayer mMpegPlayer = null;
        private volatile boolean running = true;

        public void terminate() {
            running = false;
        }

        public VideoThread(int avIndex, FFmpegPlayer aMpegPlayer) {
            this.avIndex = avIndex;
            this.mMpegPlayer = aMpegPlayer;
        }

        @Override
        public void run() {
            //if (mMpegPlayer != null) {
            int ret2 = mMpegPlayer.hello();
            //}
            System.err.printf("[%s] Start %d\n",
                    Thread.currentThread().getName(), ret2);
            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] videoBuffer = new byte[VIDEO_BUF_SIZE];
            int i = 0;
            int count = 10;
            int cntNotReady = 0;
            int cntFrame = 0;
            int cntIncomplete = 0;
            int cntLostFrame = 0;

            int flagFrameType = 0;  // 0: Pframe; 1: IFrame.
            int flagDecFramePrev = 0;
            int flagDecFrame = 0;   // frame decoding flag
            //  0: normal
            // -1: full.
            int cntPframe = 0;
            int thSkipPframe = 20;  // Count thSkipPframe frames and then skip the rest.
            int bufLen = 0;
            // -------------------------------------------------------
            // Stop mMpegPlayer, which is used to test the surface.
            // This section will be removed completely later.
            if (testOn == 1) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                mMpegPlayer.pause();
            }
            mMpegPlayer.resume();
            // -------------------------------------------------------
            //mMpegPlayer.setAvIndex(avIndex);
            while (running) {
                int[] frameNumber = new int[1];
                int[] outBufSize = new int[1];
                int[] outFrmSize = new int[1];
                int[] outFrmInfoBufSize = new int[1];
                int ret;
                int sel_dec = 2;
                if (sel_dec == 2) {
                    ret = av.avRecvFrameData2(avIndex, videoBuffer, VIDEO_BUF_SIZE,
                            outBufSize, outFrmSize,
                            frameInfo, FRAME_INFO_SIZE, outFrmInfoBufSize,
                            frameNumber);
                } else {
                    ret = av.avRecvFrameData(avIndex, videoBuffer,
                            VIDEO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                            frameNumber);
                }
//                int ret = mMpegPlayer.receiveFrameData(avIndex, videoBuffer,
//                                VIDEO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE, frameNumber);
                if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
                    try {
                        Thread.sleep(30);
                        cntNotReady++;
                        if (cntNotReady >= 100) {
                            System.err.printf("[%s] Not ready\n",
                                    Thread.currentThread().getName());
                            cntNotReady = 0;
                        }
                        continue;
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        break;
                    }
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    cntLostFrame++;
                    if (cntLostFrame % 100 == 0) {
                        System.err.printf("[%s] Lost video frame number[%d]\n",
                                Thread.currentThread().getName(), frameNumber[0]);
                    }
                    continue;
                } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                    cntIncomplete++;
                    if (cntIncomplete >= 100) {
                        System.err.printf("[%s] Incomplete video frame number[%d] cntIncomplete=%d\n",
                                Thread.currentThread().getName(), frameNumber[0], cntIncomplete);
                        cntIncomplete = 0;
                    }
                    continue;
                } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.err.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.err.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.err.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }

                // Now the data is ready in videoBuffer[0 ... ret - 1]
                // Do something here
                int ret4 = 0;
                // ret4 = mMpegPlayer.decodeVideoFrame(frameInfo, 16); // test only
                int ret3 = 0;
                /*
                // The following printByteArray() is used for debugging
            	// From the printed bytes, we know that "frameInfo[2]" is an IFrame (=1)
            	// and PFrame (=0) indicator.
            	printByteArray2(videoBuffer, ret, 16, "chk vBuffer[" + ret + "] "+ frameNumber[0] + " " + outFrmSize[0]);
            	printByteArray2(videoBuffer, ret + 24, 16, "chk vBuffer[" + ret + "+24] "+ frameNumber[0] + " " + outFrmSize[0]);
            	printByteArray(videoBuffer, 32, "vBuffer " + frameNumber[0] + " " + outFrmSize[0]);
                printByteArray(frameInfo, 16, "vFrameInfo " + frameNumber[0] + " " + outFrmSize[0]);
                */
                //printByteArray(frameInfo, 16, "vFrameInfo " + frameNumber[0] + " " + outFrmSize[0]);
                int timeStamp;
                timeStamp = (int) (frameInfo[12] + frameInfo[13] * Math.pow(2, 8) + frameInfo[14] * Math.pow(2, 16)
                        + frameInfo[15] * Math.pow(2, 24));

                //System.out.printf("%s frame No %d size=%d timeStamp=%d\n",
                //		Thread.currentThread().getName(), frameNumber[0], ret, timeStamp);

                bufLen = ret;
                flagFrameType = frameInfo[2];        // This is frame type flag.
                if (flagFrameType == 0x65){
                    flagFrameType = 0x01;
                }else if(flagFrameType == 0x61){
                    flagFrameType = 0;
                }
                flagDecFramePrev = flagDecFrame;
                if (flagDecFrame == 1 && flagFrameType == 1) {    // Half full previously and this is a P frame.

                }

                if (flagDecFrame >= 0 || flagFrameType == 1) { // not full before or this is I frame.
                    if (flagFrameType == 1) {
                        System.err.printf("%s I frame No %d size=%d cntFrame=%d timeStamp=%d\n",
                                Thread.currentThread().getName(), frameNumber[0], ret, cntFrame, timeStamp);
                    }
                    flagDecFrame = mMpegPlayer.decodeVideoFrame(videoBuffer, ret, flagFrameType);
                    if (flagDecFrame != flagDecFramePrev && (flagDecFrame < 0 || flagDecFramePrev < 0)) {
                        System.err.printf("Buf frame No %d size=(%d,%d,%d) ret3=%d, outFrmInfoSize=%d cntFrame=%d\n",
                                frameNumber[0], outFrmSize[0], outBufSize[0], ret, flagDecFrame,
                                outFrmInfoBufSize[0], cntFrame);
                    }
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /*
                setByteArray(videoBuffer, (byte) 255, bufLen, VIDEO_BUF_SIZE - bufLen - 1);
            	System.out.printf("After set Chk vBuffer " + frameNumber[0] + " " + outFrmSize[0] + 
            			videoBuffer[bufLen] + " " + videoBuffer[bufLen+1] + " " + videoBuffer[VIDEO_BUF_SIZE - 2]);
            			*/
                cntFrame++;
                if (cntFrame % 500 == 0) {
                    System.err.printf("%s frame No %d size=(%d,%d,%d) ret3=%d, outFrmInfoSize=%d cntFrame=%d\n",
                            Thread.currentThread().getName(),
                            frameNumber[0], outFrmSize[0], outBufSize[0], ret, flagDecFrame,
                            outFrmInfoBufSize[0], cntFrame);
                }
                cntIncomplete = 0;
                cntNotReady = 0;


            }

            System.err.printf("[%s] Exit\n",
                    Thread.currentThread().getName());
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesArrayToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String byteToHex(byte byteIn) {
        char[] hexChars = new char[2];
        int v = byteIn & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }

    private static void printByteArray2(byte[] dataBuf, int startPos, int bufLen, String name) {
        int n;
        System.err.printf("printByteArray() name=%s\n", name);
        String line = "";
        for (n = 0; n < bufLen; n++) {
            if (n % 16 == 0) {
                System.err.printf("%s\n", line);
                line = "";
            }
            line = line + " " + byteToHex(dataBuf[startPos + n]);
        }
        System.err.printf("%s\n", line);
    }

    // Print bytes on screen
    private static void printByteArray(byte[] dataBuf, int bufLen, String name) {
        int n;
        System.err.printf("printByteArray() name=%s\n", name);
        String line = "";
        for (n = 0; n < bufLen; n++) {
            if (n % 16 == 0) {
                System.err.printf("%s\n", line);
                line = "";
            }
            line = line + " " + byteToHex(dataBuf[n]);
        }
        System.err.printf("%s\n", line);
    }

    // Set the content of dataBuf[startPos] to dataBuf[startPos + nSamp - 1 ] to val
    private static void setByteArray(byte[] dataBuf, byte val, int startPos, int nSamp) {
        int n;
        for (n = 0; n < nSamp; n++) {
            dataBuf[startPos + n] = val;
        }
    }

    public static class AudioThread implements Runnable {
        static final int AUDIO_BUF_SIZE = 1024;
        static final int FRAME_INFO_SIZE = 16;

        static final int N_AUDIO_BUF_BLKS = 4;

        private int avIndex;
        private FFmpegPlayer mMpegPlayer;

        private volatile boolean running = true;

        public void terminate() {
            running = false;
        }


        public AudioThread(int avIndex, FFmpegPlayer aMpegPlayer) {
            this.avIndex = avIndex;
            this.mMpegPlayer = aMpegPlayer;
        }

        @Override
        public void run() {
            System.err.printf("[%s] Start\n",
                    Thread.currentThread().getName());

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] audioBuffer = new byte[AUDIO_BUF_SIZE];


            byte[] audioBufferN = new byte[N_AUDIO_BUF_BLKS * AUDIO_BUF_SIZE];
            int cntAudioBufBlk = 0;
            int enInitDelay = 0;    // Set to 1 to enable an initial delay
            // Audio starts to play after
            // N_AUDIO_BUF_BLKS has been received.
            int bufLen = 0;
            int curBufLen = 0;
            int cntFrame = 0;
            int sampleRateInHz = 8000;

            mMpegPlayer.createAudioTrack(sampleRateInHz, 1);
            mMpegPlayer.playAudio();
            boolean isPrevFailed = false;
            int count = 0;
            while (running) {
                int ret = av.avCheckAudioBuf(avIndex);

                if (ret < 0) {
                    if (!isPrevFailed) {
                        count = 0;
                    }
                    isPrevFailed = true;
                    count++;
                    if (count >= 50) {
                        break;
                    }
                    // Same error codes as below
                    Log.v(TAG, "Audio avCheckAudioBuf() failed" + ret);
                    isShowDialog = true;
                    continue;   // Changed from the original "break"
                } else if (ret < 3) {
                    try {
                        Thread.sleep(120);
                        continue;
                    } catch (InterruptedException e) {
                        Log.v(TAG, "InterruptedException" + e.getMessage());
                        break;
                    }
                }

                int[] frameNumber = new int[1];
                ret = av.avRecvAudioData(avIndex, audioBuffer,
                        AUDIO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                        frameNumber);

                if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    Log.v(TAG, "AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE");
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    Log.v(TAG, "AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT");
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    Log.v(TAG, "Audio AVAPIs.AV_ER_INVALID_SID");
                    break;
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    //System.out.printf("[%s] Audio frame losed\n",
                    //        Thread.currentThread().getName());
                    continue;
                }

                // Now the data is ready in audioBuffer[0 ... ret - 1]
                // Do something here
                cntFrame++;

                if (cntFrame % 1 == 0) {
//                    Log.v(TAG, "Audio frame info" + ret);
                }
                //printByteArray(audioBuffer, 64, "AudioBuffer");
                //printByteArray(frameInfo, 16, "AudioFrameInfo");
                curBufLen = ret;
                // Do not call decodeAudioFrameNative() as it is NOT working
                //int ret2 = mMpegPlayer.decodeAudioFrameNative(audioBuffer, curBufLen);

                if (enInitDelay == 0) {
                    mMpegPlayer.writeAudio(audioBuffer, curBufLen);
                } else {
                    // Here we buffer N_AUDIO_BUF_BLKS audioBuffer and then start to play.
                    if (cntAudioBufBlk < N_AUDIO_BUF_BLKS) {
                        System.arraycopy(audioBuffer, 0, audioBufferN, bufLen, curBufLen);
                        cntAudioBufBlk++;        // Number of blks in audioBufferN.
                        bufLen += curBufLen;            // Offset for next copy.
                    } else {
                        // At this time, we have initial data in "audioBufferN"
                        // and new data in "audioBuffer".
                        if (cntAudioBufBlk == N_AUDIO_BUF_BLKS) {
                            mMpegPlayer.writeAudio(audioBufferN, bufLen);
                            cntAudioBufBlk++;    // ++ to make sure this "if" is executed once.
                        }
                        // Play the data in the current buffer.
                        mMpegPlayer.writeAudio(audioBuffer, curBufLen);
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.printf("[%s] audio ret=%d %d\n",
                //        Thread.currentThread().getName(), ret, ret2);
            }

            Log.v(TAG, "Audio thread exit");
//            if (!isShowDialog)
//                callback.enableBackButton(false);
        }
    }

    public static class DummyThread implements Runnable {
        private int avIndex;
        private FFmpegPlayer mMpegPlayer;

        private volatile boolean running = true;

        public void terminate() {
            running = false;
        }

        public DummyThread(int avIndex, FFmpegPlayer aMpegPlayer) {
            this.avIndex = avIndex;
            this.mMpegPlayer = aMpegPlayer;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isConnecting() {
        return mIsConnecting;
    }

    public static boolean isPlaying() {
        return mIsPlaying;
    }

    public static boolean isRunning() {
        return mIsRunning;
    }

    public static void setStoppingFlag(boolean aIsStopping) {
        mIsStopping = aIsStopping;
    }

    public static void setStartAllowed() {
        System.err.println("Client.setStartAllowed");
        mIsStartAllowed = true;
    }

    public static void stop() {
        mIsStartAllowed = false;
        try {
            if (mIsPlaying) {
                if (runnableVideoThread != null) {
                    runnableVideoThread.terminate();
                }
                if (runnableAudioThread != null) {
                    runnableAudioThread.terminate();
                }
                if (runnableDummyThread != null) {
                    runnableDummyThread.terminate();
                }


//                AVAPIs av = new AVAPIs();
//                av.avSendIOCtrlExit(avIndex);
//                int IOTYPE_USER_IPCAM_STOP = 0x2FF;
//                int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_STOP,
//                        new byte[8], 8);
//                AVAPIs.avClientStop(avIndex);
//                System.err.printf("avClientStop OK\n");
//                IOTCAPIs.IOTC_Session_Close(sid);
//                System.err.printf("IOTC_Session_Close OK\n");
//                AVAPIs.avDeInitialize();
//                IOTCAPIs.IOTC_DeInitialize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Client:stop()" + mIsPlaying);
    }
}
