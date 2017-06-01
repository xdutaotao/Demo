
package com.tutk.ffmpeg;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FFmpegSurfaceView extends SurfaceView implements FFmpegDisplay,
		SurfaceHolder.Callback {

	public static enum ScaleType {
		CENTER_CROP, CENTER_INSIDE, FIT_XY
	}
	
	private static final String TAG = "FFmpegSurfaceView";
	
	private DispTaskHandler mDispHandler = null; // Handle end of stream action
	
	private FFmpegPlayer mMpegPlayer = null;
	private boolean mCreated = false;
	
	private SurfaceHolder mHolder = null;
	
	public FFmpegSurfaceView(Context context) {
		this(context, null, 0);
	}

	public FFmpegSurfaceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FFmpegSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		SurfaceHolder holder = getHolder();
		holder.setFormat(PixelFormat.RGBA_8888);
		holder.addCallback(this);
	}

	@Override
	public void setMpegPlayer(FFmpegPlayer fFmpegPlayer) {
//		if (mMpegPlayer != null)
//			throw new RuntimeException(
//					"setMpegPlayer could not be called twice");

		this.mMpegPlayer = fFmpegPlayer;
	}

	public SurfaceHolder getSurfaceHolder() {
		SurfaceHolder holder = null;
		if (mCreated) {
			//holder = getHolder();
			holder = mHolder;
		}
		return holder;
	}
	
	// This will pause the display on the surface and release the window
	public void pauseDisplay() {
		if (mHolder != null) {
			this.mMpegPlayer.pauseDisplayNative();
			Log.d(TAG, "pauseDisplay() called");
		}
	}
	
	// This will pause the display on the surface and release the window
	public void resumeDisplay() {
		if (mHolder != null) {
			Surface surface = mHolder.getSurface();
			this.mMpegPlayer.resumeDisplayNative(surface);
			Log.d(TAG, "resumeDisplay() called");
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged() called" + width + " x" + height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCreated  == true) {
			surfaceDestroyed(holder);
		}

		mHolder = holder;
		
		Surface surface = holder.getSurface();
		if (mMpegPlayer != null) {
			mMpegPlayer.render(surface);
			mMpegPlayer.resume();
			mMpegPlayer.setVolume(1); //turn on audio
		}
		mCreated = true;
		
		mDispHandler.onDispCreated();
		Log.d(TAG, "Surface for FFmpegSurfaceView created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mMpegPlayer != null) {
			//mMpegPlayer.pause();	    //comment out to allow apk to keep receiving data
			mMpegPlayer.setVolume(0);   //mute audio
			mMpegPlayer.pauseDisplayNative();   //stop yuv data convertion and display
			//mMpegPlayer.renderFrameStop();    //stop FFmpegMediaPlayer completely
		}
		mCreated = false;
		Log.d(TAG, "Surface for FFmpegSurfaceView destroyed");
	}

	public void setDispTaskHandler(DispTaskHandler dispHandler)
	{
		mDispHandler = dispHandler;
	}
}
