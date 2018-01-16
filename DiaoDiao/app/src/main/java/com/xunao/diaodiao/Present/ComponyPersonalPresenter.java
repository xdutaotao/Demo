package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ComponyPersonalModel;
import com.xunao.diaodiao.View.ComponyPersonalView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ComponyPersonalPresenter extends BasePresenter<ComponyPersonalView> {
    @Inject
    ComponyPersonalModel model;

    @Inject
    ComponyPersonalPresenter() {
    }
}
