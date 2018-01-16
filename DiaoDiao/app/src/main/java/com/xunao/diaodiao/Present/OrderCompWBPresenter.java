package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.OrderCompWBModel;
import com.xunao.diaodiao.View.OrderCompWBView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompWBPresenter extends BasePresenter<OrderCompWBView> {
    @Inject
    OrderCompWBModel model;

    @Inject
    OrderCompWBPresenter() {
    }
}
