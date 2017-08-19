package com.demo.step.Present;

import com.demo.step.Bean.HomeResponseBean;
import com.demo.step.Model.HomeModel;
import com.demo.step.Utils.RxSubUtils;
import com.demo.step.Utils.ToastUtil;
import com.demo.step.View.HomeView;

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
