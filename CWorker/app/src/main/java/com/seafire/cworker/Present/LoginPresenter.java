package com.seafire.cworker.Present;

import android.content.Context;
import android.text.TextUtils;

import com.seafire.cworker.Model.LoginModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.LoginView;

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
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误")){
                            s = "用户名或者密码错误";
                        }
                        ToastUtil.show(s);
                    }
                }));
    }

}
