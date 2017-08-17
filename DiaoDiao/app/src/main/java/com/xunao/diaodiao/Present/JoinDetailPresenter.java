package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.JoinDetailModel;
import com.xunao.diaodiao.View.JoinDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class JoinDetailPresenter extends BasePresenter<JoinDetailView> {
    @Inject
    JoinDetailModel model;

    @Inject
    JoinDetailPresenter() {
    }
}
