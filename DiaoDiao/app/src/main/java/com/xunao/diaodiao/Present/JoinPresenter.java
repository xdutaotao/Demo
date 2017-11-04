package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.JoinModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.JoinView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class JoinPresenter extends BasePresenter<JoinView> {
    @Inject
    LoginModel model;

    @Inject
    JoinPresenter() {
    }

    public void businesses(Context context, FindProjReq req){
        mCompositeSubscription.add(model.businesses(req)
                .subscribe(new RxSubUtils<FindProjectRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(FindProjectRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }



}
