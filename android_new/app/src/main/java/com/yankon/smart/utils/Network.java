package com.yankon.smart.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.yankon.smart.App;
import com.yankon.smart.SWDefineConst;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by Evan on 14/12/16.
 */
public class Network {
    private static final String LOG_TAG = "YanKon_Network";

    private static final int MAX_DATA_PACKET_LENGTH = 1024;

//    static DatagramSocket udpSocket = null;

    public static void sendCMD(int ip, byte[] cmd) {
//        if (ips == null || ips.length == 0) {
//            ips = new int[]{Global.BROADCAST_IP};
//        }
        LogUtils.i("_SCAN_LIGHTS-jack","start send msg, ip = "+ ip + " cmd = "+ cmd.toString());
        if (Global.myIPBytes == null)
            return;
        if (cmd[3] == 2) {
            for (int i = 0; i < 4; i++) {
                cmd[4 + i] = Global.myIPBytes[i];
            }
        }

//        byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
        /* 是否是直接发送给同一个局域网的 */
        boolean directSending = cmd[3] == 2 && cmd[8] == 0 && cmd[9] == 0 && cmd[10] == 0 && cmd[11] == 0;

        try {
            if (SWDefineConst.udpSocket == null) {
                SWDefineConst.udpSocket = new DatagramSocket(null);
                SWDefineConst.udpSocket.setReuseAddress(true);      /* That could be needed for receiving multicast packets */
                SWDefineConst.udpSocket.bind(new InetSocketAddress(Constants.DEFAULT_PORT));
                SWDefineConst.udpSocket.setBroadcast(true);
            }
            String ipStr = Utils.intToIp(ip);
            InetAddress broadcastAddr = InetAddress.getByName(ipStr);
            if (directSending) {
                byte[] ipaddr = broadcastAddr.getAddress();
                for (int i = 0; i < 4; i++) {
                    cmd[8 + i] = ipaddr[i];
                }
            }
            DatagramPacket dataPacket = new DatagramPacket(cmd, cmd.length, broadcastAddr, Constants.DEFAULT_PORT);
            if (App.isWifiConnected())
                SWDefineConst.udpSocket.send(dataPacket);

            LogUtils.e(LOG_TAG, "Send out cmd:" + ipStr + " data:" + Utils.byteArrayToString(cmd));
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
            try {
                SWDefineConst.udpSocket.close();
            } catch (Exception ex) {
                LogUtils.e(LOG_TAG, Log.getStackTraceString(e));
            } finally {
                SWDefineConst.udpSocket = null;
            }
        }
    }

    public static boolean getLocalIP(Context context) {
        Global.isWifiConnected = false;
        Global.myIP = 0;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Global.myIPBytes = null;
            return false;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            Global.myIPBytes = null;
            return false;
        }
        int ipAddress = wifiInfo.getIpAddress();
        Global.myIP = ipAddress;
        Global.myIPBytes = new byte[4];
        Global.myIPBytes[0] = (byte) (ipAddress & 0xFF);
        Global.myIPBytes[1] = (byte) ((ipAddress >> 8) & 0xFF);
        Global.myIPBytes[2] = (byte) ((ipAddress >> 16) & 0xFF);
        Global.myIPBytes[3] = (byte) (ipAddress >> 24 & 0xFF);
        Global.isWifiConnected = true;
        return true;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    private static String broadcastIP(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + ".255";
    }

}
