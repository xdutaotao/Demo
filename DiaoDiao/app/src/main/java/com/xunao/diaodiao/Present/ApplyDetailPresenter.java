package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ApplyDetailModel;
import com.xunao.diaodiao.View.ApplyDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ApplyDetailPresenter extends BasePresenter<ApplyDetailView> {
    @Inject
    ApplyDetailModel model;

    @Inject
    ApplyDetailPresenter() {
    }
}
