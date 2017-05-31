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
import android.widget.Toast;

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
import com.yankon.smart.utils.RxBus;
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
    private static final String LOG_TAG = "UdpHelper";
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
                DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
                try {
                    while (!IsThreadDisable && App.isWifiConnected()) {
                        SWDefineConst.udpSocket.receive(datagramPacket);        //block
                        byte[] full_data = datagramPacket.getData();
                        byte[] data = Arrays.copyOf(full_data, datagramPacket.getLength());
                        String str = Utils.byteArrayToString(data);
                        LogUtils.i(LOG_TAG, "Get data source:" + datagramPacket.getAddress().toString()+":"+datagramPacket.getPort() + " len:" + datagramPacket.getLength() + " data:" + str);
                        InetAddress addr = datagramPacket.getAddress();
                        handleData(data, addr);
                    }
                } catch (IOException e) {
                    LogUtils.e(LOG_TAG, e.getMessage());
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

//        if (data[1] == 0){
//            while (pos < data.length && pos < start_point + len) {
//                int dev_id = Utils.unsignedByteToInt(data[pos]);
//                int attr_id = Utils.unsignedByteToInt(data[pos + 1]);
//                int cmd = Utils.unsignedByteToInt(data[pos + 2]);
//                int data_len = Utils.readInt16(data, pos + 3);
//                byte[] sub_data = null;
//                if (data_len > 0) {
//                    sub_data = new byte[data_len];
//                    for (int i = 0; i < data_len; i++) {
//                        sub_data[i] = data[i + pos + 5];
//                    }
//                }
//                pos += 5 + data_len;
//                if (dev_id == 0 && attr_id == 7){
//                    String modeType = Utils.stringFromBytes(sub_data);
//                    if (Utils.getModelType(modeType) == Utils.LIGHTS_MODEL){
//                        Log.i(LOG_TAG, "灯具···························" );
//                        handleScanLightResult(data, address);
//                        return;
//                    }else{
//                        Log.i(LOG_TAG, "开关 ······························" );
//                        handleScanSwitchResult(data, address);
//                        return;
//                    }
//                }
//            }
//        }else{
//            int trans_no = Utils.unsignedByteToInt(data[1]);
//            handleTransNo(data, trans_no);
//        }

    }

    private void handleScanSwitchResult(byte[] data, InetAddress address) {
        byte[] ipaddr = address.getAddress();
        int ip = Utils.readIP(ipaddr, 0);
        int start_point = 6;
        ContentValues values = new ContentValues();
        values.put("connected", true);
        values.put("IP", ip);
        values.put("subIP", 0);
        int len;

        /* 以后要改 */
        if (data[3] == 1) {
            len = Utils.readInt16(data, 8);
            start_point = 10;
        } else {
            len = Utils.readInt16(data, 4);
        }
        int pos = start_point;                                  /* 解析PDU */
        Switchs switchs = null;
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
            }
            pos += 5 + data_len;
            switch (dev_id) {
                case 0:
                    switch (attr_id) {
                        case 3:
                            if (switchs != null && TextUtils.isEmpty(switchs.name))
                                switchs.name = Utils.stringFromBytes(sub_data);
                            break;
                        case 5:
                            if (switchs != null) {
                                switchs.firmwareVersion = Utils.stringFromBytes(sub_data);
                                values.put("firmware_version", switchs.firmwareVersion);
                            }
                            break;
                        case 7:                                         /* 区别灯和开关 */
                            if (switchs != null) {
                                switchs.model = Utils.stringFromBytes(sub_data);
                                values.put("model", switchs.model);
                            }
                            break;
                    }
                    break;
                case 1:
                    if (attr_id == 2) {
                        if (sub_data != null && sub_data.length >= 4) {
                            int selfIP = Utils.readIP(sub_data, 0);
                            if (selfIP != ip) {
                                values.put("subIP", selfIP);
                                switchs.subIP = selfIP;
                            }
                        }
                    } else if (attr_id == 1) {
                        String mac = Utils.byteArrayToString(sub_data, (char) 0);
                        values.put("MAC", mac);
                        switchs = Global.gSwitchsMacMap.get(mac);

                        if (switchs == null) {
                            switchs = new Switchs();
                            switchs.mac = mac;
                            switchs.ip = ip;
                            Global.gSwitchsMacMap.put(mac, switchs);
                        }
                    }
                    break;

                case 12: {
                    if (switchs == null) {
                        break;
                    }

                    switch (attr_id) {
                        case 1:
                            switchs.key1 = sub_data[0] > 0;
                            values.put("key1", switchs.key1);
                            break;
                        case 2:
                            switchs.key2 = sub_data[0] > 0;
                            values.put("key2", switchs.key2);
                            break;
                        case 3:
                            switchs.key3 = sub_data[0] > 0;
                            values.put("key3", switchs.key3);
                            break;
                    }
                }
                break;
            }
        }

        //Update to light
        if (switchs != null) {
            if (TextUtils.isEmpty(switchs.model)) {
                switchs.model = "Unknown";
            }

            if (switchs.id < 0) {
                Cursor c = mContext.getContentResolver().query(YanKonProvider.URI_SWITCHS, new String[]{"_id"}, "MAC=(?)", new String[]{switchs.mac}, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        switchs.id = c.getInt(0);
                    }
                    c.close();
                }
            }
            /* 保存active状态 */
            if (switchs.id >= 0) {
                values.put("last_active", System.currentTimeMillis());
                mContext.getContentResolver().update(YanKonProvider.URI_SWITCHS, values, "_id=" + switchs.id, null);
            }
            /* add switch to db */
            Intent intent = new Intent(Constants.ACTION_UPDATED);
            intent.putExtra(Constants.SEND_TYPE, "switch");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
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
                            //light.CT = Utils.unsignedByteToInt(sub_data[0]);
                            //values.put("CT", light.CT);
                            break;
                    }
                }

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
            if (light.id >= 0) {
                values.put("last_active", System.currentTimeMillis());
                mContext.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "_id=" + light.id, null);
            }
            if (drop)
                return;
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.ACTION_UPDATED));
        }
            /* add light to db */
