package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.HomeSearchRes;
import com.xunao.diaodiao.Model.HomeSearchModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.HomeSearchView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class HomeSearchPresenter extends BasePresenter<HomeSearchView> {
    @Inject
    LoginModel model;

    @Inject
    HomeSearchPresenter() {
    }


    public void indexSearch(Context context, FindProjReq req){
        mCompositeSubscription.add(model.indexSearch(req)
                .subscribe(new RxSubUtils<HomeSearchRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(HomeSearchRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
