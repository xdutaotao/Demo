package com.yankon.smart.activities;

import android.content.Context;
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

import java.util.zip.CRC32;


public class NetworkBuildActivity extends BaseActivity implements View.OnClickListener {
    private Context mycontext;
    private Button BtnStartStop;
    private TextView EditSSID;
    private EditText EditPass;
    private CheckBox CB_rp;
    private TextView TxtDebug;
    private int count;
    private String passWord;
    private String psw;
    WifiManager wifiMgr;
    WifiInfo wifiInf;
    Boolean xmitStarted = false;
    SWMulticastTask xmitter;
    SmartConfigFactory factory;
    private PowerManager.WakeLock mWakeLock;
    private ISmartConfig smartConfig = null;
    private static final String SSID_PWD = "SSID_PWD_";
    private ImageView ImScan;
    private ImageView ImDian;
    private RotateAnimation animation;
    private RotateAnimation animation2;
    private int mDownX, mDownY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mycontext = this;
        setContentView(R.layout.activity_network_build);
        initLayout();
        //initAcitivityUI();
        initScreenClock();
        factory = new SmartConfigFactory();

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
        ImScan = (ImageView) findViewById(R.id.im_scan);
        ImScan.setOnClickListener(this);
        ImDian = (ImageView) findViewById(R.id.im_dian);
        ImDian.setOnClickListener(this);
        EditPass.addTextChangedListener(watcher);
        CB_rp = (CheckBox) findViewById(R.id.cb_rumber);
        TxtDebug = (TextView) findViewById(R.id.tvdebug);
        initAnim();
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
        if (smartConfig == null)
            smartConfig = factory.createSmartConfig(ConfigType.UDP,mycontext);

        BtnStartStop.setEnabled(false);
        BtnStartStop.setOnClickListener(this);
        TxtDebug.setText("0");
        BtnStartStop.setEnabled(true);
        checkWiFi();
        passWord = getPass(EditSSID.getText().toString());
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
        // TODO Auto-generated method stub

        BtnStartStop.setText(R.string.str_start);
        if (xmitter != null && !xmitter.isCancelled()) {
            xmitter.cancel(true);
        }
        stopConfig();
        xmitStarted = false;
        stopAnim();
        passWord = null;
        if (mWakeLock != null)
            mWakeLock.release();
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
            savePass(EditSSID.getText().toString(), pass);
        } else {
            savePass(EditSSID.getText().toString(), null);
        }
        if (pass.length() == 0) {
            pass = "12345678";
        }
        try {
            if (xmitStarted == false) {
                if (!validate(pass, null))
                    return;
                xmitter = new SWMulticastTask();
                xmitter.initBase(mycontext);
                xmitter.handler = handler;
                xmitStarted = true;
                BtnStartStop.setText(R.string.str_stop);
                // set crc32
                CRC32 crc32 = new CRC32();
                crc32.reset();
                crc32.update(pass.getBytes());
                xmitter.passCRC = (int) crc32.getValue() & 0xffffffff;
                // xmitter.passCRC = (int) SUcrc32(0, pass);
                LogUtils.d("jack<-crc32", Integer.toHexString(xmitter.passCRC));
                LogUtils.d("jack<-crc32", "2_" + xmitter.passCRC);

                // set pass & passlen
                xmitter.passphrase = pass.getBytes();
                xmitter.passLen = pass.length();
                // set mac
                xmitter.mac = new char[6];
                String[] macParts = wifiInf.getBSSID().split(":");
                LogUtils.d("jack", wifiInf.getBSSID());
                for (int i = 0; i < 6; i++)
                    xmitter.mac[i] = (char) Integer.parseInt(macParts[i], 16);
                xmitter.resetStateMachine();
                xmitter.execute("");

                startAnim();
                hideSoftInput();
                new Thread(new ProbeReqThread()).start();
            } else {
                xmitStarted = false;
                BtnStartStop.setText(R.string.str_start);
                xmitter.cancel(true);
                stopConfig();
                stopAnim();
            }
        } catch (Error err) {
            LogUtils.e("jack", err.toString());
        }

    }
    private void stopConfig() {
        // TODO Auto-generated method stub
        handler.post(confPost);
    }
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EditPass.getWindowToken(), 0);
    }
    private void savePass(String SSID, String pass) {
        // TODO Auto-generated method stub
        String key = SSID_PWD + SSID;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(mycontext);
        saveutils.putString(key, pass);
    }
    private String getPass(String SSID) {
        String key = SSID_PWD + SSID;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(mycontext);
        String pass = saveutils.getString(key);
        return pass;
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

    class ProbeReqThread implements Runnable {
        public void run() {
            WifiManager wifiManager = null;

            try {
                wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    count = 0;
                    while (xmitStarted) {
                        if (SWDefineConst.IS_SUPPORT_HUADA) {
                            smartConfig.startConfig(psw);//lianshengde÷
                        }
                        count++;
                        // adapter.notifyDataSetChanged();
                        Thread.sleep(10);
                        if (count > SWDefineConst.MulticastCount) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(mContext, R.string.str_configerror, Toast.LENGTH_SHORT).show();
            } finally {

                handler.post(confPost);
                if (count >= SWDefineConst.MulticastCount) {
                    Message msg = handler.obtainMessage();
                    msg.what = 42;
                    handler.sendMessage(msg);
                }
            }
        }

    }

    private Runnable confPost = new Runnable() {

        @Override
        public void run() {
            xmitStarted = false;
            BtnStartStop.setText(R.string.str_start);
            //TxtDebug.setText("Please check indicators on the device.\n The device should have been provisioned.\n If not, please retry.");
            stopAnim();
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            if (wifiManager != null) {
                try {
                    smartConfig.stopConfig();//lianshengde
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }

    };
    private void GotoMainActivityFromMulticastActivity(){

    }
    private void initAnim() {
        // TODO Auto-generated method stub
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        animation2 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(2000);
        animation2.setRepeatCount(Animation.INFINITE);

    }
    private void stopAnim(){
        ImScan.clearAnimation();
        ImDian.clearAnimation();
    }
    private void startAnim(){
        ImScan.startAnimation(animation);
        ImDian.startAnimation(animation2);
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
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 42) {
                xmitStarted = false;
                BtnStartStop.setText(R.string.str_start);
                //TxtDebug.setText("Please check indicators on the device.\n The device should have been provisioned.\n If not, please retry.");
                stopAnim();
                GotoMainActivityFromMulticastActivity();
            } else if (msg.what == 43) {
                if (!xmitStarted && xmitter != null && !xmitter.isCancelled()) {
                    xmitter.cancel(true);
                    return;
                }
                TxtDebug.setText("" + msg.arg1);
            }
            super.handleMessage(msg);
        }
    };
}
