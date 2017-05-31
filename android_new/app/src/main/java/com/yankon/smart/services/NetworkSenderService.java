package com.yankon.smart.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.Network;

/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class NetworkSenderService extends IntentService {
    private static final String ACTION_SEND_CMD = "com.yankon.smart.services.action.send_cmd";
    private static final String ARG_CMD = "cmd";
    private static final String ARG_IP = "ip";

    public static void sendCmd(Context context, int ip, byte[] cmd) {
        Intent intent = new Intent(context, NetworkSenderService.class);
        intent.setAction(ACTION_SEND_CMD);
        intent.putExtra(ARG_CMD, cmd);
        intent.putExtra(ARG_IP, ip);
        context.startService(intent);
    }

    public NetworkSenderService() {
        super("NetworkSenderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_CMD.equals(action)) {
                final byte[] cmd = intent.getByteArrayExtra(ARG_CMD);
                final int ip = intent.getIntExtra(ARG_IP, 0);
                Network.sendCMD(ip, cmd);
            }
        }
    }
}
