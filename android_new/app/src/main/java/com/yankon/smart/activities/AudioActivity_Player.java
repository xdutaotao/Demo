package com.yankon.smart.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.model.Event;
import com.yankon.smart.music.AppConstant;
import com.yankon.smart.music.Deiviceutis;
import com.yankon.smart.music.MManager;
import com.yankon.smart.music.MusicInfo;
import com.yankon.smart.music.MusicLoader;
import com.yankon.smart.music.MusicSaveDao;
import com.yankon.smart.music.NanoHTTPD;
import com.yankon.smart.music.PlayerService;
import com.yankon.smart.music.dlna.DLNAContainer;
import com.yankon.smart.music.dlna.DLNAService;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.CustomDrawable;

import org.cybergarage.upnp.Device;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by guzhenfu on 2015/11/10.
 */
public class AudioActivity_Player extends BaseActivity implements AdapterView.OnItemClickListener {
    public static ArrayList<MusicInfo> musicList;
    private List<Device> mDevices;
    public HomeReceiver homeReceiver;
    private ProgressDialogFragment dialogFragment;
    public static MusicInfo info;
    /**
     * 记录当前是否在播放
     */
    //public static DeviceDao dao = DeviceDao.getDeviceDao();
    private boolean isMyPlay = false;
    public Bitmap bitmap = null;
    public static boolean isdevies = false;// 是否连接上设备
    public int postdevies=0;
    private int mVolume = 0;
    private int currentTime;// 当前播放的时间
    private int a;
//    private static int lastTime = -1;
    private int maxTime;// 最大时间
    private String MusicName = "";// 当前播放音乐的名字
    private NanoHTTPD nanoHTTPD;
    public  static int Selected=0;
    public  String path;
    private int progres = 0;// 拖动的位置

    private SeekBar seekBar;
    private ImageView ivPlayerCtl;
    private TextView tvCurrentPos, tvLengthPos, tvTitle;
    private ListView lvMusicList;
    private TextView tvAddDevice;
    private boolean isSeekBarChanged = false;
    private LoadMusicThread loadMusicThread;
    private boolean isFirstLaunch = false;

    public void onEventMainThread(Event.SendMusicMsgEvent event){
        Message msg = event.getMusicMsg();
        MManager.MessageType mt = MManager.MessageType.values()[msg.what];

        LogUtils.i("TAG", "onEventMainThread SendMusicMsgEvent Get:" + mt);

        switch (mt) {
            case MSG_PALY_MUSICLENGTH:
                a = (Integer) msg.obj;
                Deiviceutis.length=a;
                seekBar.setMax(a);
                seekBar.setEnabled(true);
                ivPlayerCtl.setBackgroundResource(R.drawable.stop);
                tvLengthPos.setText(Utils.getTimeFromInt(a * 1000) + "");
                Global.mMusicSaveDao.setMusicLengthString(Utils.getTimeFromInt(a * 1000) + "");
//                    lastTime = -1;
                break;
            case MSG_PALY_TRUE:
                MusicInfo ms = (MusicInfo) msg.obj;
                String mMusicName = ms.getTitle();
                LogUtils.i("mHandler", "mMusicName : " + mMusicName + "-------");
                if (mMusicName.contains("]"))
                    mMusicName = mMusicName.substring(0, mMusicName.lastIndexOf("["));
                tvTitle.setText(mMusicName);
                LogUtils.i("mHandler", "mMusicName : " + mMusicName + "-------After");
                Global.mMusicSaveDao.setTitle(mMusicName);
                Deiviceutis.info=ms;
                ivPlayerCtl.setBackgroundResource(R.drawable.stop);
                break;
            case MSG_PALY_POSITIO:
                if(Deiviceutis.mPlaying){
                    int posi = (Integer) msg.obj;
//                    if (posi == 0)
//                        break;
                    LogUtils.i("mHandler", "posi: " + posi + "--------------------" + isSeekBarChanged);
                    if (!isSeekBarChanged) {
                        seekBar.setProgress(posi);
                    }else {
                        isSeekBarChanged = false;
                    }
                    tvCurrentPos.setText(Utils.getTimeFromInt(posi * 1000) + "");
                    if (TextUtils.equals(Utils.getTimeFromInt(posi * 1000) + "", Global.mMusicSaveDao.getMusicLengthString())){
                        if (Selected+1 < musicList.size())
                            Selected ++;
                        else
                            Selected = 0;

                        Global.mMusicSaveDao.dao.stop();
                        String p = musicList.get(Selected).getSDsrc();
                        String s = p.substring(p.lastIndexOf("/") + 1);
                        path = "http://" + Utils.intToIp2(Global.myIP) + ":8089/" + s;
                        info = musicList.get(Selected);
                        Global.mMusicSaveDao.dao.play(path, info);
                        Global.mMusicSaveDao.setTitle(musicList.get(Selected).getTitle());
                    }
                }

                break;
            case MSG_PALY_PAUSE:
                ivPlayerCtl.setBackgroundResource(R.drawable.play);
                break;
            case MSG_PALY_GOON:
                ivPlayerCtl.setBackgroundResource(R.drawable.stop);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        initActivityUI();
        startService(new Intent(this, PlayerService.class));
        Global.mMusicSaveDao = MusicSaveDao.getInstance();
        EventBus.getDefault().register(this);
        loadMusicThread = new LoadMusicThread();
        loadMusicThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDLNAService();
        if (isdevies){
            seekBar.setMax( Deiviceutis.length);
            seekBar.setEnabled(true);
        }else{
            Global.mMusicSaveDao.dao.setHandler(new Handler());
            setReceiver();
        }

        if(isMyPlay||Deiviceutis.mPlaying){
            ivPlayerCtl.setBackgroundResource(R.drawable.stop);
            if(Deiviceutis.mPlaying){
                tvTitle.setText(Deiviceutis.info.getTitle());
            }
        }else{
            ivPlayerCtl.setBackgroundResource(R.drawable.play);
        }

        if (Global.mMusicSaveDao != null){
            DLNAContainer.getInstance().setSelectedDevice(Global.mMusicSaveDao.getDevice());
            tvLengthPos.setText(Global.mMusicSaveDao.getMusicLengthString());
            tvTitle.setText(Global.mMusicSaveDao.getTitle());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, DLNAService.class));
        if (homeReceiver != null) {
            unregisterReceiver(homeReceiver);
            homeReceiver = null;
        }
    }

