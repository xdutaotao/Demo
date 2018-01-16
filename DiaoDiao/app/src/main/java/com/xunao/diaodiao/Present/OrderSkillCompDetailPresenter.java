package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.OrderSkillCompDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillCompDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderSkillCompDetailPresenter extends BasePresenter<OrderSkillCompDetailView> {
    @Inject
    LoginModel model;

    @Inject
    OrderSkillCompDetailPresenter() {
    }

    public void mySkillProjDetail(Context context, int type){
        mCompositeSubscription.add(model.mySkillProjDetail(type)
                .subscribe(new RxSubUtils<SkillProjDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SkillProjDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
