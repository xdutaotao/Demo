package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.CheckEmailActivityModel;
import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.CheckEmailActivityView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CheckEmailActivityPresenter extends BasePresenter<CheckEmailActivityView> {
    @Inject
    LoginModel model;

    @Inject
    CheckEmailActivityPresenter() {}


    public void checkEmail(Context context, String email, String pwd, String phone, String name){
        mCompositeSubscription.add(model.checkEmail(email, pwd, phone, name)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        getView().onFailure();
                    }
                }));
    }
}
