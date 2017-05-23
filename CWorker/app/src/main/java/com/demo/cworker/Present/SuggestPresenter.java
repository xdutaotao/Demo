package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Model.SuggestModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.SuggestView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SuggestPresenter extends BasePresenter<SuggestView> {
    @Inject
    LoginModel model;

    @Inject
    SuggestPresenter() {
    }

    public void submitSuggest(Context context, String phone, String content){
        mCompositeSubscription.add(model.submitSuggest(phone, content)
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
