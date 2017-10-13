package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.PayFeeReq;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.PayModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.PayView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class PayPresenter extends BasePresenter<PayView> {
    @Inject
    LoginModel model;

    @Inject
    PayPresenter() {
    }

    /**
     * 支付
     * @param context
     * @param address
     */
    public void balancePay(Context context, PayFeeReq address){
        mCompositeSubscription.add(model.balancePay(address)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

    /**
     * 支付成功
     * @param context
     * @param address
     */
    public void paySuccess(Context context, PayFeeReq address){
        mCompositeSubscription.add(model.paySuccess(address)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().paySuccess(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

}
