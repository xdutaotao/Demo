package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.LoginView;

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

    public void login(Context context, String phone, String pwd){
        mCompositeSubscription.add(model.login(phone, pwd)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
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
