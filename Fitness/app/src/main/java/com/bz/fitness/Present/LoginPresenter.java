package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Model.LoginModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.Utils.ToastUtil;
import com.bz.fitness.View.LoginView;

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
