package com.yankon.smart.model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.winnermicro.smartconfig.ConfigType;
import com.winnermicro.smartconfig.ISmartConfig;
import com.winnermicro.smartconfig.SmartConfigFactory;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.SWMulticastTask;

import java.util.zip.CRC32;

/**
 * Created by guzhenfu on 2015/10/28.
 */
public class BuildNetworkThread extends Thread {
    private Context mContext;
    private String mPass;
    private Handler handler;

    private WifiManager wifiMgr;
    private WifiInfo wifiInf;
    private static Boolean xmitStarted = false;
    private SWMulticastTask xmitter;
    private ISmartConfig smartConfig = null;
    private SmartConfigFactory factory;
    private PowerManager.WakeLock mWakeLock;
    private int count;

    public BuildNetworkThread(Context context, String pass){
        mContext = context;
        mPass = pass;
        this.handler = new Handler();

        initScreenClock();
        factory = new SmartConfigFactory();
        if (smartConfig == null)
            smartConfig = factory.createSmartConfig(ConfigType.UDP, context);
        checkWiFi();
        if (mWakeLock != null)
            mWakeLock.acquire();
    }

    @Override
    public void run() {
        super.run();
        try {
            String Pass_tmp = mPass;
            if (Pass_tmp.length() == 0) {
                Pass_tmp = "12345678";
            }
            if (xmitStarted == false) {
                xmitter = new SWMulticastTask();
                xmitter.initBase(mContext);
                xmitter.handler = handler;
                xmitStarted = true;
                // set crc32
                CRC32 crc32 = new CRC32();
                crc32.reset();
                crc32.update(Pass_tmp.getBytes());
                xmitter.passCRC = (int) crc32.getValue() & 0xffffffff;
                // xmitter.passCRC = (int) SUcrc32(0, pass);
                LogUtils.d("jack<-crc32", Integer.toHexString(xmitter.passCRC));
                LogUtils.d("jack<-crc32", "2_" + xmitter.passCRC);

                // set pass & passlen
                xmitter.passphrase = Pass_tmp.getBytes();
                xmitter.passLen = Pass_tmp.length();
                // set mac
                xmitter.mac = new char[6];
                String[] macParts = wifiInf.getBSSID().split(":");
                LogUtils.d("jack", wifiInf.getBSSID());
                for (int i = 0; i < 6; i++)
                    xmitter.mac[i] = (char) Integer.parseInt(macParts[i], 16);
                xmitter.resetStateMachine();
                xmitter.execute("");

                new Thread(new ProbeReqThread()).start();
            } else {
                xmitStarted = false;
                xmitter.cancel(true);
                handler.post(confPost);
            }
        } catch (Error err) {
            LogUtils.e("jack", err.toString());
        }
    }

    @Override
    public void interrupt() {
        LogUtils.i("sunup-BuildNetWorkThread","STOP");
        xmitStarted = false;
        stopConfig();
        xmitter.cancel(true);
        if(mWakeLock != null) {
            mWakeLock.release();
        }
        super.interrupt();
    }
    private void stopConfig() {
        // TODO Auto-generated method stub
        handler.post(confPost);
    }
    /* 保持手机屏幕一直高亮 */
    private void initScreenClock() {
        // TODO Auto-generated method stub
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    }

    class ProbeReqThread implements Runnable {
        public void run() {
            WifiManager wifiManager = null;

            try {
                wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    count = 0;
                    while (xmitStarted) {
                        if (SWDefineConst.IS_SUPPORT_HUADA) {
                            smartConfig.startConfig(mPass);//lianshengde÷
                        }
                        count++;
                        LogUtils.i("sunup-confPost", ""+count);
                                // adapter.notifyDataSetChanged();
                                Thread.sleep(10);
//                        if (count > SWDefineConst.MulticastCount) {
//                            break;
//                        }
                    }
                    LogUtils.i("sunup-confPost","while end");
                }
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(mContext, R.string.str_configerror, Toast.LENGTH_SHORT).show();
            } finally {
                LogUtils.i("sunup-confPost","stop ProbeReqThread");
                handler.post(confPost);
//                if (count >= SWDefineConst.MulticastCount) {
//                    xmitStarted = false;
//                    Message msg = handler.obtainMessage();
//                    msg.what = 42;
//                    handler.sendMessage(msg);
//                }
            }
        }

    }

    private Runnable confPost = new Runnable() {

        @Override
        public void run() {
            xmitStarted = false;
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                try {
                    LogUtils.i("sunup-confPost","stop confPost");
                    smartConfig.stopConfig();//lianshengde
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }

    };


    private int checkWiFi() {
        wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiInf = wifiMgr.getConnectionInfo();
        LogUtils.d("jack", "In checkWiFi");
        if (wifiMgr.isWifiEnabled() == false) {
            //TxtDebug.setText("WiFi not enabled. Please enable WiFi and connect to the home network.");
            return -1;
        } else if (wifiMgr.getConnectionInfo().getSSID().length() == 0) {
            //TxtDebug.setText("WiFi is enabled but device is not connected to any network.");
            return -1;
        }
        return 0;
    }

}
