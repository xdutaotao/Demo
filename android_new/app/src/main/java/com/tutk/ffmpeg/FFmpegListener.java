
package com.tutk.ffmpeg;

public interface FFmpegListener {
	void onFFDataSourceLoaded(FFmpegError err, FFmpegStreamInfo[] streams);

	void onFFResume(NotPlayingException result);

	void onFFPause(NotPlayingException err);

	void onFFStop();

	void onFFUpdateTime(long mCurrentTimeUs, long mVideoDurationUs, boolean isFinished);

	void onFFSeeked(NotPlayingException result);

	void onVideoSizeChanged(FFmpegPlayer mp, int width, int height);
	
	

}
