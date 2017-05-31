package com.yankon.smart.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class NetworkReceiverService extends Service {

    public static final String ACTION_REBIND_WIFI = "action_rebind_wifi";

    public NetworkReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    UdpHelper udpHelper;
    Thread mWorkThread;
    WifiManager mWifiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUdpReceiver();
    }

    void stopUdpReceiver() {
        try {
            if (udpHelper != null) {
                udpHelper.stop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (mWorkThread != null) {
                mWorkThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null && ACTION_REBIND_WIFI.equals(intent.getAction())) {
            stopUdpReceiver();
            udpHelper = new UdpHelper(this, mWifiManager);
            mWorkThread = new Thread(udpHelper);
            mWorkThread.start();
        }
        return START_STICKY;
    }
}
