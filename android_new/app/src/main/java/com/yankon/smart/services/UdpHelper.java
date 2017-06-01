package com.yankon.smart.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.yankon.smart.App;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.model.Command;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.Switchs;
import com.yankon.smart.model.TransData;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Evan on 14/12/17.
 * 创建一个接收socket。一直接受数据
 */
public class UdpHelper implements Runnable {
    private static final String LOG_TAG = "YanKon_Network";
    public Boolean IsThreadDisable = false;
    private static WifiManager.MulticastLock lock;      /* 实例化一个Lock，解决有些手机可能接受不到UDP包的问题 */
    //    DatagramSocket datagramSocket;
    Context mContext = null;

    public UdpHelper(Context context, WifiManager manager) {
        super();
        mContext = context;
        this.lock = manager.createMulticastLock("UDPwifi");
    }

    public void StartListen() {
        byte[] message = new byte[1024];
        while (!IsThreadDisable) {
            LogUtils.e(LOG_TAG,"start listen");
            try {
                if (SWDefineConst.udpSocket == null) {
                    SWDefineConst.udpSocket = new DatagramSocket(null);
                    SWDefineConst.udpSocket.setReuseAddress(true);
                    SWDefineConst.udpSocket.bind(new InetSocketAddress(Constants.DEFAULT_PORT));
                    SWDefineConst.udpSocket.setBroadcast(true);
                }
                LogUtils.i(LOG_TAG, "socket open-------");
                DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
                try {
                    while (!IsThreadDisable && Utils.isWifiConnected()) {
                        SWDefineConst.udpSocket.receive(datagramPacket);        //block
                        byte[] full_data = datagramPacket.getData();
                        byte[] data = Arrays.copyOf(full_data, datagramPacket.getLength());
                        String str = Utils.byteArrayToString(data);
                        LogUtils.i(LOG_TAG, "Get data source:" + datagramPacket.getAddress().toString() + ":" + datagramPacket.getPort() + " len:" + datagramPacket.getLength() + " data:" + str);
                        InetAddress addr = datagramPacket.getAddress();
                        handleData(data, addr);
                    }
                    LogUtils.i(LOG_TAG, "jump while ------------------");
                } catch (Exception e) {
//                    LogUtils.e(LOG_TAG, e.getMessage());
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        StartListen();
    }

    public void stop() {
        IsThreadDisable = true;
        if (SWDefineConst.udpSocket != null) {
            SWDefineConst.udpSocket.close();
            SWDefineConst.udpSocket = null;
            LogUtils.i(LOG_TAG, "jump while socket close-------");
        }
    }


    public void handleData(byte[] data, InetAddress address) {
        if (data.length < 4 || data[2] == 0) {
            return;
        }

        int start_point = 6;
        int len =0;
        if (data[3] == 1) {                     /* 返回ip_num = 1, 解析PDU_len */
            len = Utils.readInt16(data, 8);
            start_point = 10;
        } else {
            len = Utils.readInt16(data, 4);     /* 返回ip_num = 0, 解析PDU_len */
        }
        int pos = start_point;                  /* PDU 开始的地方 */
        if((len>2048) || len > data.length){
            return;
        }

        if (data[1] == 0) {
            handleScanLightResult(data, address);
        } else {
            int trans_no = Utils.unsignedByteToInt(data[1]);
            handleTransNo(data, trans_no);
        }

    }

    /* 处理服务端 ！主动！ 发送过来的数据 */
    void handleScanLightResult(byte[] data, InetAddress address) {
        boolean drop = false;
        byte[] ipaddr = address.getAddress();
        int ip = Utils.readIP(ipaddr, 0);
        int start_point = 6;
        Light light = null;
        ContentValues values = new ContentValues();
        values.put("connected", true);
        values.put("IP", ip);
        values.put("subIP", 0);
        int len;
        if (data[3] == 1) {
            len = Utils.readInt16(data, 8);
            start_point = 10;
        } else {
            len = Utils.readInt16(data, 4);
        }
        int pos = start_point;
        int mMediaStateLength = 0;
        while (pos < data.length && pos < start_point + len) {
            int dev_id = Utils.unsignedByteToInt(data[pos]);
            int attr_id = Utils.unsignedByteToInt(data[pos + 1]);
            int cmd = Utils.unsignedByteToInt(data[pos + 2]);
            int data_len = Utils.readInt16(data, pos + 3);
            byte[] sub_data = null;
            if (data_len > 0) {
                sub_data = new byte[data_len];
                for (int i = 0; i < data_len; i++) {
                    sub_data[i] = data[i + pos + 5];
                }
            }else {
                pos += 5 + data_len;
                continue;
            }
            pos += 5 + data_len;
            switch (dev_id) {
                case 0:
                    switch (attr_id) {
                        case 3:
                            if (light != null && TextUtils.isEmpty(light.name))
                                light.name = Utils.stringFromBytes(sub_data);
                            break;
                        case 5:
                            if (light != null) {
                                light.firmwareVersion = Utils.stringFromBytes(sub_data);
                                values.put("firmware_version", light.firmwareVersion);
                            }
                            break;
                        case 7:
                            if (light != null) {
                                light.model = Utils.stringFromBytes(sub_data);
                                values.put("model", light.model);
                            }
                            break;
                        case 9:
                            if (light != null) {
                                light.type = Utils.unsignedByteToInt(sub_data[0]);
                                values.put("type", light.type);
                            }
                            break;

                        case 10:
                            if (light != null) {
                                light.number = Utils.unsignedByteToInt(sub_data[0]);
                                values.put("number", light.number);
                            }
                            break;
                    }
                    break;
                case 1:
                    if (attr_id == 2) {
                        if (sub_data != null && sub_data.length>=4) {
                            int selfIP = Utils.readIP(sub_data, 0);
                            if (selfIP != ip) {
                                values.put("subIP", selfIP);
                                light.subIP = selfIP;
                            }
                        }
                    } else if (attr_id == 1) {
                        String mac = Utils.byteArrayToString(sub_data, (char) 0);
                        values.put("MAC", mac);
                        if (Global.gLightsMacTimeMap.containsKey(mac)){
                            if (System.currentTimeMillis() - Global.gLightsMacTimeMap.get(mac) < 1000)
                                drop = true;
                        }
                        light = Global.gLightsMacMap.get(mac);
                        if (light == null) {
                            light = new Light();
                            light.mac = mac;
                            light.ip = ip;
                            Global.gLightsMacMap.put(mac, light);
                        }
                    }
                    break;

                case 10: {
                    if (light == null) {
                        break;
                    }
                    switch (attr_id) {
                        case 0:
                            light.state = sub_data[0] > 0;
                            values.put("state", light.state);
                            break;
                        case 1:
                            light.color = Utils.getRGBColor(sub_data);
                            values.put("color", light.color);
                            break;
                        case 2:
                            light.brightness = Utils.unsignedByteToInt(sub_data[0]);
                            values.put("brightness", light.brightness);
                            break;
                        case 3:
                            light.CT = Utils.unsignedByteToInt(sub_data[0]);
                            values.put("CT", light.CT);
                            break;
                    }
                }
                break;

                case 11: {
                    if (light == null) {
                        break;
                    }
                    switch (attr_id) {
                        case 0:
//                            Utils.saveSSIDState(mContext, light.mac, sub_data[0] > 0);
                            light.ap_state = sub_data[0] > 0;
                            values.put("AP_state", light.ap_state);
                            break;

                        case 1:
                            light.ap_bssid = Utils.stringFromBytes(sub_data);
                            values.put("AP_BSSID", light.ap_bssid);
                            break;

                        case 2:
//                            Utils.saveSSID(mContext, light.mac, Utils.stringFromBytes(sub_data));
                            light.ap_ssid = Utils.stringFromBytes(sub_data);
                            values.put("AP_SSID", light.ap_ssid);
                            break;

                        case 3:
//                            Utils.saveSSIDPWD(mContext, light.mac, Utils.stringFromBytes(sub_data));
                            light.ap_pass = Utils.stringFromBytes(sub_data);
                            values.put("AP_Pass", light.ap_pass);
                            break;
                    }
                }
                break;

                case 14: {
                    if (light == null || sub_data == null) {
                        break;
                    }
                    switch (attr_id) {
                        case 1:
                            if (light != null) {
                                light.sens = Utils.unsignedByteToInt(sub_data[0]);
                                values.put("sens", light.sens);
                            }
                        case 2:
                            if (light != null) {
                                light.lux = Utils.unsignedByteToInt(sub_data[0]);
                                values.put("lux", light.lux);
                            }
                    }
                }
                break;
            }

        }
        if (drop) {
            LogUtils.i("update:", "drop return----------------");
            return;
        }

        //Update to light
        if (light != null) {
            if (TextUtils.isEmpty(light.model)) {
                light.model = "Unknown";
            }
            if (light.id < 0) {
                Cursor c = mContext.getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"_id"}, "MAC=(?)", new String[]{light.mac}, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        light.id = c.getInt(0);
                    }
                    c.close();
                }
            }
            if (light.id >= 0) {    //更新已经有的数据
                values.put("last_active", System.currentTimeMillis());
                values.put("deleted", 0);
                mContext.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "_id=" + light.id, null);
                LogUtils.i("update:", "update----------" + light.id + "------" );
            }else{                  //搜索到新灯
                addLightToList(mContext, light);
            }
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.ACTION_UPDATED));
        }

    }

    void addLightToList(Context context, Light light) {
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "MAC=(?) AND deleted=0", new String[]{light.mac}, null);
        if (cursor.moveToFirst()) {
            light.added = true;
        }
        cursor.close();

        if (!light.added){
            addLightToDB(context, light);
        }
    }

    void addLightToDB(Context context, Light light) {
        ContentValues values = new ContentValues();
        values.put("MAC", light.mac);
        values.put("model", light.model);
        values.put("connected", true);
        values.put("state", light.state);
        values.put("IP", light.ip);
        values.put("subIP", light.subIP);
        values.put("color", light.color);
        values.put("brightness", Math.max(light.brightness, Constants.MIN_BRIGHTNESS));
        values.put("CT", Math.max(light.CT, Constants.MIN_CT));
        values.put("mode", 0);
        values.put("name", light.name);
        values.put("owned_time", System.currentTimeMillis());
        values.put("deleted", 0);
        values.put("firmware_version", light.firmwareVersion);
        values.put("media_state", light.mediaState);
        values.put("UID", light.uid);

        context.getContentResolver().insert(YanKonProvider.URI_LIGHTS, values);
        LogUtils.i("update:", "update ---add light ------------------------------------------");
    }

    /* 处理服务端的回包 发送过来的数据 ，只要发送包有 回包就证明上次发送包是成功的！*/
    void handleTransNo(byte[] data, int trans_no) {
        int len, flag = 0;
        String mac = null;
        int start_point = 6;
        if (data[3] == 1) {
            len = Utils.readInt16(data, 8);
            start_point = 10;
        } else {
            len = Utils.readInt16(data, 4);
        }
        int pos = start_point;//123
        if((len>2048) || len > data.length){
            return;
        }
        while (pos < data.length && pos < start_point + len) {
            int dev_id = Utils.unsignedByteToInt(data[pos]);
            int attr_id = Utils.unsignedByteToInt(data[pos + 1]);
            int cmd = Utils.unsignedByteToInt(data[pos + 2]);
            int data_len = Utils.readInt16(data, pos + 3);
            byte[] sub_data = null;
            if (data_len > 0) {
                sub_data = new byte[data_len];
                for (int i = 0; i < data_len; i++) {
                    sub_data[i] = data[i + pos + 5];
                }
            }else {
                pos += 5 + data_len;
                continue;
            }

            pos += 5 + data_len;
            int ret_vel = sub_data[0];
            if (sub_data[0] > 127) {
                ret_vel = 128 - sub_data[0];
            } else {

            }
            switch (dev_id) {

                case 1: {
                    switch (attr_id) {
                        case 1:
                            mac = Utils.byteArrayToString(sub_data, (char) 0);
                            break;
                        case 2:
                            break;
                    }
                    break;
                }
                case 10: {
                    switch (attr_id) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            if (ret_vel < 0) {
                                LogUtils.i("send msg remove11", mac + trans_no + sub_data[0]);
                                return;
                            }
                            break;
                    }
                    break;
                }
                case 12: {
                    switch (attr_id) {
                        case 2:
                        case 3:
                        case 4:

                            if (ret_vel < 0) {
                                LogUtils.i("send msg remove", mac + trans_no + sub_data[0]);
                                return;
                            }
                            break;
                    }
                    break;
                }
            }
        }

        for(TransData tdata :Global.gCommandTransactions.values()){
            if(tdata.key.equals(mac) && tdata.trans_no == trans_no){
                Global.gCommandTransactions.remove(mac);
                LogUtils.i("send msg remove",mac+trans_no);
                break;
            }
        }
    }
}