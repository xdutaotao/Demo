package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.MoneyModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MoneyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MoneyPresenter extends BasePresenter<MoneyView> {
    @Inject
    LoginModel model;

    @Inject
    MoneyPresenter() {
    }

    public void getMoney(Context context){
        mCompositeSubscription.add(model.getMoney()
                .subscribe(new RxSubUtils<GetMoneyRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(GetMoneyRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
