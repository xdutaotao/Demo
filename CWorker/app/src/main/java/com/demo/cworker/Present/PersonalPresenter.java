package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.PersonalView;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/23 17:24.
 */

public class PersonalPresenter extends BasePresenter<PersonalView> {
    @Inject
    LoginModel model;

    @Inject
    PersonalPresenter() {
    }

    public void changeHeadIcon(Context context, String path){
        mCompositeSubscription.add(model.changeHeadIcon(path)
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

    public void changeSex(Context context, int sex){
        mCompositeSubscription.add(model.changeSex(sex)
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