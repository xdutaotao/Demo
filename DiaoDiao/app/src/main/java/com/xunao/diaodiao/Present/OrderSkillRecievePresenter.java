package com.xunao.diaodiao.Present;

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

    public void mySkillWait(int page, int who){
        mCompositeSubscription.add(model.myAcceptOddWait(page, who)
                .subscribe(new RxSubUtils<OrderSkillRecieveRes>(mCompositeSubscription) {
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

    public void myAcceptOddCancel(int page, int who){
        mCompositeSubscription.add(model.myAcceptOddCancel(page, who)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().cancle(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
