package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Model.BankModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.BankView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class BankPresenter extends BasePresenter<BankView> {
    @Inject
    LoginModel model;

    @Inject
    BankPresenter() {
    }

    public void getBankList(Context context){
        mCompositeSubscription.add(model.getBankList()
                .subscribe(new RxSubUtils<BankListRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(BankListRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
