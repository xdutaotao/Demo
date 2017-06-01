package com.yankon.smart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.yankon.smart.music.dlna.DLNAContainer;
import com.yankon.smart.music.dlna.DLNAService;
import com.yankon.smart.services.NetworkReceiverService;
import com.yankon.smart.utils.Global;

/**
 * Created by Evan on 15/3/11.
 */
public class WifiStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Global.isWifiConnected = false;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_LIGHTS);
    }

}
