
package com.tutk.ffmpeg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;

public class FFmpegPlayer {
	static {
		try {
			System.loadLibrary("avutil-54");
			System.loadLibrary("avcodec-56");
			System.loadLibrary("avformat-56");
			System.loadLibrary("swscale-3");
 			System.loadLibrary("ffmpeg-jni");
		}catch (Exception e){

		}

	}

	private native int initNative(int id);
	// Return the estimated time delay (from streaming server to this client)
	private native int getTdNative();
	private native void deallocNative();

	private native int setDataSourceNative(String url,
										   Map<String, String> dictionary, int videoStreamNo,
										   int audioStreamNo, int subtitleStreamNo, int streamingFlag);

	private native void stopNative();

	native void renderFrameStart();

	native void renderFrameStop();

	private native void seekNative(long positionUs) throws NotPlayingException;

	private native long getVideoDurationNative();

	public native void render(Surface surface);
	public native void pauseDisplayNative();
	public native void resumeDisplayNative(Surface surface);

	AudioManager mAudioManager = null;
	static AudioTrack mAudioTrack = null;

	/**
	 *
	 * @param streamsInfos
	 *            - could be null
	 */
	private void setStreamsInfo(FFmpegStreamInfo[] streamsInfos) {
		this.mStreamsInfos = streamsInfos;
	}

	/**
	 * Return streamsInfo
	 *
	 * @return return streams info after successful setDataSource or null
	 */
	protected FFmpegStreamInfo[] getStreamsInfo() {
		return mStreamsInfos;
	}

	public void stop() {
		new StopTask(this).execute();
	}

	private native void setVolumeNative(float volume);

	private native void pauseNative() throws NotPlayingException;

	private native void resumeNative() throws NotPlayingException;

	private native int decodeVideoFrameNative(byte[] inBuf, int bufLen, int frmType);
	public native int decodeAudioFrameNative(byte[] inBuf, int bufLen);
	private native void startCameraNative(int avIndex);




	private static class StopTask extends AsyncTask<Void, Void, Void> {

		private final FFmpegPlayer player;
		public StopTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected Void doInBackground(Void... params) {
			player.pauseDisplayNative();
			player.stopNative();
			
			if (mAudioTrack != null) {
				mAudioTrack.stop();
				mAudioTrack.flush();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFStop();
		}

	}

	private static class SetDataSourceTaskResult {
		FFmpegError error;
		FFmpegStreamInfo[] streams;
	}

	private static class SetDataSourceTask extends
			AsyncTask<Object, Void, SetDataSourceTaskResult> {

		private final FFmpegPlayer player;

		public SetDataSourceTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected SetDataSourceTaskResult doInBackground(Object... params) {
			String url = (String) params[0];
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) params[1];
			Integer videoStream = (Integer) params[2];
			Integer audioStream = (Integer) params[3];
			Integer subtitleStream = (Integer) params[4];
			Integer isStreaming = (Integer) params[5];
			
			int videoStreamNo = videoStream == null ? -1 : videoStream.intValue();
			int audioStreamNo = audioStream == null ? -1 : audioStream.intValue();
			int subtitleStreamNo = subtitleStream == null ? -1 : subtitleStream.intValue();
			int streamingFlag = isStreaming.intValue();
			
			// ignore the map below
			int err = player.setDataSourceNative(url, null, videoStreamNo, audioStreamNo, subtitleStreamNo,
					streamingFlag);
			SetDataSourceTaskResult result = new SetDataSourceTaskResult();
			if (err < 0) {
				result.error = new FFmpegError(err);
				result.streams = null;
			} else {
				result.error = null;
				result.streams = player.getStreamsInfo();
			}
			return result;
		}

		@Override
		protected void onPostExecute(SetDataSourceTaskResult result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFDataSourceLoaded(result.error,
						result.streams);
		}

	}

