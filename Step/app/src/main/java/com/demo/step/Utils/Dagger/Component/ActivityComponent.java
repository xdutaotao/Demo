package com.demo.step.Utils.Dagger.Component;

import com.demo.step.Activity.CheckEmailActivity;
import com.demo.step.Activity.CheckPhoneActivity;
import com.demo.step.Activity.LoginActivity;
import com.demo.step.Utils.Dagger.Module.ActivityModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ActivityScope
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(CheckPhoneActivity activity);
    void inject(CheckEmailActivity activity);
}