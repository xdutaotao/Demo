package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseCompanyModel;
import com.xunao.diaodiao.View.ReleaseCompanyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseCompanyPresenter extends BasePresenter<ReleaseCompanyView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseCompanyPresenter() {
    }
}
