package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseProjModel;
import com.xunao.diaodiao.View.ReleaseProjView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjPresenter extends BasePresenter<ReleaseProjView> {
    @Inject
    ReleaseProjModel model;

    @Inject
    ReleaseProjPresenter() {
    }
}