	private static class SeekTask extends
			AsyncTask<Long, Void, NotPlayingException> {

		private final FFmpegPlayer player;

		public SeekTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected NotPlayingException doInBackground(Long... params) {
			try {
				player.seekNative(params[0].longValue());
			} catch (NotPlayingException e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFSeeked(result);
		}

	}

	// End of Stream task.
	private static class EosTask extends
			AsyncTask<Void, Void, NotPlayingException> {
		
			private final FFmpegPlayer player;
			
			public EosTask(FFmpegPlayer player) {
				this.player = player;
			}
			
			@Override
			protected NotPlayingException doInBackground(Void... params) {
				//player.pause();
				player.mEosHandler.onEndOfStream();
				return null;
			}
			
			@Override
			protected void onPostExecute(NotPlayingException result) {
			}
		}
	
	private static class PauseTask extends
			AsyncTask<Void, Void, NotPlayingException> {

		private final FFmpegPlayer player;

		public PauseTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected NotPlayingException doInBackground(Void... params) {
			try {
				player.pauseNative();
				return null;
			} catch (NotPlayingException e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFPause(result);
		}

	}

	private static class ResumeTask extends
		AsyncTask<Void, Void, NotPlayingException> {

		private final FFmpegPlayer player;
		
		public ResumeTask(FFmpegPlayer player) {
			this.player = player;
		}
		
		@Override
		protected NotPlayingException doInBackground(Void... params) {
			try {
				player.resumeNative();
				return null;
			} catch (NotPlayingException e) {
				return e;
			}
		}
		
		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFResume(result);
		}
		
	}
	

	


	private static final String TAG = "FFmpegPlayer";

	public static final int UNKNOWN_STREAM = -1;
	public static final int NO_STREAM = -2;
	private FFmpegListener mpegListener = null;
	private final RenderedFrame mRenderedFrame = new RenderedFrame();

	private int mNativePlayer;
	private final Activity activity;

	private Runnable updateTimeRunnable = new Runnable() {

		@Override
		public void run() {
			if (mpegListener != null) {
				mpegListener.onFFUpdateTime(mCurrentTimeUs,
					mVideoDurationUs, mIsFinished);
			}
		}

	};

	private long mCurrentTimeUs;
	private long mVideoDurationUs;
	private FFmpegStreamInfo[] mStreamsInfos = null;
	private boolean mIsFinished = false;

	static class RenderedFrame {
		public Bitmap bitmap;
		public int height;
		public int width;
	}

	public FFmpegPlayer(FFmpegSurfaceView videoView, Activity activity, int id) {
		this.activity = activity;
		//Log.d(TAG, "FfmpegPlayer before initNative mNativePlayer=" + mNativePlayer);
		int error = initNative(id);
		if (error != 0)
			throw new RuntimeException(String.format(
					"Could not initialize player: %d", error));
		int flagVideoView = 0;
		if (videoView != null) {
			videoView.setMpegPlayer(this);
			flagVideoView = 1;
		}
		//Log.d(TAG, "FfmpegPlayer done mNativePlayer=" + mNativePlayer + 
		//		" videoView=" + flagVideoView);
	}

	@Override
	protected void finalize() throws Throwable {
		deallocNative();
		super.finalize();
	}

	private EosTaskHandler mEosHandler = null; // Handle end of stream action
	private String myName = "";
	private boolean mIsDisplayIng = false;
	private boolean mIsEos = false;
	

	//private native void setAvIndexNative(int avIndex);
	
	//private native int receiveFrameDataNative(int avIndex, byte[] buf, int bufMaxSize,
	//		   byte[] pFrmInfo, int FrmInfoMaxSize, int[] pFrmNo);
	
	public void pause() {
		new PauseTask(this).execute();
	}

	public void seek(long positionUs) {
		new SeekTask(this).execute(Long.valueOf(positionUs));
	}

	public void resume() {
		new ResumeTask(this).execute();
	}

	public void startCamera(int avIndex) {
		startCameraNative(avIndex);
	}
	
	private Bitmap prepareFrame(int width, int height) {
		// Bitmap bitmap =
		// Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		this.mRenderedFrame.height = height;
		this.mRenderedFrame.width = width;
		return bitmap;
	}

	private void onUpdateTime(long currentUs, long maxUs, boolean isFinished) {

		this.mCurrentTimeUs = currentUs;
		this.mVideoDurationUs = maxUs;
		this.mIsFinished  = isFinished;
		activity.runOnUiThread(updateTimeRunnable);
	}

	public void createAudioTrack(int sampleRateInHz,
			int numberOfChannels) {
		mAudioTrack = prepareAudioTrack(sampleRateInHz, numberOfChannels);
		if (mAudioTrack == null) {
			Log.d(TAG, "createAudioTrack() failed");
		}
	}
	
	public void playAudio() {
		if (mAudioTrack != null) {
			mAudioTrack.play();
		}
	}
		
	public void writeAudio(byte[] audioData, int dataLen) {
		if (mAudioTrack != null) {
			mAudioTrack.write(audioData, 0, dataLen);
		}
	}
	
	private AudioTrack prepareAudioTrack(int sampleRateInHz,
			int numberOfChannels) {
		//System.out.println("##prepareAudioTrack\n");
		for (;;) {
			int channelConfig;
			if (numberOfChannels == 1) {
				channelConfig = AudioFormat.CHANNEL_OUT_MONO;
			} else if (numberOfChannels == 2) {
				channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
			} else if (numberOfChannels == 3) {
				channelConfig = AudioFormat.CHANNEL_OUT_FRONT_CENTER
						| AudioFormat.CHANNEL_OUT_FRONT_RIGHT
						| AudioFormat.CHANNEL_OUT_FRONT_LEFT;
			} else if (numberOfChannels == 4) {
				channelConfig = AudioFormat.CHANNEL_OUT_QUAD;
			} else if (numberOfChannels == 5) {
				channelConfig = AudioFormat.CHANNEL_OUT_QUAD
						| AudioFormat.CHANNEL_OUT_LOW_FREQUENCY;
			} else if (numberOfChannels == 6) {
				channelConfig = AudioFormat.CHANNEL_OUT_5POINT1;
			} else if (numberOfChannels == 8) {
				channelConfig = AudioFormat.CHANNEL_OUT_7POINT1;
			} else {
				channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
			}
			try {
				int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz,
						channelConfig, AudioFormat.ENCODING_PCM_16BIT);
				AudioTrack audioTrack = new AudioTrack(
						AudioManager.STREAM_MUSIC, sampleRateInHz,
						channelConfig, AudioFormat.ENCODING_PCM_16BIT,
						minBufferSize, AudioTrack.MODE_STREAM);
				return audioTrack;
			} catch (IllegalArgumentException e) {
				if (numberOfChannels > 2) {
					numberOfChannels = 2;
				} else if (numberOfChannels > 1) {
					numberOfChannels = 1;
				} else {
					throw e;
				}
			}
		}
	}

