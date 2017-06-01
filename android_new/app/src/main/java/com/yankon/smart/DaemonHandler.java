package com.yankon.smart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yankon.smart.model.TransData;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.services.NetworkReceiverService;
import com.yankon.smart.services.NetworkSenderService;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Network;
import com.yankon.smart.utils.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by Evan on 15/3/10.
 */
public class DaemonHandler extends Handler {
    public static final int MSG_CHECK_TRANS = 0;
    public static final int MSG_CHECK_TRANS_FIRE = 1;
    public static final int MSG_SCAN_LIGHTS = 2;
    public static final int MSG_CHECK_ACTIVE = 3;
    public static final int MSG_QUICK_SEARCH = 4;

    public static final int MSG_SET_SCAN_TYPE = 10001;
    static final String LOG_TAG = DaemonHandler.class.getSimpleName();

    WeakReference<Context> contextWeakReference;

    public DaemonHandler(Context context) {
        super();
        contextWeakReference = new WeakReference<>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (contextWeakReference == null) {
            return;
        }
        Context context = contextWeakReference.get();
        if (context == null)
            return;
        switch (msg.what) {
            case MSG_CHECK_TRANS:
                sendEmptyMessageDelayed(MSG_CHECK_TRANS_FIRE, Constants.TRANS_CHECK_PERIOD);
                break;

            case MSG_CHECK_TRANS_FIRE:
                checkTransactions(context);
                removeMessages(MSG_CHECK_TRANS_FIRE);
                if(Global.gCommandTransactions.size()>0)
                    sendEmptyMessage(MSG_CHECK_TRANS);
                break;

            //发送搜索消息开始进行搜灯操作
            case MSG_SCAN_LIGHTS:
                LogUtils.i("TAG", Global.gScanLightsType + "---------------------" + Global.mQuickTime + "--------------");
                removeMessages(MSG_SCAN_LIGHTS);
                LogUtils.i("MSG_SCAN_LIGHTS-jack","start search");
                scanLights(context);
                sendEmptyMessageDelayed(MSG_CHECK_ACTIVE, Constants.SCAN_ACTIVE_CHECK_PERIOD);
                sendEmptyMessageDelayed(MSG_SCAN_LIGHTS, Constants.SCAN_PERIOD[Global.gScanLightsType]);
                break;
            case MSG_SET_SCAN_TYPE:
                Global.gScanLightsType = Constants.SCAN_LIGHTS_NORMAL;
                sendEmptyMessage(MSG_CHECK_ACTIVE);
                sendEmptyMessage(MSG_SCAN_LIGHTS);
                LogUtils.i("MSG_SET_SCAN_TYPE-jack", "set type");
                break;
            case MSG_CHECK_ACTIVE:
                checkLightsActive(context);
                break;

            case MSG_QUICK_SEARCH:
                Global.gScanLightsType = Constants.SCAN_LIGHTS_QUICK;
                Global.mQuickTime++;
                if (Global.mQuickTime >= 3){
                    Global.mQuickTime = 0;
                    Global.gScanLightsType = Constants.SCAN_LIGHTS_NORMAL;
                    sendEmptyMessage(MSG_SCAN_LIGHTS);
                    break;
                }
                LogUtils.i("TAG", Global.gScanLightsType + "---------------------" + Global.mQuickTime + "--------------");

                removeMessages(MSG_QUICK_SEARCH);
                scanLights(context);
                sendEmptyMessageDelayed(MSG_CHECK_ACTIVE, Constants.SCAN_ACTIVE_CHECK_PERIOD);
                sendEmptyMessageDelayed(MSG_QUICK_SEARCH, Constants.SCAN_PERIOD[Global.gScanLightsType]);
                break;
        }
    }

    private void checkSwitchsActive(Context context) {
        ContentValues values = new ContentValues();
        values.put("connected", false);
        values.put("IP", 0);
        values.put("subIP", 0);
        long time = System.currentTimeMillis() - Constants.SCAN_ACTIVE_CHECK_PERIOD * 12;
        context.getContentResolver().update(YanKonProvider.URI_SWITCHS, values, "last_active<" + time, null);
    }

    void checkTransactions(Context context) {
        for (TransData data : Global.gCommandTransactions.values()) {
            if (data.time + Constants.TRANS_CHECK_PERIOD < System.currentTimeMillis()) {
                if (data.retryTimes < 5) {
                    NetworkSenderService.sendCmd(context, data.IP, data.data);
                    data.time = System.currentTimeMillis();
                    data.retryTimes++;
                    LogUtils.i("send msg re", "MSG_CHECK_TRANS retry" + data.retryTimes);
                }else {
                    Global.gCommandTransactions.remove(data.key);
                }
            }
        }
    }

    void scanLights(Context context) {
        boolean needRebindWifi = false;
        if (!Global.isWifiConnected) {
            needRebindWifi = true;
            int oldIP = Global.myIP;
            Network.getLocalIP(context);
            if (Global.myIP == 0 || oldIP != Global.myIP) {
                Utils.setAllLightsOffline(context);
            }
        }
        if (Global.isWifiConnected) {
            if (needRebindWifi) {
                Intent intent = new Intent(context, NetworkReceiverService.class);
                intent.setAction(NetworkReceiverService.ACTION_REBIND_WIFI);
                context.startService(intent);
            }
            /* 发送广播 搜索附近的灯 */
            NetworkSenderService.sendCmd(context, Constants.BROADCAST_IP, Constants.SEARCH_LIGHTS_CMD);
        }
    }

    void scanSwitchs(Context context) {
        boolean needRebindWifi = false;
        if (!Global.isWifiConnected) {
            needRebindWifi = true;
            int oldIP = Global.myIP;
            Network.getLocalIP(context);
            if (Global.myIP == 0 || oldIP != Global.myIP) {
                Utils.setAllSwitchsOffline(context);
            }
        }
        if (Global.isWifiConnected) {
            if (needRebindWifi) {
                Intent intent = new Intent(context, NetworkReceiverService.class);
                intent.setAction(NetworkReceiverService.ACTION_REBIND_WIFI);
                context.startService(intent);
            }
            /* 发送广播 搜索附近的开关 */
            //NetworkSenderService.sendCmd(context, Constants.BROADCAST_IP, Constants.SEARCH_SWITCHS_CMD);
        }
    }

    void checkLightsActive(Context context) {
        ContentValues values = new ContentValues();
        values.put("connected", false);
        values.put("IP", 0);
        values.put("subIP", 0);
        long time = System.currentTimeMillis() - Constants.SCAN_ACTIVE_CHECK_PERIOD * 12;
        context.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "last_active<" + time, null);
    }
}
