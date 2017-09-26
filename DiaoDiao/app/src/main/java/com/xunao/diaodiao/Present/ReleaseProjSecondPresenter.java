package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseProjSecondModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseProjSecondView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjSecondPresenter extends BasePresenter<ReleaseProjSecondView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseProjSecondPresenter() {
    }


    public void typeExpenses(Context context, String address){
        mCompositeSubscription.add(model.typeExpenses(address)
                .subscribe(new RxSubUtils<ExpensesInfoRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ExpensesInfoRes token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }

}
