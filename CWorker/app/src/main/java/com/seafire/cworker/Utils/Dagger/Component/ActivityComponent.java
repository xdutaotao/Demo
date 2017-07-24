package com.seafire.cworker.Utils.Dagger.Component;


import com.seafire.cworker.Activity.AddressActivity;
import com.seafire.cworker.Activity.BuyActivity;
import com.seafire.cworker.Activity.CheckEmailActivity;
import com.seafire.cworker.Activity.CollectActivity;
import com.seafire.cworker.Activity.HomeDetailActivity;
import com.seafire.cworker.Activity.LoginActivity;
import com.seafire.cworker.Activity.CheckPhoneActivity;
import com.seafire.cworker.Activity.PDFActivity;
import com.seafire.cworker.Activity.PersonalActivity;
import com.seafire.cworker.Activity.SearchResultActivity;
import com.seafire.cworker.Activity.SettingActivity;
import com.seafire.cworker.Activity.SuggestActivity;
import com.seafire.cworker.Fragment.AddFragment;
import com.seafire.cworker.Fragment.FriendFragment;
import com.seafire.cworker.Fragment.HomeFragment;
import com.seafire.cworker.Fragment.MessageFragment;
import com.seafire.cworker.Fragment.MyFragment;
import com.seafire.cworker.Fragment.SearchFragment;
import com.seafire.cworker.Utils.Dagger.Module.ActivityModule;

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
    void inject(PersonalActivity activity);
    void inject(FriendFragment fragment);
}