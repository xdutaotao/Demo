package com.yankon.smart.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.SWMulticastTask;
import com.yankon.smart.utils.SharedPreferencesUtils;
import com.winnermicro.smartconfig.ConfigType;
import com.winnermicro.smartconfig.ISmartConfig;
import com.winnermicro.smartconfig.SmartConfigFactory;
import com.yankon.smart.utils.Utils;

import java.util.zip.CRC32;


public class NetworkBuildActivity extends BaseActivity implements View.OnClickListener {
    private Context mycontext;
    private Button BtnStartStop;
    private TextView EditSSID;
    private EditText EditPass;
    private CheckBox CB_rp;
    private String passWord;
    private String psw;
    WifiManager wifiMgr;
    WifiInfo wifiInf;
//    SmartConfigFactory factory;
    private PowerManager.WakeLock mWakeLock;
//    private ISmartConfig smartConfig = null;
    private int mDownX, mDownY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mycontext = this;
        setContentView(R.layout.activity_network_build);
        initLayout();
        //initAcitivityUI();
        initScreenClock();
//        factory = new SmartConfigFactory();

    }

    private void initLayout() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        View backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.action_addlights_network));

        BtnStartStop = (Button) findViewById(R.id.btn_build);
        BtnStartStop.setOnClickListener(this);
        EditSSID = (TextView) findViewById(R.id.tvssid);
        EditPass = (EditText) findViewById(R.id.etpassword);
        EditPass.addTextChangedListener(watcher);
        CB_rp = (CheckBox) findViewById(R.id.cb_rumber);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                LogUtils.i("MOVE", mDownX + "----" + event.getX() + "MOVE---------" + mDownY + "----" + event.getY());
                if (event.getX() - mDownX > Global.X_DISTANCE && Math.abs(event.getY() - mDownY) < Global.Y_DISTANCE)
                    finish();
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (smartConfig == null)
//            smartConfig = factory.createSmartConfig(ConfigType.UDP,mycontext);

        BtnStartStop.setEnabled(false);
        BtnStartStop.setOnClickListener(this);
        BtnStartStop.setEnabled(true);
        checkWiFi();
        passWord = Utils.getPass(mycontext, EditSSID.getText().toString());
        if (passWord != null) {
            EditPass.setText(passWord);
        } else {
            EditPass.setText(null);
        }

        if (mWakeLock != null)
            mWakeLock.acquire();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            // Log.i("TAG",EditPass.getText().toString());
            passWord = EditPass.getText().toString();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_build:
                buttonCB();
                break;

            case R.id.back:
            case R.id.back_layout:
                finish();
                break;

            default:
                break;
        }

    }

    private void buttonCB() {///111
        if (checkWiFi() < 0 || EditSSID.getText().toString() == null
                || EditSSID.getText().toString().isEmpty()) {
            Toast.makeText(mycontext, R.string.select_wifi, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // TODO Auto-generated method stub
        String pass = EditPass.getText().toString();
        psw = pass;

        if (CB_rp.isChecked()) {
            Utils.savePass(mycontext, EditSSID.getText().toString(), pass);
        } else {
            Utils.savePass(mycontext, EditSSID.getText().toString(), null);
        }
        if (pass.length() == 0) {
            pass = "12345678";
        }

        if (!validate(pass, null))
            return;

        Intent intent = new Intent(this, AddLightsActivity.class);
        intent.putExtra(AddLightsActivity.SSID, EditSSID.getText().toString());
        intent.putExtra(AddLightsActivity.PWD, EditPass.getText().toString());
        startActivity(intent);
        finish();

    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EditPass.getWindowToken(), 0);
    }


    /* 保持手机屏幕一直高亮 */
    private void initScreenClock() {
        // TODO Auto-generated method stub
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    }
    private int checkWiFi() {
        wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInf = wifiMgr.getConnectionInfo();
        LogUtils.d("jack", "In checkWiFi");
        if (wifiMgr.isWifiEnabled() == false) {
            //TxtDebug.setText("WiFi not enabled. Please enable WiFi and connect to the home network.");
            return -1;
        } else if (wifiMgr.getConnectionInfo().getSSID().length() == 0) {
            //TxtDebug.setText("WiFi is enabled but device is not connected to any network.");
            return -1;
        }
        EditSSID.setText(wifiMgr.getConnectionInfo().getSSID() + "_"
                + wifiMgr.getConnectionInfo().getBSSID());
        return 0;
    }


    private boolean validate(String pass, String key) {
        if (key == null || key.length() == 0) {
            return true;
        }
        if (pass.length() < 8 || pass.length() > 63) {
            //TxtDebug.setText("Invalid passphrase. Passphrase must be 8 to 63 characters long.");
            return false;
        }
        if (key.length() != 16 && key.length() != 0) {
            //TxtDebug.setText("Invalid key. Key must be empty or 16 characters.");
            return false;
        }
        return true;
    }

}
