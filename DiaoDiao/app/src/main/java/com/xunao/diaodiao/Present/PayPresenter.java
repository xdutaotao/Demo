package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.PayModel;
import com.xunao.diaodiao.View.PayView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class PayPresenter extends BasePresenter<PayView> {
    @Inject
    PayModel model;

    @Inject
    PayPresenter() {
    }
}
