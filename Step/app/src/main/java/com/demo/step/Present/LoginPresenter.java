package com.demo.step.Present;

import android.content.Context;

import com.demo.step.Model.LoginModel;
import com.demo.step.Utils.RxSubUtils;
import com.demo.step.Utils.ToastUtil;
import com.demo.step.View.LoginView;

import javax.inject.Inject;

/**
 * Created by
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    @Inject
    LoginModel model;

    @Inject
    LoginPresenter() {
    }

    public void login(Context context, String phone, String pwd, String id){
        mCompositeSubscription.add(model.login(phone, pwd, id)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("用户名或者密码错误");
                    }
                }));
    }

}
