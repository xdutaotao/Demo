package com.demo.cworker;

import android.app.Application;
import android.content.Context;

import com.demo.cworker.Utils.Dagger.Component.ApplicationComponent;
import com.demo.cworker.Utils.Dagger.Component.DaggerApplicationComponent;
import com.demo.cworker.Utils.Dagger.Module.ApplicationModule;

import cn.smssdk.SMSSDK;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/15 11:16.
 */

public class App  extends Application{
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
        SMSSDK.initSDK(this, "1d79b47d1e280", "b4cfa85b04cfee6601067e1aa9b0b5d8");
    }
}
