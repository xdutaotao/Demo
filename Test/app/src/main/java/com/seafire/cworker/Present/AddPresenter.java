package com.seafire.cworker.Present;

import android.content.Context;

import com.seafire.cworker.Model.LoginModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.AddView;

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

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }
}