//            Intent intent = new Intent(Constants.ACTION_UPDATED);
//            intent.putExtra(Constants.SEND_TYPE, "light");
//            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
//            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.ACTION_UPDATED));
//        }
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

//            if (dev_id == 1 && attr_id == 1) {
//                mac = Utils.byteArrayToString(sub_data, (char) 0);
//                Global.gCommandTransactions.remove(mac + "," + trans_no);
//                break;
//            }


//            if (dev_id == 1 && attr_id == 1){
//                mac = Utils.byteArrayToString(sub_data, (char) 0);
//            }
//
//            if (dev_id == 10 && attr_id == 0){
//                flag = 1;
//            }else if(dev_id == 10 && attr_id == 1){
//                flag = 2;
//            }else if(dev_id == 10 && attr_id == 2){
//                flag = 3;
//            }else if(dev_id == 10 && attr_id == 3){
//                flag = 4;
//            }

//                if (Global.gLightsMacMap.containsKey(mac)){
//                    Global.transLightsList.remove(Command.currentLightKey);
//                    Global.gCommandTransactions.remove(Command.currentLightKey);
//                }else if(Global.gSwitchsMacMap.containsKey(mac)){
//                    Global.transSwitchsList.remove(Command.currentSwitchKey);
//                    Global.gCommandTransactions.remove(Command.currentLightKey);
//                }else
//                    ;
//                break;

//                if (Global.gLightsMacMap.containsKey(mac))
//                    Global.gCommandTransactions.remove(Command.lastLightKey);   /* 删除待确认包的状态 */
//                else if (Global.gSwitchsMacMap.containsKey(mac))
//                    Global.gCommandTransactions.remove(Command.lastSwitchKey);
//                else
//                    ;
//                break;

//        if (flag == 0)
//            flag = 5;
//
//        if (Global.gCommandTransactions.containsKey(mac + "," + flag + "," + trans_no)){
//            Global.gCommandTransactions.remove(mac + "," + flag + "," + trans_no);
//            Global.transLightsMap.remove(mac + "," + flag);
//        }


    }
}