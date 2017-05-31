package com.yankon.smart.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;

import com.kii.cloud.storage.KiiUser;
import com.yankon.smart.model.Command;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.StatusInfo;
import com.yankon.smart.model.Switchs;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.services.NetworkSenderService;

import java.util.ArrayList;

/**
 * Created by tian on 14/11/25:上午9:00.
 */
public class Utils {
    public static final int LIGHTS_MODEL = 1;
    public static final int SWITCH_MODEL = 2;

    public static int getModelType(String modelName){
        if (TextUtils.isEmpty(modelName))
            return -1;

        int model_type = Integer.valueOf(modelName.substring(0, 2));
        if (model_type >= 10)
            return SWITCH_MODEL;
        else if (model_type > 0 && model_type < 10)
            return LIGHTS_MODEL;
        else
            return -1;
    }

    public static String getMacString(byte[] data, int pos){
        byte[] mac_data = new byte[6];
        for (int i = 0; i < 6; i++) {
            mac_data[i] = data[i + pos + 5];
        }
        String mac = Utils.byteArrayToString(mac_data, (char) 0);
        return mac;
    }

    public static float dp2Px(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static int px2Dp(Context context, int pixel) {
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixel,
                context.getResources().getDisplayMetrics());
        return dp;
    }

    public static String byteArrayToString(byte[] cmd, char seperator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cmd.length; i++) {
            if (seperator != 0)
                sb.append(seperator);
            sb.append(Utils.ByteToHexString(cmd[i]));
        }
        return sb.toString();
    }

    public static String byteArrayToString(byte[] cmd) {
        return byteArrayToString(cmd, ' ');
    }

    public static void byteArrayToString(byte[] cmd, StringBuilder sb) {
        for (int i = 0; i < cmd.length; i++) {
            sb.append(' ');
            sb.append(Utils.ByteToHexString(cmd[i]));
        }
    }

    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static int readInt16(byte[] data, int offset) {
        return (unsignedByteToInt(data[offset + 1]) << 8) + unsignedByteToInt(data[offset]);
    }

    public static int readIP(byte[] data, int offset) {
        return (unsignedByteToInt(data[offset]) << 24) + (unsignedByteToInt(data[offset + 1]) << 16) + (unsignedByteToInt(data[offset + 2]) << 8) + unsignedByteToInt(data[offset + 3]);
    }

    public static void Int16ToByte(int data, byte[] arr, int pos) {
        arr[pos + 1] = (byte) (data >> 8);
        arr[pos] = (byte) (data & 0xFF);
    }

    public static int getRGBColor(byte[] data) {
        if (data == null || data.length < 3) {
            return Color.WHITE;
        }
        return (unsignedByteToInt(data[2]) << 16) + (unsignedByteToInt(data[1]) << 8) + unsignedByteToInt(data[0]);
    }

    public static String stringFromBytes(byte[] data) {
        if (data == null)
            return "";
        int size = 0;
        while (size < data.length) {
            if (data[size] == 0) {
                break;
            }
            size++;
        }
        return new String(data, 0, size);
    }

    public static int char2int(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        } else if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        return 0;
    }

    public static String ByteToHexString(byte b) {
        char[] ch = new char[2];
        int a = unsignedByteToInt(b);
        int t = a / 16;
        if (t < 10) {
            ch[0] = (char) ('0' + t);
        } else {
            ch[0] = (char) ('A' + t - 10);
        }

        t = a % 16;
        if (t < 10) {
            ch[1] = (char) ('0' + t);
        } else {
            ch[1] = (char) ('A' + t - 10);
        }
        return new String(ch);
    }

    public static String buildNumsInSQL(String[] data) {
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(s);
        }
        return "(" + sb.toString() + ")";
    }

    public static String buildStringsInSQL(String[] data) {
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append('\'');
            sb.append(s);
            sb.append('\'');
        }
        return "(" + sb.toString() + ")";
    }


    public static String intToIp(int i) {
        if (i == 0)
            return "255.255.255.255";
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    public static String formatMac(String mac) {
        if (mac == null || mac.length() != 12) {
            return mac;
        }
        char[] res = new char[17];
        for (int i = 0; i < 12; i++) {
            res[i / 2 * 3 + i % 2] = mac.charAt(i);
        }
        for (int i = 0; i < 5; i++) {
            res[2 + i * 3] = ':';
        }
        return new String(res);
    }

    public static void setAllLightsOffline(Context context) {
        ContentValues values = new ContentValues();
        values.put("connected", false);
        values.put("IP", 0);
        values.put("subIP", 0);
        context.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, null, null);
    }

    public static void setAllSwitchsOffline(Context context) {
        ContentValues values = new ContentValues();
        values.put("connected", false);
        values.put("IP", 0);
        values.put("subIP", 0);
        context.getContentResolver().update(YanKonProvider.URI_SWITCHS, values, null, null);
    }

    public static void controlLight(final Context context, final int light_id, final Command cmd, boolean doItNow) {
        controlLightsById(context, new String[]{String.valueOf(light_id)}, cmd, doItNow);
    }

    public static void controlSwitch(final Context context, final int switch_id, final Command cmd, boolean doItNow) {
        controlSwitchsById(context, new String[]{String.valueOf(switch_id)}, cmd, doItNow);
    }

    public static void controlGroup(final Context context, final int group_id, final Command cmd, boolean doItNow) {
        ArrayList<String> lightIds = new ArrayList<>();
        Cursor c = context.getContentResolver().query(YanKonProvider.URI_LIGHT_GROUP_REL, null, "group_id=" + group_id, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String light_id = c.getString(c.getColumnIndex("light_id"));
                lightIds.add(light_id);
            }
            c.close();
        }
        ContentValues values = new ContentValues();
        cmd.fillInContentValues(values);
        values.put("synced", false);
        context.getContentResolver().update(YanKonProvider.URI_LIGHT_GROUPS, values, "_id=" + group_id, null);
        String[] lights = lightIds.toArray(new String[lightIds.size()]);
        controlLightsById(context, lights, cmd, doItNow);
    }

    public static void controlLightsById(final Context context, final String[] lights, final Command cmd, boolean doItNow) {
        if (lights == null || lights.length == 0)
            return;
        final String where;
        String mac = null;
        if (lights.length == 1) {
            where = "_id='" + lights[0] + "'";
        } else {
            where = "_id in " + buildNumsInSQL(lights);
        }
        ArrayList<Light> connectedLights = new ArrayList<Light>();
        final ArrayList<Pair<String, String>> unconnectedLights = new ArrayList<>();
        Cursor c = context.getContentResolver().query(YanKonProvider.URI_LIGHTS, null, where, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                boolean connected = c.getInt(c.getColumnIndex("connected")) > 0;
                int ip = c.getInt(c.getColumnIndex("IP"));
                mac = c.getString(c.getColumnIndex("MAC"));
                if (connected && ip != 0) {
                    int subIP = c.getInt(c.getColumnIndex("subIP"));
                    Light light = new Light();
                    light.ip = ip;
                    light.subIP = subIP;
                    light.mac = mac;
                    connectedLights.add(light);
                } else {
                    String remotePwd = c.getString(c.getColumnIndex("remote_pwd"));
                    String remote = c.getString(c.getColumnIndex("remote"));
                    if (remotePwd != null && remotePwd.length() == 4 && TextUtils.isEmpty(remote))
                        unconnectedLights.add(new Pair(mac, remotePwd));
                }
            }
            c.close();
        }

        /* 通过UDP Socket 发送命令 */
        if (connectedLights.size() > 0) {
            for (Light light : connectedLights) {
                if (light.subIP != 0)
                    sendCmdToLocalLight(context, light, cmd);
            }
            for (Light light : connectedLights) {
                if (light.subIP == 0)
                    sendCmdToLocalLight(context, light, cmd);
            }
        }

        /* ？？？？*/
        if (doItNow && unconnectedLights.size() > 0 && KiiUser.isLoggedIn()) {
            new Thread() {
                @Override
                public void run() {
                    KiiSync.fireLamp(unconnectedLights, false, cmd);
                }
            }.start();
        }

        /* 更新数据库 */
        //修改状态放最后, 因为有解散组网，解散后会取不到IP
        ContentValues values = new ContentValues();
        cmd.fillInContentValues(values);
        context.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, where, null);
        if(mac!=null) {
             Global.gLightsMacTimeMap.put(mac, System.currentTimeMillis());
        }
    }

    public static void controlSwitchsById(final Context context, final String[] switchs, final Command cmd, boolean doItNow) {
        if (switchs == null || switchs.length == 0)
            return;
        final String where;
        if (switchs.length == 1) {
            where = "_id='" + switchs[0] + "'";
        } else {
            where = "_id in " + buildNumsInSQL(switchs);
        }
        ArrayList<Switchs> connectedSwitchs = new ArrayList<>();
        final ArrayList<Pair<String, String>> unconnectedSwitchs = new ArrayList<>();
        Cursor c = context.getContentResolver().query(YanKonProvider.URI_SWITCHS, null, where, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                boolean connected = c.getInt(c.getColumnIndex("connected")) > 0;
                int ip = c.getInt(c.getColumnIndex("IP"));
                String mac = c.getString(c.getColumnIndex("MAC"));
                if (connected && ip != 0) {
                    int subIP = c.getInt(c.getColumnIndex("subIP"));
                    Switchs item = new Switchs();
                    item.ip = ip;
                    item.subIP = subIP;
                    item.mac = mac;
                    connectedSwitchs.add(item);
                } else {
                    String remotePwd = c.getString(c.getColumnIndex("remote_pwd"));
                    String remote = c.getString(c.getColumnIndex("remote"));
                    if (remotePwd != null && remotePwd.length() == 4 && TextUtils.isEmpty(remote))
                        unconnectedSwitchs.add(new Pair(mac, remotePwd));
                }
            }
            c.close();
        }

        if (connectedSwitchs.size() > 0) {
            for (Switchs s : connectedSwitchs) {
                if (s.subIP != 0)
                    sendCmdToLocalSwitch(context, s, cmd);
            }
            for (Switchs s : connectedSwitchs) {
                if (s.subIP == 0)
                    sendCmdToLocalSwitch(context, s, cmd);
            }
        }

        if (doItNow && unconnectedSwitchs.size() > 0 && KiiUser.isLoggedIn()) {
            new Thread() {
                @Override
                public void run() {
                    KiiSync.fireLamp(unconnectedSwitchs, false, cmd);
                }
            }.start();
        }

        /* 更新数据库 */
        //修改状态放最后, 因为有解散组网，解散后会取不到IP
        ContentValues values = new ContentValues();
        cmd.fillInSwitchsContentValues(values);
        context.getContentResolver().update(YanKonProvider.URI_SWITCHS, values, where, null);
    }

    public static void sendCmdToLocalLight(Context context, Light light, Command cmd) {
        byte[] send_data;
        send_data = cmd.build(light.mac, light.ip, light.subIP);
        if (send_data != null)
            NetworkSenderService.sendCmd(context, light.ip, send_data);
    }

    /* switch */
    public static void sendCmdToLocalSwitch(Context context, Switchs s, Command cmd) {
        byte[] send_data;
        send_data = cmd.buildSwitch(s.mac, s.ip, s.subIP);
        if (send_data != null)
            NetworkSenderService.sendCmd(context, s.ip, send_data);
    }

    public static void controlScene(final Context context, final int scene_id, boolean closeAll, boolean doItNow) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(YanKonProvider.URI_SCENES_DETAIL, null, "scene_id=" + scene_id, null, "group_id desc");
        Command cmd = new Command(Command.CommandType.CommandTypeState, 0);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int light_id = cursor.getInt(cursor.getColumnIndex("light_id"));
                int group_id = cursor.getInt(cursor.getColumnIndex("group_id"));
                if (!closeAll) {
                    StatusInfo info = new StatusInfo();
                    info.brightness = cursor.getInt(cursor.getColumnIndex("brightness"));
                    info.CT = cursor.getInt(cursor.getColumnIndex("CT"));
                    info.color = cursor.getInt(cursor.getColumnIndex("color"));
                    info.state = cursor.getInt(cursor.getColumnIndex("state")) != 0;
                    info.mode = cursor.getInt(cursor.getColumnIndex("mode"));
                    cmd = new Command(info);
                }
                String[] lights = null;
                if (light_id > -1) {
                    lights = new String[]{String.valueOf(light_id)};
                } else if (group_id > -1) {
                    ArrayList<String> group_lights = new ArrayList<>();
                    Cursor c = context.getContentResolver().query(YanKonProvider.URI_LIGHT_GROUP_REL, null, "group_id=" + group_id, null, null);
                    if (c != null) {
                        while (c.moveToNext()) {
                            int group_l_id = c.getInt(c.getColumnIndex("light_id"));
                            group_lights.add(String.valueOf(group_l_id));
                        }
                        c.close();
                    }
                    lights = group_lights.toArray(new String[group_lights.size()]);
                }
                controlLightsById(context, lights, cmd, doItNow);
            }
            cursor.close();
        }
        ContentValues values = new ContentValues();
        values.put("last_used_time", System.currentTimeMillis());
        cr.update(YanKonProvider.URI_SCENES, values, "_id=" + scene_id, null);
    }
}
