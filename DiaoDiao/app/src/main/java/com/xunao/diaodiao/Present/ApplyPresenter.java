package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ApplyModel;
import com.xunao.diaodiao.View.ApplyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ApplyPresenter extends BasePresenter<ApplyView> {
    @Inject
    ApplyModel model;

    @Inject
    ApplyPresenter() {
    }
}
