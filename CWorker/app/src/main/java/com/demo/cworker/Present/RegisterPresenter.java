package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.LoginView;
import com.demo.cworker.View.RegisterView;

import javax.inject.Inject;

/**
 * Created by
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
    @Inject
    LoginModel model;

    @Inject
    RegisterPresenter() {
    }

    public void checkPhone(Context context, String name){
        mCompositeSubscription.add(model.checkPhone(name)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("手机号已经注册");
                    }
                }));
    }

    /**
     * 修改密码 检查手机号是否存在
     */
    public void changePwd(Context context, String phone){
        mCompositeSubscription.add(model.checkExistPhone(phone)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("手机号没有注册");
                    }
                }));
    }
}