    @Override
    public void initActivityUI() {
        super.initActivityUI();
        setTitle(getString(R.string.audio));
        lvMusicList = (ListView) findViewById(R.id.lv_songs);
        lvMusicList.setOnItemClickListener(this);

        showLoadingDialog();

        seekBar = (SeekBar) findViewById(R.id.pb_player_progress);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isdevies) {
                    String targetPosition = Deiviceutis.secToTime(progres);
                    Global.mMusicSaveDao.dao.seek(targetPosition);
//                    lastTime = -1;
                } else {
                    Intent intent = new Intent();
                    intent.setAction(PlayerService.CTL_ACTION);
                    intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
                    intent.putExtra("progress", progres);
                    sendBroadcast(intent);
                }
                isSeekBarChanged = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    progres = progress;
                    seekBar.setProgress(progress);
                }
            }
        });

        ivPlayerCtl = (ImageView) findViewById(R.id.ibtn_player_control);
        ivPlayerCtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isdevies){
                    if(Deiviceutis.mPlaying){
                        Global.mMusicSaveDao.dao.pause();
                    }else{
                        Global.mMusicSaveDao.dao.goon();
                    }
                }else{
                    if (isMyPlay) {
                        Intent intent = new Intent();
                        intent.setAction(PlayerService.CTL_ACTION);
                        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(PlayerService.CTL_ACTION);
                        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                        sendBroadcast(intent);
                    }
                }
            }
        });
        tvCurrentPos = (TextView) findViewById(R.id.tv_player_currentPosition);
        tvLengthPos = (TextView) findViewById(R.id.tv_player_duration);
        tvTitle = (TextView) findViewById(R.id.tv_player_title);

        tvAddDevice = (TextView) findViewById(R.id.player_image);
        tvAddDevice.setBackgroundDrawable(new CustomDrawable());
        tvAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevices = DLNAContainer.getInstance().getDevices();
                postdevies = Global.mMusicSaveDao.getPostDevice();
                if (mDevices.size() == 0) {
                    playLocal();
                    Toast.makeText(AudioActivity_Player.this, getString(R.string.no_device), Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String[] strs = new String[1 + mDevices.size()];
                    strs[0] = getResources().getString(R.string.mobile);
                    for (int i = 1; i <= mDevices.size(); i++) {
                        strs[i] = mDevices.get(i - 1).getFriendlyName() + "";
                    }

                    new AlertDialog.Builder(AudioActivity_Player.this)
                            .setTitle(getString(R.string.select_device))
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(strs, postdevies,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (postdevies != which) {
                                                updateInfoUI();
                                                postdevies = which;
                                                Global.mMusicSaveDao.setPostDevice(postdevies);
                                                if (which == 0) {
                                                    playLocal();
                                                    Toast.makeText(AudioActivity_Player.this, getString(R.string.connect_mobile), Toast.LENGTH_SHORT).show();
                                                    changeLocal(Selected);
                                                } else {
//                                                    Global.mMusicSaveDao.dao.setHandler(mHandler);
                                                    PlayerService.stop();
                                                    ivPlayerCtl.setBackgroundResource(R.drawable.play);
                                                    isMyPlay = false;
                                                    if (homeReceiver != null) {
                                                        unregisterReceiver(homeReceiver);
                                                        homeReceiver = null;
                                                    }
                                                    Global.mMusicSaveDao.setDevice(mDevices.get(which - 1));
                                                    DLNAContainer.getInstance().setSelectedDevice(mDevices.get(which - 1));
                                                    isdevies = true;
                                                    try {
                                                        if (musicList.size() > 0) {
                                                            String path = musicList.get(0).getSDsrc();
                                                            String dir = path.substring(0, path.lastIndexOf("/") + 1);
                                                            File wwwroot = new File(dir);
                                                            nanoHTTPD = new NanoHTTPD(8089, wwwroot);
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(AudioActivity_Player.this, getString(R.string.connect) +
                                                            mDevices.get(which - 1).getFriendlyName() + getString(R.string.success), Toast.LENGTH_SHORT).show();
                                                    if (isFirstLaunch) {
                                                        //changeDevice(Selected);
                                                        changeDevice(Selected);
                                                    }
                                                    isFirstLaunch = true;
                                                }
                                            }
                                            dialog.dismiss();

                                        }
                                    }).show();

                }
            }
        });
    }

    private void showLoadingDialog(){
        dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.loading));
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (homeReceiver != null) {
            unregisterReceiver(homeReceiver);
            homeReceiver = null;
        }
        stopService(new Intent(this, PlayerService.class));
        if (loadMusicThread.isAlive()){
            loadMusicThread.interrupt();
            loadMusicThread = null;
        }
        EventBus.getDefault().unregister(this);
        ivPlayerCtl = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isdevies){
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (isdevies) {
                        mVolume = mVolume - 5;
                        if (mVolume <= 0)
                            mVolume = 0;
                        Global.mMusicSaveDao.dao.setVoice(mVolume);
                        Global.mMusicSaveDao.setVolume(mVolume);
                        return true;
                    }

                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (isdevies) {
                        mVolume = mVolume + 5;
                        if (mVolume >= 100)
                            mVolume = 100;
                        Global.mMusicSaveDao.dao.setVoice(mVolume);
                        Global.mMusicSaveDao.setVolume(mVolume);
                        return true;
                    }
            }
            return super.onKeyDown(keyCode, event);
        }else
            return super.onKeyDown(keyCode, event);
    }


    private void updateInfoUI(){
        tvTitle.setText("");
        tvCurrentPos.setText("00:00");
        tvLengthPos.setText("00:00");
        seekBar.setProgress(0);
        bitmap=null;
        ivPlayerCtl.setBackgroundResource(R.drawable.play);
    }

    private void startDLNAService() {
        DLNAContainer.getInstance().clear();
        Intent intent = new Intent(this, DLNAService.class);
        startService(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Selected = position;
        if (musicList.size() > 0) {
            if (isdevies) {
                changeDevice(position);
            } else {
                changeLocal(position);
            }
        }
    }

    private void changeDevice(int position){
        if (Deiviceutis.mPlaying){
            Global.mMusicSaveDao.dao.stop();
        }
        String p = musicList.get(position).getSDsrc();
        String s = p.substring(p.lastIndexOf("/") + 1);
        path = "http://" + Utils.intToIp2(Global.myIP) + ":8089/" + s;
        info = musicList.get(position);
        Global.mMusicSaveDao.dao.play(path, info);
//        Global.mMusicSaveDao.dao.play(path, info);
    }

    private void changeLocal(int position){
        if (Deiviceutis.mPlaying){
            Global.mMusicSaveDao.dao.stop();
        }
        Intent intent = new Intent();
        intent.setAction(PlayerService.PLAY_SDMUSIC);
        intent.putExtra("current", position);
        intent.putExtra("musicinfo", musicList);
        sendBroadcast(intent);
    }

    private void playLocal(){
        Global.mMusicSaveDao.dao.stop();
        Global.mMusicSaveDao.dao.getleng = 0;
        Global.mMusicSaveDao.dao.info = null;
        Global.mMusicSaveDao.dao.setHandler(new Handler());
        Global.mMusicSaveDao.dao.setPositioThre(false);
        setReceiver();
        isdevies = false;
        MusicName = "";
        if (nanoHTTPD != null) {
            nanoHTTPD.stop();
        }
    }

    private void loadEnd() {
        MusicAdapter adapter = new MusicAdapter();
        lvMusicList.setAdapter(adapter);
        if (dialogFragment != null){
            dialogFragment.dismiss();
            dialogFragment = null;
        }

    }

    // 自定义的BroadcastReceiver，负责监听从Service传回来的广播
    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PlayerService.MUSIC_CURRENT)) {
                currentTime = intent.getIntExtra("currentTime", 0);
                maxTime = intent.getIntExtra("maxTime", 0);
                MusicInfo info = (MusicInfo) intent.getSerializableExtra("info");

                if (!MusicName.equals(info.getTitle())) {
                    MusicName = info.getTitle();
                    if (MusicName.contains("]"))
                        MusicName = MusicName.substring(0, MusicName.lastIndexOf("["));
                    tvTitle.setText(MusicName);
                    tvLengthPos.setText(Utils.getTimeFromInt(maxTime) + "");
                    seekBar.setMax((int) maxTime);
                    seekBar.setEnabled(true);
                }
                if(isMyPlay){
                    tvCurrentPos.setText(Utils.getTimeFromInt(currentTime) + "");
                    seekBar.setProgress(currentTime);
                }
            }else if (action.equals(PlayerService.UPDATE_CTL)){
                boolean isPlay = intent.getBooleanExtra("isPlay", false);
                ivPlayerCtl.setBackgroundResource(isPlay ? R.drawable.stop : R.drawable.play);
                isMyPlay = isPlay;
            }
        }

    }

    private void setReceiver() {
        IntentFilter filter = new IntentFilter();
        homeReceiver = new HomeReceiver();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(PlayerService.UPDATE_ACTION);
        filter.addAction(PlayerService.MUSIC_CURRENT);
        filter.addAction(PlayerService.MUSIC_DURATION);
        filter.addAction(PlayerService.PLAY_SDMUSIC);
        filter.addAction(PlayerService.UPDATE_CTL);
        registerReceiver(homeReceiver, filter);
    }

    static class LoadMusicThread extends Thread {
        @Override
        public void run() {
            MusicLoader loader = MusicLoader.instance(App.getApp().getContentResolver());
            musicList = loader.getMusicList();
            EventBus.getDefault().post(new Event.GetMusicListEvent(musicList));
        }
    }

    public void onEventMainThread(Event.GetMusicListEvent event){
        LogUtils.i("TAG", "onEventMainThread" + " is on going");
        loadEnd();
    }

    public void onEventMainThread(Event.PostDeviceEvent event){
        Global.mMusicSaveDao.setPostDevice(0);
    }

    class MusicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public Object getItem(int position) {
            return musicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(AudioActivity_Player.this).inflate(R.layout.music_item, null);
                ImageView pImageView = (ImageView) convertView.findViewById(R.id.albumPhoto);
                TextView pTitle = (TextView) convertView.findViewById(R.id.title);
                TextView pDuration = (TextView) convertView.findViewById(R.id.duration);
                //TextView pArtist = (TextView) convertView.findViewById(R.id.artist);
                viewHolder = new ViewHolder(pImageView, pTitle, pDuration);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.imageView.setImageResource(R.drawable.audio);
            String title = musicList.get(position).getTitle();
            if (title.contains("]"))
                title = title.substring(0, title.lastIndexOf("["));
            viewHolder.title.setText(title);
            viewHolder.duration.setText(Utils.getTimeFromInt(musicList.get(position).getPlaytime()));
            LogUtils.i("mHandler", "duration :" + Utils.getTimeFromInt(musicList.get(position).getPlaytime()));
            return convertView;
        }
    }

    class ViewHolder{
        public ViewHolder(ImageView pImageView, TextView pTitle, TextView pDuration){
            imageView = pImageView;
            title = pTitle;
            duration = pDuration;
        }

        ImageView imageView;
        TextView title;
        TextView duration;
    }

}
