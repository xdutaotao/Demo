package com.xunao.diaodiao.Utils.Dagger.Module;

import android.app.Application;

import com.xunao.diaodiao.Common.RetrofitConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    RetrofitConfig provideFreyRetrofit(){
        return RetrofitConfig.getInstance();
    }

}