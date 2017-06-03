package com.yankon.smart.model;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import com.yankon.smart.DaemonHandler;
import com.yankon.smart.utils.CommandBuilder;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;

/**
 * Created by Evan on 15/2/10.
 */
public class Command {
    public enum CommandType {
        CommandTypeNone(0),
        CommandTypeState(1),
        CommandTypeColor(2),
        CommandTypeCT(3),
        CommandTypeBrightness(4),
        CommandTypeAll(5),
        CommandTypeResetWifi(6),
        CommandTypeType(7),
        CommandTypeID(8),
        CommandTypeSENS(9),
        CommandTypeLux(10),
        CommandRunModel(11);

        private int value;

        private CommandType(int value) {
            this.value = value;
        }

        public String getColumnName() {
            switch (value) {
                case 1:
                    return "state";
                case 2:
                    return "color";
                case 3:
                    return "CT";
                case 4:
                    return "brightness";

                case 7:
                    return "type";
                case 8:
                    return "number";
                case 9:
                    return "sens";
                case 10:
                    return "lux";
                case 11:
                    return "runmodel";

                default:
                    return "";
            }
        }
    }

    public enum CommandSwitchsType {
        CommandTypeNone(0),
        CommandTypeKey1(1),
        CommandTypeKey2(2),
        CommandTypeKey3(3),
        CommandTypeState(4),
        CommandTypeAll(5),
        CommandTypeResetWifi(6);


        private int value;

        private CommandSwitchsType(int value) {
            this.value = value;
        }

        public String getColumnName() {
            switch (value) {
                case 1:
                    return "key1";
                case 2:
                    return "key2";
                case 3:
                    return "key3";
                case 4:
                    return "state";
                default:
                    return "";
            }
        }

    }

    private int trans_no = 1;

    public CommandType type;
    public int value;
    public StatusInfo info; //Only used for CommandTypeAll

    public CommandSwitchsType switchsType;
    public SwitchsStatusInfo switchsStatusInfo;

    //public static String lastLightKey = null;
    public static String currentLightKey = null;
    //public static String lastSwitchKey = null;
    public static String currentSwitchKey = null;

    public Command(StatusInfo info) {
        super();
        this.type = CommandType.CommandTypeAll;
        this.info = info;
    }

    public Command(CommandType type, int value) {
        super();
        this.type = type;
        this.value = value;
    }

    public Command(SwitchsStatusInfo info){
        super();
        this.switchsType = CommandSwitchsType.CommandTypeAll;
        this.switchsStatusInfo = info;
    }
    public Command(CommandSwitchsType type, int value){
        super();
        this.switchsType = type;
        this.value = value;
    }

    public void fillInContentValues(ContentValues values) {
        switch (type) {
            case CommandTypeCT:
                values.put("mode", Constants.MODE_CT);
                values.put(type.getColumnName(), value);
                break;

            case CommandTypeColor:
                values.put("mode", Constants.MODE_COLOR);
                values.put(type.getColumnName(), value);
                break;

            case CommandTypeState:
            case CommandTypeBrightness:
            case CommandTypeType:
            case CommandTypeID:
            case CommandTypeSENS:
            case CommandTypeLux:
            case CommandRunModel:
                values.put(type.getColumnName(), value);
                break;

            case CommandTypeAll:
                values.put("mode", info.mode);
                values.put("color", info.color);
                values.put("state", info.state);
                values.put("CT", info.CT);
                values.put("brightness", info.brightness);
                break;

            case CommandTypeResetWifi:
                values.put("connected", false);
                values.put("IP", 0);
                values.put("subIP", 0);
                break;
            case CommandTypeNone:
                break;

            default:
                break;
        }
    }


    public void fillInSwitchsContentValues(ContentValues values) {
        switch (switchsType) {
            case CommandTypeState:
            case CommandTypeKey1:
            case CommandTypeKey2:
            case CommandTypeKey3:
                values.put(switchsType.getColumnName(), value);
                break;

            case CommandTypeAll:
                values.put("mode", switchsStatusInfo.mode);
                values.put("state", switchsStatusInfo.state);
                values.put("key1", switchsStatusInfo.key1);
                values.put("key2", switchsStatusInfo.key2);
                values.put("key3", switchsStatusInfo.key3);
                break;

            case CommandTypeResetWifi:
                values.put("connected", false);
                values.put("IP", 0);
                values.put("subIP", 0);
                break;
            case CommandTypeNone:
            default:
                break;
        }
    }

