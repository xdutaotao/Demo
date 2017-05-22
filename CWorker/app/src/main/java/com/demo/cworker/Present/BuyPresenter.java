package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.BuyView;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/22 19:54.
 */

public class BuyPresenter extends BasePresenter<BuyView> {
    @Inject
    LoginModel model;

    @Inject
    BuyPresenter(){}

    public void addVipDuration(Context context, long time, long gold){
        mCompositeSubscription.add(model.addVipDuration(time, gold)
                .subscribe(new RxSubUtils<UserInfo.PersonBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(UserInfo.PersonBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("请求失败");
                    }
                }));
    }
}
