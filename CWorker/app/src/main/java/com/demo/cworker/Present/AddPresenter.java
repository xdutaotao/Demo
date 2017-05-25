package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.AddModel;
import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.AddView;

import javax.inject.Inject;

import rx.Subscriber;

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
