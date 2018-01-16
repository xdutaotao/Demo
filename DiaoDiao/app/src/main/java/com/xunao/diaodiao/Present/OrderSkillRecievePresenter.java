package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillRecieveView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderSkillRecievePresenter extends BasePresenter<OrderSkillRecieveView> {

    @Inject
    LoginModel model;

    @Inject
    OrderSkillRecievePresenter() {
    }

    public void mySkillWait(Context context, int page, int who){
        mCompositeSubscription.add(model.myAcceptOddWait(page, who)
                .subscribe(new RxSubUtils<OrderSkillRecieveRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(OrderSkillRecieveRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myAcceptOddCancel(Context context, int page, int who){
        mCompositeSubscription.add(model.myAcceptOddCancel(page, who)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().cancle(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
