package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Model.LoginModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.Utils.ToastUtil;
import com.bz.fitness.View.CheckEmailActivityView;

import javax.inject.Inject;

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
                        ToastUtil.show("注册失败");
                    }
                }));
    }

    /**
     * 修改密码
     */
    public void changePwd(Context context, String phone, String pwd){
        mCompositeSubscription.add(model.changePwd(phone, pwd)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("没有此号码");
                    }
                }));
    }
}
