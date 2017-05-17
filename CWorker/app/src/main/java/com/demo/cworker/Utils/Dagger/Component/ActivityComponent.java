package com.demo.cworker.Utils.Dagger.Component;


import com.demo.cworker.Activity.CheckEmailActivity;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Activity.CheckPhoneActivity;
import com.demo.cworker.Fragment.MyFragment;
import com.demo.cworker.Fragment.SearchFragment;
import com.demo.cworker.Utils.Dagger.Module.ActivityModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ActivityScope
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(CheckPhoneActivity activity);
    void inject(CheckEmailActivity activity);
    void inject(SearchFragment fragment);
    void inject(MyFragment fragment);
}