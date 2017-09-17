package com.xunao.diaodiao.Utils.Dagger.Component;


import com.xunao.diaodiao.Activity.AddBankActivity;
import com.xunao.diaodiao.Activity.AddressActivity;
import com.xunao.diaodiao.Activity.ApplyActivity;
import com.xunao.diaodiao.Activity.ApplyDetailActivity;
import com.xunao.diaodiao.Activity.BankActivity;
import com.xunao.diaodiao.Activity.BuyActivity;
import com.xunao.diaodiao.Activity.CheckEmailActivity;
import com.xunao.diaodiao.Activity.CollectActivity;
import com.xunao.diaodiao.Activity.ComplaintRecordActivity;
import com.xunao.diaodiao.Activity.ComponyPersonalActivity;
import com.xunao.diaodiao.Activity.DocActivity;
import com.xunao.diaodiao.Activity.DocDetailActivity;
import com.xunao.diaodiao.Activity.EditCompanyActivity;
import com.xunao.diaodiao.Activity.EditPersonalActivity;
import com.xunao.diaodiao.Activity.EditSkillActivity;
import com.xunao.diaodiao.Activity.FeedBackDetailActivity;
import com.xunao.diaodiao.Activity.FindProjectActivity;
import com.xunao.diaodiao.Activity.GetMoneyActivity;
import com.xunao.diaodiao.Activity.HelpDetailActivity;
import com.xunao.diaodiao.Activity.HomeDetailActivity;
import com.xunao.diaodiao.Activity.JoinActivity;
import com.xunao.diaodiao.Activity.JoinDetailActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.CheckPhoneActivity;
import com.xunao.diaodiao.Activity.MessageActivity;
import com.xunao.diaodiao.Activity.MoneyActivity;
import com.xunao.diaodiao.Activity.MyFavoriteActivity;
import com.xunao.diaodiao.Activity.MyRatingActivity;
import com.xunao.diaodiao.Activity.NormalPersonalActivity;
import com.xunao.diaodiao.Activity.OrderCompProjActivity;
import com.xunao.diaodiao.Activity.OrderCompProjDetailActivity;
import com.xunao.diaodiao.Activity.OrderProjProgressActivity;
import com.xunao.diaodiao.Activity.PDFActivity;
import com.xunao.diaodiao.Activity.PayActivity;
import com.xunao.diaodiao.Activity.PersonalActivity;
import com.xunao.diaodiao.Activity.ProjectDetailActivity;
import com.xunao.diaodiao.Activity.RecordDetailActivity;
import com.xunao.diaodiao.Activity.ReleaseCompanyActivity;
import com.xunao.diaodiao.Activity.ReleaseHelpActivity;
import com.xunao.diaodiao.Activity.ReleaseProjActivity;
import com.xunao.diaodiao.Activity.ReleaseProjSecondActivity;
import com.xunao.diaodiao.Activity.ReleaseProjThirdActivity;
import com.xunao.diaodiao.Activity.ReleaseSKillTypeActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillInforActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillSureInfoActivity;
import com.xunao.diaodiao.Activity.SearchResultActivity;
import com.xunao.diaodiao.Activity.SelectCompanyActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Activity.SelectNormalActivity;
import com.xunao.diaodiao.Activity.SelectSkillActivity;
import com.xunao.diaodiao.Activity.SettingActivity;
import com.xunao.diaodiao.Activity.SignDetailActivity;
import com.xunao.diaodiao.Activity.SuggestActivity;
import com.xunao.diaodiao.Fragment.AddFragment;
import com.xunao.diaodiao.Fragment.FriendFragment;
import com.xunao.diaodiao.Fragment.HasRateFragment;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.Fragment.MyComplaintRecordFragment;
import com.xunao.diaodiao.Fragment.NoRateFragment;
import com.xunao.diaodiao.Fragment.OrderCompTabFragment;
import com.xunao.diaodiao.Fragment.ProjectFragment;
import com.xunao.diaodiao.Fragment.MyFragment;
import com.xunao.diaodiao.Fragment.ReleaseFragment;
import com.xunao.diaodiao.Fragment.TabFragment;
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
    void inject(FeedBackDetailActivity activity);
    void inject(EditPersonalActivity activity);
    void inject(ComponyPersonalActivity activity);
    void inject(EditCompanyActivity activity);
    void inject(EditSkillActivity activity);
    void inject(NormalPersonalActivity activity);
    void inject(MoneyActivity activity);
    void inject(BankActivity activity);
    void inject(AddBankActivity activity);
    void inject(HelpDetailActivity activity);
    void inject(JoinActivity activity);
    void inject(DocDetailActivity activity);
    void inject(ProjectDetailActivity activity);
    void inject(JoinDetailActivity activity);
    void inject(ReleaseSkillActivity activity);
    void inject(ReleaseCompanyActivity activity);
    void inject(ReleaseProjActivity activity);
    void inject(ReleaseProjSecondActivity activity);
    void inject(ReleaseProjThirdActivity activity);
    void inject(PayActivity activity);
    void inject(ReleaseHelpActivity activity);
    void inject(ReleaseSkillInforActivity activity);
    void inject(ReleaseSkillSureInfoActivity activity);
    void inject(OrderCompProjActivity activity);
    void inject(OrderCompProjDetailActivity activity);
    void inject(OrderProjProgressActivity activity);
    void inject(SignDetailActivity activity);
    void inject(ApplyActivity activity);
    void inject(ApplyDetailActivity activity);
    void inject(TabFragment fragment);
    void inject(HasRateFragment fragment);
    void inject(NoRateFragment fragment);
    void inject(MyComplaintRecordFragment fragment);
    void inject(OrderCompTabFragment fragment);
    void inject(GetMoneyActivity activity);
    void inject(DocActivity activity);
    void inject(ReleaseSKillTypeActivity activity);
}