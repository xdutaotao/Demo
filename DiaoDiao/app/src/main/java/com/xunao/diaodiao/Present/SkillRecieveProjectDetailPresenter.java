package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.SkillRecieveProjDetailRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SkillRecieveProjectDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SkillRecieveProjectDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SkillRecieveProjectDetailPresenter extends BasePresenter<SkillRecieveProjectDetailView> {
    @Inject
    LoginModel model;

    @Inject
    SkillRecieveProjectDetailPresenter() {
    }

    public void myAcceptProjectDetail(Context context, int req){
        mCompositeSubscription.add(model.myAcceptProjectDetail(req)
                .subscribe(new RxSubUtils<SkillRecieveProjDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SkillRecieveProjDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
