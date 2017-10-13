package com.xunao.diaodiao.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;

import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.App;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by zhangerjiang on 2016/11/21.
 */

public class Utils {

    // IMEI码
    public static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String deviceId = tm.getDeviceId();
            return deviceId;
        }else{
            return null;
        }
    }

    public static int dipToPx(Context c,float dipValue) {
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    private final static  int SIZE = 1000000;
    /**
     * 拨号：激活系统的拨号组件
     *
     * @param context
     * @param number
     */
    public static void startCallActivity(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 启动登录页面
     */
    public static void startLoginActivity() {
            Intent intent = new Intent(App.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getContext().startActivity(intent);
    }

    /**
     * 通知栏 点击启动 H5页面
     * @param mContext
     * @param url
     */
    public static void startH5Activity(Context mContext, String url) {
        Intent intent = null;
        intent.putExtra("intent_url",url);
        intent.putExtra("intent_type", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static String subZeroAnd6Dot(double f){
        String s = "";
        if(f < 1){
            s = f+"";
        }else{
            DecimalFormat df = new DecimalFormat("#0.000000");
            double temp = f*SIZE;
            double result = new BigDecimal(temp).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue()/SIZE;
            s = df.format(result);
        }
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static String subZeroAnd6DotNew(double f){
        String s = "";
            DecimalFormat df = new DecimalFormat("#0.000000");
            double temp = f*SIZE;
            double result = new BigDecimal(temp).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue()/SIZE;
            s = df.format(result);
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static String double2TwoDecimal(double f) {
//        DecimalFormat df = new DecimalFormat("#0.00");
//         if(f < 1){
//             double temp = 100*f;
//             double result =  new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()/100;
//            return df.format(result);
//        }
//        return df.format(new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            DecimalFormat df = new DecimalFormat("#0.00");
            double temp = 100*f;
            double result =  new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()/100;
            return df.format(result);
    }

    /**
     * 判断集合是否为空
     *
     * @param list 待判断的集合
     * @return 是否为空
     */
    public static boolean isEmpty(List list) {
        if (null == list || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param date 时间字符串
     * @return XX天前  xx小时前
     */
    public static String formatDate(String date) {
        if(TextUtils.isEmpty(date)){
            return "-";
        }
        SimpleDateFormat format = null;
        if(date.contains("-")){
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else{
            format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        }
        Date currentTime = new Date();
        try {
            Date date1 = format.parse(date);
            int temDate_d = (int) Math.floor((currentTime.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24);
            if (date1.getDay()==currentTime.getDay()&&temDate_d<=0) {
//                return temDate_d + "天前";
                return String.format("%02d",date1.getHours())+":"+String.format("%02d",date1.getMinutes());
            } else  {
                return date.substring(0,10);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 金额加逗号
     * @param number 123456.00
     * @return demo,456.00
     */
    public static String moneyNumberFormat(double number) {
        if(number < 1){
            return number+"";
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(number);
    }

    /**
     * 格式：2016/12/13
     * @return
     */
    public static String getNowDate(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date(System.currentTimeMillis()));
        return date;
    }

    /**
     * 格式：2016/12/13
     * @return
     */
    public static String getNowDateMonth(){
        SimpleDateFormat sd = new SimpleDateFormat("MM-dd");
        String date = sd.format(new Date(System.currentTimeMillis()));
        return date;
    }

    public static String getNowDateMonth(long time){
        SimpleDateFormat sd = new SimpleDateFormat("MM-dd");
        String date = sd.format(new Date(time));
        return date;
    }

    /**
     * 格式：2016/12/13
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sd.format(new Date(System.currentTimeMillis()));
        return date;
    }


    /**
     * 格式：2016/12/13
     * @return
     */
    public static boolean isZeroTime(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sd.format(new Date(System.currentTimeMillis()));
        String[] strings = date.split(" ");
        if (TextUtils.equals(strings[1], "00:00:00")){
            return true;
        }
        return false;
    }

    /**
     * 将日期格式的字符串转换为长整型
     * @param date
     * @return
     */
    public static long convert2long(String date) {
        try {
            if (!TextUtils.isEmpty(date)) {
                String format = "yyyy-MM-dd HH:mm";
                SimpleDateFormat sf = new SimpleDateFormat(format);
                return sf.parse(date).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 将长整型格式转 日期格式
     * @param time
     * @return
     */
    public static String strToDateLong(long time) {
        Date date = new Date(time*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = formatter.format(date);
        String[] results = result.split(" ");
        if (TextUtils.equals(results[0], getNowDate())){
            results[0] = "今天";
        }
        return results[0]+" "+results[1];
    }

    public static String millToDateString(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = formatter.format(date);
        String[] results = result.split(" ");
        if (TextUtils.equals(results[0], getNowDate())){
            results[0] = "今天";
        }
        return results[0]+" "+results[1];
    }

    public static String millToYearString(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String result = formatter.format(date);
        return result;
    }

    /**
     * 获取版本号
     * @return
     */
    public static String getVersionCode() {
        String versionCode = null;
        try {
            versionCode = App
                    .getContext()
                    .getPackageManager()
                    .getPackageInfo(App.getContext().getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {

        }
        return versionCode;
    }

    /**
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        //执行动作
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return int 宽或高
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * String  转 UTF-8
     * @param xml
     * @return
     */
    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    /**
     * 取得两个时间段的时间间隔
     * return t2 与t1的间隔天数
     * throws ParseException 如果输入的日期格式不是0000-00-00 格式抛出异常
     */
    public static int getBetweenDays(String t1,String t2) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int betweenDays = 0;
        Date d1 = format.parse(t1);
        Date d2 = format.parse(t2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if(c1.after(c2)){
            c1 = c2;
            c2.setTime(d1);
        }
        int betweenYears = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
        for(int i=0;i<betweenYears;i++){
            int tmp=countDays(c1.get(Calendar.YEAR));
            betweenDays+=countDays(c1.get(Calendar.YEAR));
            c1.set(Calendar.YEAR,(c1.get(Calendar.YEAR)+1));
        }
        return betweenDays;
    }
    public static int countDays(int year){
        int n=0;
        for (int i = 1; i <= 12; i++) {
            n += countDays(i,year);
        }
        return n;
    }


    public static int countDays(int month, int year){
        int count = -1;
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                count = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                count = 30;
                break;
            case 2:
                if(year % 4 == 0)
                    count = 29;
                else
                    count = 28;
                if((year % 100 ==0) & (year % 400 != 0))
                    count = 28;
        }
        return count;
    }


    /**
     * 判断是否是  小米手机
     */
    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static boolean isMIUI() {
        Properties prop= new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI= prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        return isMIUI;
    }

    /**
     * 判断是否是  华为手机
     */
    // 检测MIUI
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
    public static boolean isHuaWei() {
        Properties prop= new Properties();
        boolean isHuaWei;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isHuaWei= prop.getProperty(KEY_EMUI_VERSION, null) != null
                || prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null;
        return isHuaWei;
    }


    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static final int APP_NO_START = 3;
    public static final int APP_START_BACK = 2;
    public static final int APP_START_FORE = 1;
    public static int getAppStatus(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);
        String pageName = getAppProcessName(context);
        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return APP_START_FORE;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return APP_START_BACK;
                }
            }
            return APP_NO_START;//栈里找不到，返回3
        }
    }

    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    private static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 启动APP 进入MainActivity，并且传入参数
     * @param context
     * @param appPackageName
     * @param data
     */
    public static final String MAIN_INTENT_KEY = "main_intent_key";
    public static void startAPP(Context context, String appPackageName, String data){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            intent.putExtra(MAIN_INTENT_KEY, data);
            context.startActivity(intent);
        }catch(Exception e){

        }
    }


    /**
     * 时间戳 转 Date 字符串
     *  例如：cc_time=1291778220
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos( final View contentView) {
        final int windowPos[] = new int[2];

        // 获取屏幕的高宽
        final int screenHeight = getScreenHeight(contentView.getContext());
        final int screenWidth = getScreenWidth(contentView.getContext());
        contentView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();

        windowPos[0] = (screenWidth - windowWidth)/2;
        windowPos[1] = (screenHeight - windowHeight)/2;

        return windowPos;
    }

    /**
     * 截取字符串 去掉后面单位
     */
    public static float subText(@NonNull String s){
        if (s.length() < 3){
            return 0;
        }

        int length = s.length();
        return Float.valueOf(s.substring(0, length-2));
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 读取xlsx
     * @param path
     * @return
     */
    public static String readXLSX(String path) {
        String str = "";
        String v = null;
        boolean flat = false;
        List<String> ls = new ArrayList<String>();
        try {
            ZipFile xlsxFile = new ZipFile(new File(path));
            ZipEntry sharedStringXML = xlsxFile
                    .getEntry("xl/sharedStrings.xml");
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        if (tag.equalsIgnoreCase("t")) {
                            ls.add(xmlParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet1.xml");
            InputStream inputStreamsheet = xlsxFile.getInputStream(sheetXML);
            XmlPullParser xmlParsersheet = Xml.newPullParser();
            xmlParsersheet.setInput(inputStreamsheet, "utf-8");
            int evtTypesheet = xmlParsersheet.getEventType();
            while (evtTypesheet != XmlPullParser.END_DOCUMENT) {
                switch (evtTypesheet) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParsersheet.getName();
                        if (tag.equalsIgnoreCase("row")) {
                        } else if (tag.equalsIgnoreCase("c")) {
                            String t = xmlParsersheet.getAttributeValue(null, "t");
                            if (t != null) {
                                flat = true;
                                System.out.println(flat + "有");
                            } else {
                                System.out.println(flat + "没有");
                                flat = false;
                            }
                        } else if (tag.equalsIgnoreCase("v")) {
                            v = xmlParsersheet.nextText();
                            if (v != null) {
                                if (flat) {
                                    str += ls.get(Integer.parseInt(v)) + "  ";
                                } else {
                                    str += v + "  ";
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParsersheet.getName().equalsIgnoreCase("row")
                                && v != null) {
                            str += "\n";
                        }
                        break;
                }
                evtTypesheet = xmlParsersheet.next();
            }
            System.out.println(str);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if (str == null) {
            str = "解析文件出现问题";
        }

        return str;
    }

    /*
    * 将时间转换为时间戳
    */
    public static long dateToStamp(String s) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    public static boolean isActiveTime(){
        boolean isActive = false;
        try {
            long startTime = dateToStamp("2017-10-15 08:30:00");
            if (startTime < System.currentTimeMillis()){
                isActive = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isActive;

    }

    /**
     * float 转 int String
     * @param d
     * @return
     */
    public static String formatData(float d){
        String data = String.valueOf(d);
        if (!TextUtils.isEmpty(data) && data.contains(".")){
            int length = data.length();
            while(data.length() > 1 &&
                    (TextUtils.equals("0", data.substring(length-1, length)) || TextUtils.equals(".", data.substring(length-1, length)))){
                data = data.substring(0, length-1);
                length = data.length();
            }
        }
        return data;
    }


    /**
     * 获取MD5加密
     *
     * @param pwd
     *            需要加密的字符串
     * @return String字符串 加密后的字符串
     */
    public static String getMD5(String pwd) {
        try {
            // 创建加密对象
            MessageDigest digest = MessageDigest.getInstance("md5");

            // 调用加密对象的方法，加密的动作已经完成
            byte[] bs = digest.digest(pwd.getBytes());
            // 接下来，我们要对加密后的结果，进行优化，按照mysql的优化思路走
            // mysql的优化思路：
            // 第一步，将数据全部转换成正数：
            String hexString = "";
            for (byte b : bs) {
                // 第一步，将数据全部转换成正数：
                // 解释：为什么采用b&255
                /*
                 * b:它本来是一个byte类型的数据(1个字节) 255：是一个int类型的数据(4个字节)
                 * byte类型的数据与int类型的数据进行运算，会自动类型提升为int类型 eg: b: 1001 1100(原始数据)
                 * 运算时： b: 0000 0000 0000 0000 0000 0000 1001 1100 255: 0000
                 * 0000 0000 0000 0000 0000 1111 1111 结果：0000 0000 0000 0000
                 * 0000 0000 1001 1100 此时的temp是一个int类型的整数
                 */
                int temp = b & 255;
                // 第二步，将所有的数据转换成16进制的形式
                // 注意：转换的时候注意if正数>=0&&<16，那么如果使用Integer.toHexString()，可能会造成缺少位数
                // 因此，需要对temp进行判断
                if (temp < 16 && temp >= 0) {
                    // 手动补上一个“0”
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 通过Base32将Bitmap转换成Base64字符串
     * @param
     * @return
     */
    public static String Bitmap2StrByBase64(@NonNull String path){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bit = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return "data:image/png;base64,"+Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String floatToString(float data){
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(data);//format 返回的是字符串
        return p;
    }

    public static String stringToPinyin(@NonNull String name){
        StringBuilder sb = new StringBuilder();
        char[] pins = name.toCharArray();
        for(int i=0; i<name.length(); i++){
            String pinyin = PinyinHelper.toHanyuPinyinStringArray(pins[i])[0];
            if(Character.isDigit(pinyin.charAt(pinyin.length()-1))){
                pinyin = pinyin.substring(0, pinyin.length()-1);
            }
            sb.append(pinyin);
        }
        String result =  sb.toString();
        return result;
    }


}