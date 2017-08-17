package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.JoinModel;
import com.xunao.diaodiao.View.JoinView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class JoinPresenter extends BasePresenter<JoinView> {
    @Inject
    JoinModel model;

    @Inject
    JoinPresenter() {
    }
}
