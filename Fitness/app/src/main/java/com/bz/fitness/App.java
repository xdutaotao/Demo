package com.bz.fitness;

import android.app.Application;
import android.content.Context;

import com.bz.fitness.Utils.Dagger.Component.ApplicationComponent;
import com.bz.fitness.Utils.Dagger.Component.DaggerApplicationComponent;
import com.bz.fitness.Utils.Dagger.Module.ApplicationModule;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/15 11:16.
 */

public class App extends android.support.multidex.MultiDexApplication{
    private static App mAPPLike;
    private static Application mApplication;
    private static Context mContext;
    private ApplicationComponent applicationComponent;

    public ApplicationComponent getComponent(){
        if (applicationComponent == null){
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(mApplication))
                    .build();
        }
        return applicationComponent;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public static Application getApplicationInstance(){
        return mApplication;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        CrashReport.initCrashReport(getApplicationContext(), "9ce26c6dd7", false);
    }
}
