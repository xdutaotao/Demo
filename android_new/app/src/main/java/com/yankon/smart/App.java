package com.yankon.smart;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;

import java.util.List;

/**
 * Created by  on 14/11/20:上午11:30.
 */
public class App extends Application {
    private static App mApp;
    private static Context context;
    private RefWatcher refWatcher;

    public static App getApp() {
        return mApp;
    }

    public static Context getContext(){
        return context;
    }

    public static RefWatcher getRefWatcher(){
        return mApp.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        context = getApplicationContext();
        ApplicationInfo info = getApplicationInfo();
        if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            refWatcher = LeakCanary.install(this);
            LogUtils.LEVEL = 0;
        }
        else {
            refWatcher = RefWatcher.DISABLED;
            LogUtils.LEVEL = 6;
        }
        context = getApplicationContext();
        Global.gDaemonHandler = new DaemonHandler(getApplicationContext());
        Global.init(this);
        Utils.setAllLightsOffline(this);
        //Utils.setAllSwitchsOffline(this);

        /* Move to AndroidManifest.xml
        mWifiStateReceiver = new WifiStateReceiver();
        IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mWifiStateReceiver, filters);
        */
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected()) {
            return true ;
        }
        return false ;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
