package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Model.AddBankModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddBankView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AddBankPresenter extends BasePresenter<AddBankView> {
    @Inject
    LoginModel model;

    @Inject
    AddBankPresenter() {
    }

    public void bindingCard(Context context, BindBankReq req){
        mCompositeSubscription.add(model.bindingCard(req)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
