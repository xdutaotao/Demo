package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.MyRatingModel;
import com.xunao.diaodiao.View.MyRatingView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MyRatingPresenter extends BasePresenter<MyRatingView> {
    @Inject
    MyRatingModel model;

    @Inject
    MyRatingPresenter() {
    }
}
