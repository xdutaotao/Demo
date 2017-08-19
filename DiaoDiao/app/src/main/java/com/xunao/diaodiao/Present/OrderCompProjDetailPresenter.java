package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.OrderCompProjDetailModel;
import com.xunao.diaodiao.View.OrderCompProjDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompProjDetailPresenter extends BasePresenter<OrderCompProjDetailView> {
    @Inject
    OrderCompProjDetailModel model;

    @Inject
    OrderCompProjDetailPresenter() {
    }
}
