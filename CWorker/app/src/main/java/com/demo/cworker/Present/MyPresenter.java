package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.LoginView;
import com.demo.cworker.View.MyView;

import javax.inject.Inject;

/**
 * Created by
 */
public class MyPresenter extends BasePresenter<MyView> {
    @Inject
    LoginModel model;

    @Inject
    MyPresenter() {
    }

    public void logout(Context context, String token){
        mCompositeSubscription.add(model.logout(token)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }
                }));
    }


    public void signToday(int gold, int exp){
        mCompositeSubscription.add(model.signToday(gold, exp)
                .subscribe(new RxSubUtils<UserInfo.PersonBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(UserInfo.PersonBean token) {
                        getView().signToday(token);
                    }
                }));
    }

}
