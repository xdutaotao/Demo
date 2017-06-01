package com.tutk.sample.AVAPI;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tutk.ffmpeg.DispTaskHandler;
import com.tutk.ffmpeg.EosTaskHandler;
import com.tutk.ffmpeg.FFmpegError;
import com.tutk.ffmpeg.FFmpegListener;
import com.tutk.ffmpeg.FFmpegPlayer;
import com.tutk.ffmpeg.FFmpegStreamInfo;
import com.tutk.ffmpeg.FFmpegSurfaceView;
import com.tutk.ffmpeg.NotPlayingException;
import com.yankon.smart.R;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.utils.Utils;

import java.io.File;

public class CameraStream extends Fragment implements
        FFmpegListener, OnSeekBarChangeListener, EosTaskHandler,
        DispTaskHandler, CallbackFromP2P {

    static String UID = "D7C991NEJE2FBH6PWFZ1";
    //static final String UID = "UU473RHK5A97ZGBY111A";
    //static String UID="E3FT8HREMFUUAN6KSTY1";
    static String PWD = "888888";
    /**
     * Called when the activity is first created.
     */

    private static final String TAG = "CameraStream";

    private final String FRAME_DUMP_FOLDER_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "android-camera-view";

    private FFmpegPlayer mMpegPlayer;
    private FFmpegSurfaceView mSurface;
    private static SurfaceHolder mHolder;

    //	private Button mMuteButton;
//	private Button mPauseButton;
//	private Button mSnapshotButton;
    protected boolean mMute = false;
    protected boolean mPlay = true;
//    private TextView failure;
//    private TextView back;
//    private ProgressBar progressBar;
//    private TextView camName;

    private ProgressDialogFragment dialogFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffmpegplayer_video, null);
        mSurface = (FFmpegSurfaceView) view.findViewById(R.id.surfaceview);
//        failure = (TextView) view.findViewById(R.id.failure_msg_p2p);
//        progressBar = (ProgressBar) view.findViewById(R.id.progress_p2p);
//        back = (TextView) view.findViewById(R.id.back);
//        camName = (TextView) view.findViewById(R.id.camera_id);
//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getFragmentManager().popBackStack();
//            }
//        });

//        showLoadingDialog();
        UID = (String) getArguments().get("uid");
        return view;
    }

    private void showLoadingDialog(){
        dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.loading));
//        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDispCreated() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEndOfStream() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub

    }

