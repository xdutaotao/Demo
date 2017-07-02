package com.seafire.cworker.Utils.Dagger.Component;

import android.support.annotation.NonNull;

import com.seafire.cworker.App;
import com.seafire.cworker.Utils.Dagger.Module.ActivityModule;
import com.seafire.cworker.Utils.Dagger.Module.ApplicationModule;

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