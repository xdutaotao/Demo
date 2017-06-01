package com.yankon.smart.utils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.util.Xml;
import android.view.WindowManager;

import com.yankon.smart.App;
import com.yankon.smart.activities.AudioActivity;
import com.yankon.smart.music.MusicInfo;
import com.kii.cloud.storage.KiiUser;
import com.yankon.smart.model.Command;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.StatusInfo;
import com.yankon.smart.model.Switchs;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.services.NetworkSenderService;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tian on 14/11/25:上午9:00.
 */
public class Utils {
    public static final int LIGHTS_MODEL = 1;
    public static final int SWITCH_MODEL = 2;
    private static final String SSID_PWD = "SSID_PWD_";
    public static final String SSID_KEY = "SSID_KEY_";
    public static final String SSID_PWD_KEY = "SSID_PWD_KEY_";
    public static final String SSID_STATE = "SSID_STATE_";
    public static final String USE_TIME = "use_time";
    public static final String TIME_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android.txt";
    public static final String AV_LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AV_log.txt";
    public static final String IOT_LOG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IOT_log.txt";

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static long getNetworkTime(){
        URL url;
        long time = 0;

        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect(); // 发出连接
            long ld = uc.getDate(); // 取得网站日期时间
            Date date = new Date(ld); // 转换为标准时间对象
            time = date.getTime();
            LogUtils.i("TAG", time + "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void writeSdCard(String s){
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            try {
                File saveFile=new File(TIME_PATH);
                FileOutputStream outStream = new FileOutputStream(saveFile);
                outStream.write(s.getBytes());
                outStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }

    }

    public static String readSdCard(){
        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)){
            StringBuffer sb = new StringBuffer();
            try {
                File file = new File(TIME_PATH);
                if (file == null)
                    return null;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String readline = "";

                while ((readline = br.readLine()) != null) {
                    sb.append(readline);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        }else
            return null;

    }

    public static void saveUseTime(Context context, long pwd) {
        String key = USE_TIME;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        saveutils.putLong(key, pwd);
    }

    public static long getUseTime(Context context) {
        String key = USE_TIME;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        long time = saveutils.getLong(key);
        return time;
    }

    public static boolean isApkInstall(Context context, String apkPackageName){
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    apkPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();

        }
        if (packageInfo == null)
            return false;

        return true;
    }

    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
            //InputStream is = getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

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

