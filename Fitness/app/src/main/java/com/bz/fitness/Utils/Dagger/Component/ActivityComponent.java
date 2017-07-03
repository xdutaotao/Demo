package com.bz.fitness.Utils.Dagger.Component;

import com.bz.fitness.Activity.CheckEmailActivity;
import com.bz.fitness.Activity.CheckPhoneActivity;
import com.bz.fitness.Activity.LoginActivity;
import com.bz.fitness.Utils.Dagger.Module.ActivityModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ActivityScope
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(CheckPhoneActivity activity);
    void inject(CheckEmailActivity activity);
}