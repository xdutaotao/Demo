package com.seafire.cworker.Present;

import android.content.Context;

import com.seafire.cworker.Model.LoginModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.BuyView;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/22 19:54.
 */

public class BuyPresenter extends BasePresenter<BuyView> {
    @Inject
    LoginModel model;

    @Inject
    BuyPresenter(){}

    public void addVipDuration(Context context, long time, int gold){
        mCompositeSubscription.add(model.addVipDuration(time, gold)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
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
