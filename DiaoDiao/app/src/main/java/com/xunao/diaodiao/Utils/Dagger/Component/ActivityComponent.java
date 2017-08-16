package com.xunao.diaodiao.Utils.Dagger.Component;


import com.xunao.diaodiao.Activity.AddressActivity;
import com.xunao.diaodiao.Activity.BuyActivity;
import com.xunao.diaodiao.Activity.CheckEmailActivity;
import com.xunao.diaodiao.Activity.CollectActivity;
import com.xunao.diaodiao.Activity.ComplaintRecordActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.HomeDetailActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.CheckPhoneActivity;
import com.xunao.diaodiao.Activity.MessageActivity;
import com.xunao.diaodiao.Activity.MyFavoriteActivity;
import com.xunao.diaodiao.Activity.MyRatingActivity;
import com.xunao.diaodiao.Activity.PDFActivity;
import com.xunao.diaodiao.Activity.PersonalActivity;
import com.xunao.diaodiao.Activity.RecordDetailActivity;
import com.xunao.diaodiao.Activity.SearchResultActivity;
import com.xunao.diaodiao.Activity.SelectCompanyActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Activity.SelectNormalActivity;
import com.xunao.diaodiao.Activity.SelectSkillActivity;
import com.xunao.diaodiao.Activity.SettingActivity;
import com.xunao.diaodiao.Activity.SuggestActivity;
import com.xunao.diaodiao.Fragment.AddFragment;
import com.xunao.diaodiao.Fragment.FriendFragment;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.Fragment.ProjectFragment;
import com.xunao.diaodiao.Fragment.MyFragment;
import com.xunao.diaodiao.Fragment.ReleaseFragment;
import com.xunao.diaodiao.Utils.Dagger.Module.ActivityModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ActivityScope
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(CheckPhoneActivity activity);
    void inject(CheckEmailActivity activity);
    void inject(ReleaseFragment fragment);
    void inject(MyFragment fragment);
    void inject(HomeFragment fragment);
    void inject(AddFragment fragment);
    void inject(ProjectFragment fragment);
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
    void inject(SelectCompanyActivity activity);
    void inject(SelectSkillActivity activity);
    void inject(SelectNormalActivity activity);
    void inject(SelectMemoryActivity activity);
    void inject(FindProjectActivity activity);
    void inject(MyFavoriteActivity activity);
    void inject(MyRatingActivity activity);
    void inject(ComplaintRecordActivity activity);
    void inject(RecordDetailActivity activity);
    void inject(MessageActivity activity);
}