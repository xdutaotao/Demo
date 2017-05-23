package com.demo.cworker.Utils.Dagger.Component;


import com.demo.cworker.Activity.AddressActivity;
import com.demo.cworker.Activity.BuyActivity;
import com.demo.cworker.Activity.CheckEmailActivity;
import com.demo.cworker.Activity.CollectActivity;
import com.demo.cworker.Activity.HomeDetailActivity;
import com.demo.cworker.Activity.LoginActivity;
import com.demo.cworker.Activity.CheckPhoneActivity;
import com.demo.cworker.Activity.PDFActivity;
import com.demo.cworker.Activity.SearchResultActivity;
import com.demo.cworker.Activity.SettingActivity;
import com.demo.cworker.Activity.SuggestActivity;
import com.demo.cworker.Fragment.AddFragment;
import com.demo.cworker.Fragment.HomeFragment;
import com.demo.cworker.Fragment.MessageFragment;
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
    void inject(HomeFragment fragment);
    void inject(AddFragment fragment);
    void inject(MessageFragment fragment);
    void inject(PDFActivity activity);
    void inject(HomeDetailActivity activity);
    void inject(SearchResultActivity activity);
    void inject(CollectActivity activity);
    void inject(BuyActivity activity);
    void inject(AddressActivity activity);
    void inject(SettingActivity activity);
    void inject(SuggestActivity activity);
}