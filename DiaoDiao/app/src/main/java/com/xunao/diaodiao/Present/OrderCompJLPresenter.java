package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.OrderCompJLModel;
import com.xunao.diaodiao.View.OrderCompJLView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompJLPresenter extends BasePresenter<OrderCompJLView> {
    @Inject
    OrderCompJLModel model;

    @Inject
    OrderCompJLPresenter() {
    }
}
