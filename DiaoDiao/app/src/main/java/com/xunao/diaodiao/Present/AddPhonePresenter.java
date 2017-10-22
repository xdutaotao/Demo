package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.WeiXinReq;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.UserInfo;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddPhoneView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AddPhonePresenter extends BasePresenter<AddPhoneView> {
    @Inject
    LoginModel model;

    @Inject
    AddPhonePresenter() {
    }

    public void checkPhone(Context context, String name){
        mCompositeSubscription.add(model.wxPhone(name)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    public void wxPhone(Context context, WeiXinReq name){
        mCompositeSubscription.add(model.wxPhone(name)
                .subscribe(new RxSubUtils<UserInfo>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(UserInfo token) {
                        getView().getRes(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