    public byte[] build(String mac, int IP, int subIP) {
        CommandBuilder cb = new CommandBuilder();
        byte[] data;
        cb.append((byte) 1, (byte) 1, (byte) 0, null);      /* first read mac from db */
        switch (type) {
            case CommandTypeResetWifi:
                data = new byte[1];
                data[0] = 0;
                cb.append((byte) 2, (byte) 4, (byte) 1, data);
                break;

            case CommandTypeType:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 0, (byte) 9, (byte) 1, data);     /* wirte light status into db */
                break;

            case CommandTypeID:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 0, (byte) 10, (byte) 1, data);     /* wirte light status into db */
                break;

            case CommandTypeSENS:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 14, (byte) 1, (byte) 1, data);     /* wirte light status into db */
                break;

            case CommandTypeLux:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 14, (byte) 2, (byte) 1, data);     /* wirte light status into db */
                break;


            case CommandTypeState:
                data = new byte[1];
                data[0] = (byte) (value > 0 ? 1 : 0);
                cb.append((byte) 10, (byte) 0, (byte) 1, data);     /* wirte light status into db */
                break;

            case CommandRunModel:
                data = new byte[1];
                data[0] = (byte) (value > 0 ? 1 : 0);
                cb.append((byte) 14, (byte) 0, (byte) 1, data);     /* wirte light status into db */
                break;


            case CommandTypeColor: {
                int color = value & 0x00FFFFFF;
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                data = new byte[3];
                data[2] = (byte) b;
                data[1] = (byte) g;
                data[0] = (byte) r;
                cb.append((byte) 10, (byte) 1, (byte) 1, data);
            }
            break;

            case CommandTypeBrightness:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 10, (byte) 2, (byte) 1, data);
                break;

            case CommandTypeCT:
                data = new byte[1];
                data[0] = (byte) value;
                cb.append((byte) 10, (byte) 3, (byte) 1, data);
                break;

            case CommandTypeAll: {
                data = new byte[1];
                data[0] = (byte) info.brightness;
                cb.append((byte) 10, (byte) 2, (byte) 1, data);
                data = new byte[1];
                data[0] = (byte) (info.state ? 1 : 0);
                cb.append((byte) 10, (byte) 0, (byte) 1, data);

                if (info.mode == Constants.MODE_COLOR) {
                    int color = info.color & 0x00FFFFFF;
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;

                    data = new byte[3];
                    data[2] = (byte) b;
                    data[1] = (byte) g;
                    data[0] = (byte) r;
                    cb.append((byte) 10, (byte) 1, (byte) 1, data);
                } else {
                    data = new byte[1];
                    data[0] = (byte) info.CT;
                    cb.append((byte) 10, (byte) 3, (byte) 1, data);
                }
            }
            break;

            default:
                break;
        }

        if (!TextUtils.isEmpty(mac)) {
            /*方案一 把所有命令单独存下来
            //trans num是0是预留给广播的
            //所以要模254，得到0~254之间的数再加1，保证是一个1~255之间的trans no
            trans_no = 0;
            Integer max_trans_num = Global.gCommandTransNums.get(mac);
            if (max_trans_num != null) {
                trans_no = max_trans_num + 1;
            }
            trans_no %= 254;
            trans_no++;
            Global.gCommandTransNums.put(mac, trans_no);
            */
            //方案二 总共就四类操作，状态颜色色温亮度，占用四个trans_no
            //防止例如第一次改颜色失败自动重试，但第二次改颜色成功，之后又被第一次的重试覆盖
            //所以所有颜色的修改互相覆盖好了

            trans_no = 1;
            for (TransData tdata : Global.gCommandTransactions.values()){
                if(tdata.key.isEmpty()&& tdata.key.equals(mac)) {
                    trans_no = (tdata.trans_no)%254 + 1;
                    Global.gCommandTransactions.remove(mac);
                    break;
                }
            }
        }
        byte[] result = cb.build(trans_no, subIP);      /* 从开始组装命令 */
        if (!TextUtils.isEmpty(mac)) {
            TransData transData = new TransData();
            transData.data = result;
            transData.IP = IP;
            transData.time = System.currentTimeMillis();
            transData.type = type.value;
            transData.trans_no = trans_no;
            transData.key = mac ;

            Global.gCommandTransactions.put(transData.key, transData);
            Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_CHECK_TRANS);

