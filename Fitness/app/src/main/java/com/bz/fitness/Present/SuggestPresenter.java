package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Model.LoginModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.Utils.ToastUtil;
import com.bz.fitness.View.SuggestView;

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
