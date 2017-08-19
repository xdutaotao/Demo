package com.demo.step.Present;

import android.content.Context;

import com.demo.step.Model.LoginModel;
import com.demo.step.Utils.RxSubUtils;
import com.demo.step.Utils.ToastUtil;
import com.demo.step.View.SuggestView;

import javax.inject.Inject;

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
