package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Model.HomeModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.HomeView;

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

    public void getFirstPage(String lat, String lng){
        mCompositeSubscription.add(model.getFirstPage(lat, lng)
                .subscribe(new RxSubUtils<HomeResponseBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(HomeResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "请求失败";
                        ToastUtil.show(s);
                        getView().onFailure();
                    }
                }));
    }

    public void getFirstPage(Context context, String lat, String lng){
        mCompositeSubscription.add(model.getFirstPage(lat, lng)
                .subscribe(new RxSubUtils<HomeResponseBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(HomeResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "请求失败";
                        ToastUtil.show(s);
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
