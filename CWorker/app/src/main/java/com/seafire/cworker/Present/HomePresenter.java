package com.seafire.cworker.Present;

import android.text.TextUtils;

import com.seafire.cworker.Activity.LoginActivity;
import com.seafire.cworker.Bean.HomeResponseBean;
import com.seafire.cworker.Model.HomeModel;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Utils.Utils;
import com.seafire.cworker.View.HomeView;

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

                    @Override
                    protected void _onError(String msg) {
                        super._onError(msg);
                        User.getInstance().clearUser();
                        Utils.startLoginActivity();
                    }
                }));
    }
}
