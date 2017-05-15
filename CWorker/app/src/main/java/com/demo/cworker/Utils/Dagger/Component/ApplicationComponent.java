package com.demo.cworker.Utils.Dagger.Component;

import android.support.annotation.NonNull;

import com.demo.cworker.App;
import com.demo.cworker.Utils.Dagger.Module.ActivityModule;
import com.demo.cworker.Utils.Dagger.Module.ApplicationModule;

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