package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.Model.HomeModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.HomeView;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:45.
 */

public class HomePresenter extends BasePresenter<HomeView> {
    @Inject
    HomeModel model;

    @Inject
    HomePresenter() {}

    public void getFirstPage(){
        mCompositeSubscription.add(model.getFirstPage()
                .subscribe(new RxSubUtils<HomeResponseBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(HomeResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("请求失败");
                        getView().onFailure();
                    }
                }));
    }

    public void checkToken(){
        mCompositeSubscription.add(model.checkToken()
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getTokenResult(token);
                    }
                }));
    }
}
