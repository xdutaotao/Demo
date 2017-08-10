package com.seafire.cworker.Present;

import android.content.Context;

import com.seafire.cworker.Model.LoginModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.SuggestView;

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
