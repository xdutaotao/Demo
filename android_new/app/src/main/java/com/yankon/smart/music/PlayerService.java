package com.yankon.smart.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import com.yankon.smart.R;
import com.yankon.smart.activities.AudioActivity;
import com.yankon.smart.activities.AudioActivity_Player;
import com.yankon.smart.providers.DBHelper;
import com.yankon.smart.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by guzhenfu on 2015/11/10.
 */
public class PlayerService extends Service {
    public static MediaPlayer mediaPlayer; // 媒体播放器对象
    private int msg; // 播放信息
    private boolean isPause; // 暂停状态
    private int current = 0; // 记录当前正在播放的音乐
    private static ArrayList<MusicInfo> mp3Infos; // 存放Mp3Info对象的集合
    private int status = 3; // 播放状态，默认为顺序播放
    private MyReceiver myReceiver; // 自定义广播接收器
    private int currentTime; // 当前播放进度
    private int maxTime; // 当前播放进度
    private int duration; // 播放长度
    private static DBHelper dbHelper;
    private MusicInfo info;// 当前播放的音乐
    private boolean isPlay = false;
    // private LrcProcess mLrcProcess; //歌词处理
    // private List<LrcContent> lrcList = new ArrayList<LrcContent>();
    // //存放歌词列表对象
    private int index = 0; // 歌词检索值
    private DeviceDao dao = DeviceDao.getDeviceDao();

    // 服务要发送的一些Actioncom.wwj.media.MUSIC_SERVICE
    public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION"; // 更新动作
    public static final String CTL_ACTION = "com.wwj.action.CTL_ACTION"; // 控制动作
    public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"; // 当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION";// 新音乐长度更新动作
    public static final String SHOW_LRC = "com.wwj.action.SHOW_LRC"; // 通知显示歌词
    public static final String PLAY_MUSIC = "com.wwj.action.PLAY_MUSIC"; // 播放音乐
    public static final String PLAY_SDMUSIC = "com.wwj.action.PLAY_SDMUSIC"; // 播放音乐
    public static final String SET_PLAYZT = "com.wwj.action.SET_PLAYZT"; // 设置播放完成后任务
    public static final String UPDATE_CTL = "UPDATE_CTL";

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置

