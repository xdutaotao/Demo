package com.demo.step.Utils.Dagger.Component;

import android.support.annotation.NonNull;

import com.demo.step.App;
import com.demo.step.Utils.Dagger.Module.ActivityModule;
import com.demo.step.Utils.Dagger.Module.ApplicationModule;

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