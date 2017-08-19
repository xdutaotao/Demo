package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.OrderProjProgressModel;
import com.xunao.diaodiao.View.OrderProjProgressView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderProjProgressPresenter extends BasePresenter<OrderProjProgressView> {
    @Inject
    OrderProjProgressModel model;

    @Inject
    OrderProjProgressPresenter() {
    }
}
