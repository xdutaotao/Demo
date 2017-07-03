package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Model.LoginModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.View.MyView;

import javax.inject.Inject;

/**
 * Created by
 */
public class MyPresenter extends BasePresenter<MyView> {
    @Inject
    LoginModel model;

    @Inject
    MyPresenter() {
    }

    public void logout(Context context, String token){
        mCompositeSubscription.add(model.logout(token)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }
                }));
    }


    public void signToday(int gold, int exp){
        mCompositeSubscription.add(model.signToday(gold, exp)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().signToday(token);
                    }
                }));
    }

}