	private void setVideoListener(FFmpegListener mpegListener) {
		this.setMpegListener(mpegListener);
	}
	
	public void setDataSource(String url, int playSel) {
		myName = "playSel=" + playSel;
		int isStreaming = (playSel > 1) ? 1 : 0;
		setDataSource(url, null, UNKNOWN_STREAM, UNKNOWN_STREAM, NO_STREAM, isStreaming);
	}
	
	@SuppressLint("NewApi")
	public void setVolume(float volume) {

		if (mAudioTrack != null) {
			mAudioTrack.setVolume(volume);
		}
	}

	public void setDataSource(String url, Map<String, String> dictionary,
			int videoStream, int audioStream, int subtitlesStream, int isStreaming) {
		new SetDataSourceTask(this).execute(url, dictionary,
				Integer.valueOf(videoStream), Integer.valueOf(audioStream),
				Integer.valueOf(subtitlesStream), isStreaming);
		mIsEos = false;
	}

	public FFmpegListener getMpegListener() {
		return mpegListener;
	}

	public void setEosTaskHandler(EosTaskHandler eosHandler)
	{
		mEosHandler = eosHandler;
	}
	
	// Handle end of stream. Called from mNativePlayer (i.e. player.c)
	private void onEndOfStream() {
		Log.d(TAG, "onEndOfStream() called myName=" + myName);
		if (mEosHandler != null) {
			new EosTask(this).execute();
		}
		mIsEos = true;
	}
	
	public void setMpegListener(FFmpegListener mpegListener) {
		this.mpegListener = mpegListener;
	}
	
	// This will pause the display on the surface and release the window
	public void pauseDisplay() {
		pauseDisplayNative();
		Log.d(TAG, "pauseDisplay() called.");
		//Log.d(TAG, "pauseDisplay() called; time delay=" + getTdNative());
	}
	
	// This will pause the display on the surface and release the window
	public void resumeDisplay(SurfaceHolder holder) {
		Surface surface = holder.getSurface();
		resumeDisplayNative(surface);
		mIsDisplayIng = true;
		Log.d(TAG, "resumeDisplay() called.");
		//Log.d(TAG, "resumeDisplay() called; time delay=" + getTdNative());
	}
	
	public int getTimeDelay() {
		return getTdNative();
	}
	
	public boolean isDisplaying() {
		return mIsDisplayIng;
	}
	
	// Check if the end of stream is set.
	public boolean isEos() {
		return mIsEos;
	}
	
	public int hello() {
		Log.d(TAG, "hello");
		byte[] inBuf = {1,2,3,4};
		int ret = 0; //decodeVideoFrameNative(inBuf, 4);
		return ret;
	}
	
	public int decodeVideoFrame(byte[] inBuf, int bufLen, int frmType) {
		/*
		System.out.printf("\n");
		for (int n = 0; n < bufLen; ++n) {
			 System.out.printf("inBuf[%d]=%d ", n, inBuf[n]);
		}
		System.out.printf("\n");
		*/
		int ret = decodeVideoFrameNative(inBuf, bufLen, frmType);
		return ret;
	}
	
	/*
	public void setAvIndex(int avIndex) {
		setAvIndexNative(avIndex);
	}
	
	public int receiveFrameData(int avIndex, byte[] buf, int bufMaxSize,
			   byte[] pFrmInfo, int FrmInfoMaxSize, int[] pFrmNo){
		int ret = receiveFrameDataNative(avIndex, buf, bufMaxSize, pFrmInfo, FrmInfoMaxSize, pFrmNo);
		return ret;
	}
	*/
}