//            currentLightKey = mac + "," + type + "," + trans_no;
//            String currentKey = mac + "," + type;
//            /* 取消等待上一次的回包 */
//            if (Global.transLightsMap.containsKey(currentKey)){
//                Global.gCommandTransactions.remove(Global.transLightsMap.get(currentKey));
//            }
//
//            Global.transLightsMap.put(currentKey, currentLightKey);
//            Global.gCommandTransactions.put(currentLightKey, transData);
//            Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_CHECK_TRANS);
        }
        return result;
    }


    public byte[] buildSwitch(String mac, int IP, int subIP) {
        String switchTypeKey = null;
        CommandBuilder cb = new CommandBuilder();
        byte[] data;
        cb.append((byte) 1, (byte) 1, (byte) 0, null);      /* first read mac from db */

        data = new byte[1];
        data[0] = (byte) (value > 0 ? 1 : 0);
        switch(switchsType){
            case CommandTypeState:
                cb.append((byte) 12, (byte) 0, (byte) 1, data);
                switchTypeKey = "state";
                break;

            case CommandTypeKey1:
                cb.append((byte) 12, (byte) 1, (byte) 1, data);     /* wirte switch status into db */
                switchTypeKey = "key1";
                break;

            case CommandTypeKey2:
                cb.append((byte) 12, (byte) 2, (byte) 1, data);     /* wirte switch status into db */
                switchTypeKey = "key2";
                break;

            case CommandTypeKey3:
                cb.append((byte) 12, (byte) 3, (byte) 1, data);     /* wirte switch status into db */
                switchTypeKey = "key3";
                break;

            case CommandTypeAll:
                data = new byte[1];
                data[0] = (byte)(switchsStatusInfo.state ? 1 : 0);
                cb.append((byte) 12, (byte) 0, (byte) 1, data);
                data[0] = (byte)(switchsStatusInfo.key1 ? 1 : 0);
                cb.append((byte) 12, (byte) 1, (byte) 1, data);
                data[0] = (byte)(switchsStatusInfo.key2 ? 1 : 0);
                cb.append((byte) 12, (byte) 2, (byte) 1, data);
                data[0] = (byte)(switchsStatusInfo.key3 ? 1 : 0);
                cb.append((byte) 12, (byte) 3, (byte) 1, data);
                switchTypeKey = "all";
                break;
        }

        if (!TextUtils.isEmpty(mac)) {
            /*方案一 把所有命令单独存下来
            //trans num是0是预留给广播的
            //所以要模254，得到0~254之间的数再加1，保证是一个1~255之间的trans no
            trans_no = 0;
            Integer max_trans_num = Global.gCommandTransNums.get(mac);
            if (max_trans_num != null) {
                trans_no = max_trans_num + 1;
            }
            trans_no %= 254;
            trans_no++;
            Global.gCommandTransNums.put(mac, trans_no);
            */
            //方案二 总共就四类操作，状态颜色色温亮度，占用四个trans_no
            //防止例如第一次改颜色失败自动重试，但第二次改颜色成功，之后又被第一次的重试覆盖
            //所以所有颜色的修改互相覆盖好了

            trans_no = 1 + switchsType.value;
        }
        byte[] result = cb.build(trans_no, subIP);      /* 从开始组装命令 */
        if (!TextUtils.isEmpty(mac)) {
            TransData transData = new TransData();
            transData.data = result;
            transData.IP = IP;
            transData.time = System.currentTimeMillis();
            currentSwitchKey = mac + "," + switchTypeKey + "," + trans_no;

            /* 取消等待上一次的回包 */
//            if (TextUtils.equals(lastSwitchKey, currentSwitchKey) &&
//                    Global.gCommandTransactions.containsKey(lastSwitchKey)){
//                Global.gCommandTransactions.remove(lastSwitchKey);
//            }
//            Global.gCommandTransactions.put(currentSwitchKey, transData);
//            Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_CHECK_TRANS);
//            lastSwitchKey = currentSwitchKey;
        }
        return result;
    }

}
