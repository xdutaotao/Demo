package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Model.CashRecordModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.CashRecordView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CashRecordPresenter extends BasePresenter<CashRecordView> {
    @Inject
    LoginModel model;

    @Inject
    CashRecordPresenter() {
    }

    public void cashRecord(Context context, int page){
        mCompositeSubscription.add(model.cashRecord(page)
                .subscribe(new RxSubUtils<CashRecordRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(CashRecordRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
