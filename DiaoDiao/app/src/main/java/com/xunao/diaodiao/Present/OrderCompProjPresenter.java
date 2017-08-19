package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.OrderCompProjModel;
import com.xunao.diaodiao.View.OrderCompProjView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompProjPresenter extends BasePresenter<OrderCompProjView> {
    @Inject
    OrderCompProjModel model;

    @Inject
    OrderCompProjPresenter() {
    }
}
