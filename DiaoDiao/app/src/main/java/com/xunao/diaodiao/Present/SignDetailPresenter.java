package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SignDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SignDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SignDetailPresenter extends BasePresenter<SignDetailView> {
    @Inject
    LoginModel model;

    @Inject
    SignDetailPresenter() {
    }

    //列表
    public void myAcceptProjectSignList(Context context, int req, int who){
        mCompositeSubscription.add(model.myAcceptProjectSignList(req, who)
                .subscribe(new RxSubUtils<SignRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SignRes token) {
                        getView().getList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myAcceptProjectSign(Context context, GetMoneyReq req){
        mCompositeSubscription.add(model.myAcceptProjectSign(req)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

}
