package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Bean.SkillProjRecieveDetailRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillCompDetailView;
import com.xunao.diaodiao.View.OrderSkillCompRecieveDetailView;

import javax.inject.Inject;

/**
 * Created by
 */
public class OrderSkillCompRecieveDetailPresenter extends BasePresenter<OrderSkillCompRecieveDetailView> {
    @Inject
    LoginModel model;

    @Inject
    OrderSkillCompRecieveDetailPresenter() {
    }

    public void mySkillProjRecieveDetail(Context context, int type){
        mCompositeSubscription.add(model.mySkillProjRecieveDetail(type)
                .subscribe(new RxSubUtils<SkillProjRecieveDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SkillProjRecieveDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
