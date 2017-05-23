package com.demo.cworker.Utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.App;
import com.zhy.autolayout.utils.ScreenUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by zhangerjiang on 2016/11/21.
 */

public class Utils {

    // IMEI码
    public static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
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
     * @param mContext
     */
    public static void startLoginActivity(Context mContext) {
            Intent intent = new Intent(mContext, LoginActivity.class);
//            intent.putExtra("login_activity", Constants.UPDATE_UI);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
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
     * @return 123,456.00
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
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String date = sd.format(new Date(System.currentTimeMillis()));
        return date;
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



}