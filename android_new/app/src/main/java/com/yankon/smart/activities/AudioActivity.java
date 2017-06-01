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
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jack jiang on 2016/2/22.
 */
public class AudioActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_kgplayer;
    private Button btn_qqplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initActivityUI();
    }
    @Override
    public void initActivityUI() {
        super.initActivityUI();
        setTitle(getString(R.string.audio));
        btn_kgplayer = (Button)findViewById(R.id.btn_kgplayer);
        btn_qqplayer = (Button)findViewById(R.id.btn_qqplayer);
        btn_kgplayer.setOnClickListener(this);
        btn_qqplayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_kgplayer:
                if(Utils.openPackage(this, "com.kugou.android")){
                    finish();
                }else{
                    Toast.makeText(this, R.string.toast_install_kgplayer , Toast.LENGTH_SHORT).show();
                }
                finish();
                break;

            case R.id.btn_qqplayer:
                if(Utils.openPackage(this, "com.tencent.qqmusic")){
                    finish();
                }else{
                    Toast.makeText(this, R.string.toast_install_qqplayer, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
