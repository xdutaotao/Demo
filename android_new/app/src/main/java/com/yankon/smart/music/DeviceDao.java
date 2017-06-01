package com.yankon.smart.music;

import java.io.File;

import org.cybergarage.upnp.Device;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yankon.smart.activities.AudioActivity;
import com.yankon.smart.music.dlna.DLNAContainer;
import com.yankon.smart.music.dlna.IController;
import com.yankon.smart.music.dlna.MultiPointController;
import com.yankon.smart.utils.Utils;

public class DeviceDao {
	private static DeviceDao dao = null;
	private static IController controller = null;
	private Device device = null;
	private int mMediaDuration = 0;// 当前播放的长度
	public static int getleng = 0;// 上次播放的长度
	private String positio;
	private String mediaDuration;
	private Boolean ispositio=false;//是否正在广播播放进度
	public static MusicInfo info;//是否为同首歌
	private int du=0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			}
	};

	public static DeviceDao getDeviceDao() {
		if (dao == null) {
			dao = new DeviceDao();
		}
		if (controller == null) {
			controller = new MultiPointController();
		}

		return dao;
		

	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	private boolean is;
/**
 * 播放音乐
 * @param path
 */
	public void play(final MusicInfo path) {
		du=0;
		mMediaDuration = 0;
		device = DLNAContainer.getInstance().getSelectedDevice();
		if (device == null || controller == null) {
			// usually can't reach here.
			Log.e("a", "device+controller为空了");
		}
//		controller = new MultiPointController();
		new Thread() {
			public void run() {
				is = false;
				is = controller.play(device, path.getSrc());
				
				Log.e("a", "is" + is);
				if (is) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_TRUE,
							path);
					getLength(info);
				}
			}
		}.start();
		

	}
	public void play(final String path,final MusicInfo info) {
		du=0;
		mMediaDuration = 0;
		device = DLNAContainer.getInstance().getSelectedDevice();
		if (info.getLength() == null){
			MediaPlayer mMediaPlayer = new MediaPlayer();
			try {
//				File f = new File(AudioActivity.MP3SRC, info.getId() + ".mp3");
				File f = new File(info.getSDsrc());
				if (!f.exists()) {
					mMediaPlayer.setDataSource(info.getSrc());
				} else {
//					mMediaPlayer.setDataSource(AudioActivity.MP3SRC + info.getId() + ".mp3");
					mMediaPlayer.setDataSource(info.getSDsrc());
				}
				mMediaPlayer.prepare(); // 进行缓冲
			} catch (Exception e) {
				e.printStackTrace();
			}
			info.setLength(mMediaPlayer.getDuration() + "");
			Integer time = Integer.parseInt(Utils.getTime(Utils.getSyTime()));
			info.setPlaytime(time);
		}
		
		if (device == null || controller == null) {
			// usually can't reach here.
			Log.e("a", "device+controller为空了");
		}
		new Thread() {
			public void run() {
				is = false;
				is = controller.play(device, path);
				Log.e("a", "is" + is);
				if (is) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_TRUE,
							info);
					getLength(info);

				}
			}
		}.start();

	}
	/**
	 * 暂停播放
	 */
	public void pause() {

		new Thread() {
			public void run() {
				if(Deiviceutis.mPlaying){
					final boolean isSuccess = controller.pause(device);
						if (isSuccess) {
							Deiviceutis.mPlaying=false;
							MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_PAUSE,"");
							
						}else{
							Deiviceutis.mPlaying=true;
						}
				}
				
			};
		}.start();
	}
	/**
	 * 暂停播放
	 */
	public void stop() {

		new Thread() {
			public void run() {
				if(Deiviceutis.mPlaying){
					final boolean isSuccess = controller.stop(device);
						if (isSuccess) {
							Deiviceutis.mPlaying=false;
							MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_PAUSE,"");

						}else{
							Deiviceutis.mPlaying=true;
						}
				}
				
			};
		}.start();
	}
	/**
	 * 继续播放
	 * @param
	 */
	public void  goon() {
		new Thread() {
			@Override
			public void run() {
				Log.e("a", "到了goon"+positio);
				
				if (device == null)
					device = DLNAContainer.getInstance().getSelectedDevice();
				if (positio == null)
					getPositio();
				
				final boolean isSuccess = controller.goon(device,
						positio);
				
				if (isSuccess) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_GOON,"");
				}else{
					Deiviceutis.mPlaying=false;
				}

			}
		}.start();
	}
	
	private void getLength(final MusicInfo info) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//暴力添加
				mediaDuration = controller.getMediaDuration(device);
				int lenght = Integer.valueOf(info.getLength());
				mediaDuration = Utils.getTimeFromInt(lenght);
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mMediaDuration = Deiviceutis.getIntLength(mediaDuration);
					
						if (mMediaDuration == 0) {
							getLength(info);
						} else {
							if (mMediaDuration == getleng) {
								du++;
								if(du<20){
									getLength(info);
								}
							} else {
								getleng = mMediaDuration;
								if(!ispositio){
									getPositio();
									ispositio=true;
								}
								Deiviceutis.mPlaying=true;
								MManager.sendMessage(mHandler,
										MManager.MessageType.MSG_PALY_MUSICLENGTH,
										mMediaDuration);
							}
						}

					}
				}, 1000);

			}
		}).start();
	}

	public void getPositio() {
		new Thread() {
			public void run() {
				positio = controller.getPositionInfo(device);
				MManager.sendMessage(mHandler,
						MManager.MessageType.MSG_PALY_POSITIO,Deiviceutis.getIntLength(positio));
				mHandler.postDelayed(new Runnable() {		
					@Override
					public void run() {
						getPositio();
						
					}
				},1000);
			};
		}.start();
	}

	
	
	public void  seek(final String targetPosition) {
		new Thread() {
			@Override
			public void run() {
				boolean isSuccess = controller.seek(device, targetPosition);
				if (isSuccess) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MManager.MessageType.MSG_PALY_GOON,"");
				} else {
					Deiviceutis.mPlaying=false;
				}

			}
		}.start();
	}
	
	
	
	public void setVoice(final int voice){
		new Thread() {
			@Override
			public void run() {
				boolean isSuccess = controller.setVoice(device, voice);
//				if (isSuccess) {
//					Deiviceutis.mPlaying=true;
//					MManager.sendMessage(mHandler, MessageType.MSG_PALY_GOON,"");
//				} else {
//					Deiviceutis.mPlaying=false;
//				}

			}
		}.start();
	}
	

	/**
	 * 传flase关闭读取进度线程
	 * @param posit
	 */
	public void setPositioThre(boolean posit){
		ispositio=false;
	}
	
	
}
