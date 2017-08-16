package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.NormalPersonalModel;
import com.xunao.diaodiao.View.NormalPersonalView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class NormalPersonalPresenter extends BasePresenter<NormalPersonalView> {
    @Inject
    NormalPersonalModel model;

    @Inject
    NormalPersonalPresenter() {
    }
}
