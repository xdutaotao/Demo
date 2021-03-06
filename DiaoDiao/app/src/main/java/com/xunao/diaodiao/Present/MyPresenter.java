package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.MyView;

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

    public void getInfo(){
        mCompositeSubscription.add(model.getInfo()
                .subscribe(new RxSubUtils<MyBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyBean token) {
                        getView().getData(token);
                    }
                }));
    }

    public void getPersonalInfo(Context context){
        mCompositeSubscription.add(model.getPersonalInfo()
                .subscribe(new RxSubUtils<PersonalRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(PersonalRes token) {
                        getView().getPersonalData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }



}
