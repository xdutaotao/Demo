package com.xunao.diaodiao.Utils.Dagger.Component;

import android.support.annotation.NonNull;

import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Utils.Dagger.Module.ActivityModule;
import com.xunao.diaodiao.Utils.Dagger.Module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    @NonNull
    ActivityComponent plus(@NonNull ActivityModule activityModule);
    @NonNull

    void inject(@NonNull App app);

}