package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Model.LoginModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.View.AddView;

import javax.inject.Inject;

/**
 * Created by
 */
public class AddPresenter extends BasePresenter<AddView> {
    @Inject
    LoginModel model;

    @Inject
    AddPresenter() {
    }

    public void changeAddress(Context context, String address){
        mCompositeSubscription.add(model.changeAddress(address)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }
                }));
    }
}