//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//        switch (viewId) {
//            case R.id.mute:
//                muteSound();
//                return;
//            case R.id.pause:
//                pausePlay();
//                return;
//            case R.id.snapshot:
//                return;
//            default:
//                throw new RuntimeException();
//        }
//    }

    public void muteSound() {
        mMute = !mMute;
        float volume = (float) ((mMute) ? 0.0 : 1.0);
        mMpegPlayer.setVolume(volume);
//		if(mMute)
//			//this.mMuteButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
//		else
//			//this.mMuteButton.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
    }


    @Override
    public void onResume() {
        super.onResume();
        //	this.mPauseButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        Bundle arguments = getArguments();
//        Camera camera = (Camera) arguments.getParcelable(Constants.CAMERA);
//
//        back.setEnabled(false);
//        progressBar.setVisibility(View.VISIBLE);
//        UID = camera.getCameraUID();
//        String camName = camera.getName();
//        this.camName.setText(camName);
//        Log.v(TAG, "Camera UID " + UID);
        File dumpFolder = new File(FRAME_DUMP_FOLDER_PATH);
        if (!dumpFolder.exists()) {
            dumpFolder.mkdirs();
        }
        String videoFileName = "out1.mp4";    //640x360
        //copy input video file from assets folder to directory
        File videoFile = new File(FRAME_DUMP_FOLDER_PATH + "/" + videoFileName);
        if (!videoFile.exists())
            Utils.copyAssets(getActivity(), videoFileName, FRAME_DUMP_FOLDER_PATH);
        else
            Log.v(TAG, videoFileName + " already exists");

        String url = FRAME_DUMP_FOLDER_PATH + File.separator + videoFileName;
        mSurface.setDispTaskHandler((DispTaskHandler) this);

        // select ffmpegPlayer
        int testMmpegPlayer = 0; // set to 1 to test mMpegPlayer, = 0 for camera.
        int sel_pkt_io = (testMmpegPlayer == 1) ? 0 : 1;
        int disable_audio_thread = (testMmpegPlayer == 1) ? 0 : 1;
        int use_preconfig_resolution = (testMmpegPlayer == 1) ? 0 : 0;
        int id_bitmap = 0;
        int id = sel_pkt_io << 6 | disable_audio_thread << 5 | use_preconfig_resolution << 4 | id_bitmap;
        mMpegPlayer = new FFmpegPlayer(mSurface, getActivity(), id);
        mMpegPlayer.setMpegListener(this);
        mMpegPlayer.setDataSource(url, 0);
        mMpegPlayer.resume();

//		mMuteButton = (Button) view.findViewById(R.id.mute);
//		mMuteButton.setOnClickListener(this);
//
//		mPauseButton = (Button) view.findViewById(R.id.pause);
//		mPauseButton.setOnClickListener(this);
//
//		mSnapshotButton = (Button) view.findViewById(R.id.snapshot);
//		mSnapshotButton.setOnClickListener(this);
        mSurface.setVisibility(View.VISIBLE);
//        failure.setVisibility(View.GONE);
//        if (testMmpegPlayer != 1) {
            //mClient = new Client();
            (new Thread() {
                public void run() {
                    Client.start(CameraStream.this.UID, CameraStream.this.PWD, mMpegPlayer, CameraStream.this);
                    Log.v(TAG, "*********************************************************************");
                }
            }).start();
//        }

        mHolder = mSurface.getSurfaceHolder();
        if (mHolder != null)
            mMpegPlayer.resumeDisplay(mHolder);
        if (mMute != true)
            mMpegPlayer.setVolume(1);
    }

    @Override
    public void onPause() {
        super.onPause();


        //	this.mPauseButton.setBackgroundResource(android.R.drawable.ic_media_play);
        if (mMpegPlayer != null) {
            mMpegPlayer.pauseDisplay();

            mMpegPlayer.setVolume(0);
        }
        try {
            System.err.println("CameraStream.onDestroyView");

            // Stop client.
            // If it is connecting, wait
            Client.setStoppingFlag(true);
            Client.stop();
            int cnt = 0;
            do {
                cnt++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Send stop command again just in case the playing happened after
                // the previous stop().
                if (Client.isPlaying()) {
                    Client.stop();
                }
                // Post messages once in a while
                if (cnt % 5 == 0) {
                    Log.v(TAG, "onDestroy() wait for Client to release " + cnt);
                }
            } while (Client.isRunning() && cnt < 600); // wait for up to 60 seconds
            Client.setStoppingFlag(false);

            if (!Client.isPlaying()) {
                this.mMpegPlayer.setMpegListener(null);
                this.mMpegPlayer.stop();
            }
            stop();

//		super.onDestroy();

            // Wait for some time to make sure mMpegPlayer.stop() is complete
/*		try {
            Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
            cnt = 0;
            while (cnt < 1000000 * 100) {
                cnt++;
            }
            CamStreamState.isRunning = false;
            Log.v(TAG, "********************************************onDestroy exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMpegPlayer = null;

    }

    public void pausePlay() {


        mPlay = !mPlay;

        if (mPlay) {
            //this.mPauseButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            mHolder = mSurface.getSurfaceHolder();
            if (mHolder != null)
                mMpegPlayer.resumeDisplay(mHolder);
            if (mMute != true)
                mMpegPlayer.setVolume(1);
        } else {
            //this.mPauseButton.setBackgroundResource(android.R.drawable.ic_media_play);
            mMpegPlayer.pauseDisplay();
            mMpegPlayer.setVolume(0);
        }
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        System.out.println("len=" + len);
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public void onFFDataSourceLoaded(FFmpegError err, FFmpegStreamInfo[] streams) {
        // TODO Auto-generated method stub
        // Later, we post an error message.
        Log.v(TAG, "onFFDataSourceLoaded err=" + err);
        int testOn = 0;  // This is for testing only
        if (testOn == 1) {
            byte[] vdataBuf = hexStringToByteArray("00000180419a604f0f71125935e8f6bf");
            mMpegPlayer.decodeVideoFrame(vdataBuf, 16, 1);
            mMpegPlayer.decodeVideoFrame(vdataBuf, 16, 1);
        }

        if (err != null) {
            String format = getResources().getString(
                    R.string.hello_world);
            String message = String.format(format, err.getMessage());

            Builder builder = new Builder(getActivity());
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setOnCancelListener(
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    getFragmentManager().popBackStack();
                                }
                            }).show();
            return;
        }

        //Allow to connect to a camera only after player open source completed
        Client.setStartAllowed();

    }

    @Override
    public void onFFResume(NotPlayingException result) {
        // TODO Auto-generated method stub
        Log.v(TAG, "call onFFResume");

    }

    @Override
    public void onFFPause(NotPlayingException err) {
        // TODO Auto-generated method stub
/*		if(mPlay){
            Log.v(TAG, "Pause camera playing");
			mMpegPlayer.pauseDisplay();
			Log.v(TAG, "Pause camera");
			mMpegPlayer.setVolume(0);
			Log.v(TAG, "set volume to 0");

		}*/
    }

    @Override
    public void onFFStop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFFUpdateTime(long mCurrentTimeUs, long mVideoDurationUs, boolean isFinished) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFFSeeked(NotPlayingException result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onVideoSizeChanged(FFmpegPlayer mp, int width, int height) {
        // TODO Auto-generated method stub

    }

    private void stop() {
        //this.mControlsView.setVisibility(View.GONE);
        //this.mLoadingView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void message(final String message) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSurface.setVisibility(View.GONE);
//                    failure.setVisibility(View.VISIBLE);
//                    failure.setText(message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enableBackButton(boolean b) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.err.println("\nCameraStream.run enableBackButton");
//                        back.setEnabled(true);
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    public void streamSucess(boolean b) {

    }
}