                    // PlayActivity.lyricView.SelectIndex(currentTime);
                    Intent intent = new Intent();
                    intent.setAction(MUSIC_CURRENT);
                    intent.putExtra("currentTime", currentTime);
                    intent.putExtra("maxTime", maxTime);
                    intent.putExtra("info", info);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        dbHelper = new DBHelper(getApplicationContext());
        mp3Infos = MusicLoader.instance(getContentResolver()).getMusicList();
        setPlayStop();
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CTL_ACTION);
        filter.addAction(SHOW_LRC);
        filter.addAction(PLAY_MUSIC);
        filter.addAction(PLAY_SDMUSIC);
        registerReceiver(myReceiver, filter);
    }

    /**
     * 设置播放完成后监听
     */

    private void setPlayStop() {
        /**
         * 设置音乐播放完成时的监听器
         */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(AudioActivity_Player.isdevies){

                }else{
                    if (mp3Infos == null) {
                        mediaPlayer.start();
                    } else {
                        if (status == 1) { // 单曲循环
                            mediaPlayer.start();
                        } else if (status == 2) { // 全部循环
                            current++;
                            if (current > mp3Infos.size() - 1) { // 变为第一首的位置继续播放
                                current = 0;
                            }
                            Intent sendIntent = new Intent(UPDATE_ACTION);
                            sendIntent.putExtra("current", current);
                            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                            sendBroadcast(sendIntent);
                            play(0, null);
                        } else if (status == 3) { // 顺序播放
                            current++; // 下一首位置
                            if (current <= mp3Infos.size() - 1) {
                                play(0, null);
                            } else {
                                current = 0;
                                play(0, null);
                            }
                        } else if (status == 4) { // 随机播放
                            current = getRandomIndex(mp3Infos.size() - 1);
                            System.out.println("currentIndex ->" + current);
                            Intent sendIntent = new Intent(UPDATE_ACTION);
                            sendIntent.putExtra("current", current);
                            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                            sendBroadcast(sendIntent);
                            play(0, null);
                        }
                    }
                }


            }
        });
    }

    /**
     * 获取随机位置
     *
     * @param end
     * @return
     */
    protected int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    /**
     * 播放音乐
     *
     * @param
     */
    private void play(final int currentTime, MusicInfo infos) {

        try {
//            AudioActivity.isMyPlay = true;
            isPlay = true;
            if (infos == null) {
                if (currentTime == 0) {
                    info = mp3Infos.get(current);
                }
            } else {
                if (currentTime == 0) {
                    info = infos;
                }
            }
            //AudioActivity.ivPlayerCtl.setBackgroundResource(R.drawable.stop);
            sendPlayCtl();
            mediaPlayer.reset();// 把各项参数恢复到初始状态
//            File f = new File(AudioActivity.MP3SRC, info.getId() + ".mp3");
            File f = new File(info.getSDsrc());
            if (!f.exists()) {
                mediaPlayer.setDataSource(info.getSrc());
            } else {
//                mediaPlayer.setDataSource(AudioActivity.MP3SRC + info.getId() + ".mp3");
                mediaPlayer.setDataSource(info.getSDsrc());
            }
            mediaPlayer.prepare(); // 进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentTime == 0) {
            maxTime = mediaPlayer.getDuration();
            info.setLength(maxTime + "");
            Integer time = Integer.parseInt(Utils.getTime(Utils.getSyTime()));
            info.setPlaytime(time);
            SendCurrent();
            handler.sendEmptyMessage(1);
        }

    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
//            AudioActivity.isMyPlay = false;
            //AudioActivity.ivPlayerCtl.setBackgroundResource(R.drawable.play);
            isPlay = false;
            sendPlayCtl();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
//            AudioActivity.isMyPlay = true;
            isPlay = true;
            sendPlayCtl();
            //AudioActivity.ivPlayerCtl.setBackgroundResource(R.drawable.stop);
        }
    }

    /**
     * 上一首
     */
    private void previous() {
        if (current >= 1) {
            current--;
        } else {
            current = mp3Infos.size() - 1;
        }
        play(0, null);
    }

    /**
     * 下一首
     */
    private void next() {
        if (current < mp3Infos.size() - 1) {
            current++;
        } else {
            current = 0;
        }
        play(0, null);
    }

    /**
     * 停止音乐
     */
    public static void stop() {
        if (mediaPlayer != null) {

//            AudioActivity.isMyPlay = false;
            //AudioActivity.ivPlayerCtl.setBackgroundResource(R.drawable.play);

            mediaPlayer.stop();
        }
    }

    @Override
    public  void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            unregisterReceiver(myReceiver);
        }
        // handler.removeCallbacks(mRunnable);
    }


    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start(); // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mediaPlayer.getDuration();
            intent.putExtra("duration", duration); // 通过Intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(SHOW_LRC)) {
                current = intent.getIntExtra("listPosition", -1);
            } else if (action.equals(PLAY_MUSIC)) {
                ArrayList<MusicInfo> infos = (ArrayList<MusicInfo>) intent.getExtras().get("musicinfo");
                int cuont = intent.getIntExtra("current", 0);
                play(0, infos.get(cuont));

            } else if (action.equals(PLAY_SDMUSIC)) {
                int cuont = intent.getIntExtra("current", 0);
                current = cuont;
                play(0, null);
            } else if (action.equals(CTL_ACTION)) {
                // path = intent.getStringExtra("url"); //歌曲路径
                // current = intent.getIntExtra("listPosition", -1);
                // //当前播放歌曲的在mp3Infos的位置
                msg = intent.getIntExtra("MSG", 0); // 播放信息
                if (msg == AppConstant.PlayerMsg.PLAY_MSG) { // 直接播放音乐
                    play(0, null);
                } else if (msg == AppConstant.PlayerMsg.PAUSE_MSG) { // 暂停
                    pause();
                } else if (msg == AppConstant.PlayerMsg.STOP_MSG) { // 停止
                    stop();
                    isPlay = false;
                    sendPlayCtl();
                } else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) { // 继续播放
                    resume();
                } else if (msg == AppConstant.PlayerMsg.PRIVIOUS_MSG) { // 上一首
                    previous();
                } else if (msg == AppConstant.PlayerMsg.NEXT_MSG) { // 下一首
                    next();
                } else if (msg == AppConstant.PlayerMsg.PROGRESS_CHANGE) { // 进度更新
                    currentTime = intent.getIntExtra("progress", -1);

                    play(currentTime, null);
                } else if (msg == AppConstant.PlayerMsg.PLAYING_MSG) {
                    handler.sendEmptyMessage(1);
                }
            } else if (action.equals(SET_PLAYZT)) {
                int control = intent.getIntExtra("MSG", 0);
                switch (control) {
                    case 1:
                        status = 1; // 将播放状态置为1表示：单曲循环
                        setPlayStop();
                        break;

                    case 2:
                        status = 2; // 将播放状态置为2表示：全部循环
                        setPlayStop();
                        break;

                    case 3:
                        status = 3; // 将播放状态置为3表示：顺序播放
                        setPlayStop();
                        break;

                    case 4:
                        status = 4; // 将播放状态置为4表示：随机播放
                        setPlayStop();
                        break;
                }
            }
        }
    }

    /**
     * 发送当前播放的音乐
     */
    private void SendCurrent() {
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        sendIntent.putExtra("maxTime", maxTime);
        sendIntent.putExtra("info", info);
        sendBroadcast(sendIntent);
    }

    private void sendPlayCtl() {
        Intent sendIntent = new Intent(UPDATE_CTL);
        sendIntent.putExtra("isPlay", isPlay);
        sendBroadcast(sendIntent);
    }

}