    public static String intToIp2(int i) {
        if (i == 0)
            return "255.255.255.255";
        return  (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
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

        int i = 0;
        /* 通过UDP Socket 发送命令 */
        if (connectedLights.size() > 0) {
            for (Light light : connectedLights) {
                if (light.subIP != 0) {
                    sendCmdToLocalLight(context, light, cmd);
                }
            }
            for (Light light : connectedLights) {
                if (light.subIP == 0) {
                    sendCmdToLocalLight(context, light, cmd);
                }
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
        if (!"".equals(values.toString()))
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
                String key = c.getString(c.getColumnIndex(""));
                String deviceId = c.getString(c.getColumnIndex(""));
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
        if (send_data != null) {

            LogUtils.i("TAG", "send state cmd" + "-------" + byteArrayToString(send_data) + "-------" );
            NetworkSenderService.sendCmd(context, light.ip, send_data);
        }
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


    public static void savePass(Context context, String SSID, String pass) {
        // TODO Auto-generated method stub
        String key = SSID_PWD + SSID;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        saveutils.putString(key, pass);
    }
    public static String getPass(Context context, String SSID) {
        String key = SSID_PWD + SSID;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        String pass = saveutils.getString(key);
        return pass;
    }

    public static void saveSSID(Context context, String mac, String pass) {
        String key = mac + SSID_KEY;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        saveutils.putString(key, pass);
    }

    public static String getSSID(Context context, String mac) {
        String key = mac + SSID_KEY;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        String pass = saveutils.getString(key);
        return pass;
    }

    public static void saveSSIDPWD(Context context, String mac, String pwd) {
        String key = mac + SSID_PWD_KEY;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        saveutils.putString(key, pwd);
    }

    public static String getSSIDPWD(Context context, String mac) {
        String key = mac + SSID_PWD_KEY;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        String pass = saveutils.getString(key);
        return pass;
    }

    public static void saveSSIDState(Context context, String mac, boolean state) {
        String key = mac + SSID_STATE;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        saveutils.putBoolean(key, state);
    }

    public static boolean getSSIDState(Context context, String mac) {
        String key = mac + SSID_STATE;
        SharedPreferencesUtils saveutils = new SharedPreferencesUtils(context);
        boolean pass = saveutils.getBoolean(key);
        return pass;
    }

    public static String getUTF8XMLString(String xml) {
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }


    public static String getTimeFromInt(int time) {
        if (time <= 0) {
            return "0:00";
        }
        int secondnd = (time / 1000) / 60;
        int million = (time / 1000) % 60;
        String f = String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0"
                + String.valueOf(million);
        return f + ":" + m;
    }

    public static String getTimeFromInt2(int time) {
        if (time <= 0) {
            return "0:00";
        }
        int secondnd = (time / 1000) / 60;
        int million = (time / 1000) % 60;
        String f = String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0"
                + String.valueOf(million);
        return "0:"+f + ":" + m+".000";
    }

    public static String WriteXmlStr(MusicInfo info){
        if (info == null)
            return null;

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try{
            serializer.setOutput(writer);
			serializer.startDocument("UTF-8", false);
            serializer.startTag("", "DIDL-Lite");
            serializer.attribute("", "xmlns:dc", "http://purl.org/dc/elements/1.1/");
            serializer.attribute("", "xmlns:upnp", "urn:schemas-upnp-org:metadata-1-0/upnp/");
            serializer.attribute("", "xmlns", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/");

            serializer.startTag("", "item");
            serializer.attribute("", "id", String.valueOf(info.getId()));

            serializer.startTag("", "dc:title");
            serializer.text(info.getTitle());
            serializer.endTag("", "dc:title");

            serializer.startTag("", "dc:creator");
            serializer.text(info.getAuthor());
            serializer.endTag("", "dc:creator");

            serializer.startTag("", "upnp:class");
            serializer.text("object.item.audioItem.musicTrack");
            serializer.endTag("", "upnp:class");


            if(info != null){
                String str_len = getTimeFromInt2(Integer.valueOf(info.getLength()));
                serializer.startTag("", "res");
                serializer.attribute("", "protocolInfo", "http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;");
                serializer.attribute("", "duration", str_len);
                serializer.text(info.getSrc());
                serializer.endTag("", "res");
            }

            serializer.startTag("", "upnp:artist");
            serializer.text(info.getAuthor());
            serializer.endTag("", "upnp:artist");

            serializer.startTag("", "upnp:album");
            serializer.text(info.getTitle());
            serializer.endTag("", "upnp:album");

            serializer.startTag("", "upnp:albumArtURI");
            serializer.text(info.getCover());
            serializer.endTag("", "upnp:albumArtURI");

            serializer.endTag("", "item");
            serializer.endTag("", "DIDL-Lite");
            serializer.endDocument();
            return writer.toString();

        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    /* 将字符串转为时间戳 */
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    /* 将时间戳转为字符串 */
    public static String getStrTime(String cc_time) {
        String re_Strtime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 例如：cc_time = 1291778220 2010年12月08日11时17分00秒
        long lcc_time = Long.valueOf(cc_time);
        re_Strtime = sdf.format(new Date(lcc_time * 1000L));
        return re_Strtime;
    }

    public static String getSyTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new java.util.Date());
        return date;

    }



    public static String getFilePathFromUri(Context context){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int actual_audio_column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String audio_path = cursor.getString(actual_audio_column_index);
        cursor.close();
        String path = audio_path.substring(0, audio_path.lastIndexOf("/") + 1);
        return path;
    }

    public static void getAllFiles(String path){
        if (path == null)
            return;

        File root = new File(path);
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isDirectory()){
                    getAllFiles(f.getAbsolutePath());
                }else{
                    if (f.getAbsolutePath().endsWith(".mp3")){
                        String p = f.getAbsolutePath();
                        String s = p.substring(p.lastIndexOf("/") + 1, p.lastIndexOf("."));
                        MusicInfo info = new MusicInfo();
                        info.setId(s);
                        info.setTitle(s);
                        info.setAuthor("sunup");
                        info.setCover("sunup");
                        info.setSrc(p);
                        info.setLrc("sunup");
                        info.setSDimg("sunup");
                        info.setSDLRC("sunup");
                        MediaPlayer mMediaPlayer = new MediaPlayer();
                        try {
                            mMediaPlayer.setDataSource(p);
                            mMediaPlayer.prepare();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        info.setPlaytime(mMediaPlayer.getDuration());
                        info.setSDsrc(p);
                        info.setLength(mMediaPlayer.getDuration() + "");
//                        AudioActivity.musicList.add(info);
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                }
            }
        }
    }

    public static int StringToInt(String s){
        if (s == null)
            return -1;

        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        String ver =m.replaceAll("").trim();
        return Integer.valueOf(ver);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        System.out.println("len=" + len);
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static void copyAssets(Context pContext, String pAssetFilePath, String pDestDirPath) {
        AssetManager assetManager = pContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(pAssetFilePath);
            File outFile = new File(pDestDirPath, pAssetFilePath);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + pAssetFilePath, e);
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024*16];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getSystemProperty(String propName){
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)App.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()) {
            return true ;
        }
        return false ;
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!TextUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static Intent getAppOpenIntentByPackageName(Context context,String packageName){
        // MainActivity完整名
        String mainAct = null;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }
    public static boolean openPackage(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }

        return false;
    }
}
