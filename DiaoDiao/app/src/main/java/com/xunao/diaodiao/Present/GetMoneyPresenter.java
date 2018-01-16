package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.GetCashRes;
import com.xunao.diaodiao.Model.GetMoneyModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.GetMoneyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class GetMoneyPresenter extends BasePresenter<GetMoneyView> {
    @Inject
    LoginModel model;

    @Inject
    GetMoneyPresenter() {
    }

    public void applyCash(Context context, GetCashRes res){
        mCompositeSubscription.add(model.applyCash(res)
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

    /**
     * bank list
     * @param context
     * @param
     */
    public void getBankList(Context context){
        mCompositeSubscription.add(model.getBankList()
                .subscribe(new RxSubUtils<BankListRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(BankListRes token) {
                        getView().getBankList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
