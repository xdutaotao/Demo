package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.AppealModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.View.AppealView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AppealPresenter extends BasePresenter<AppealView> {
    @Inject
    LoginModel model;

    @Inject
    AppealPresenter() {
    }


}
