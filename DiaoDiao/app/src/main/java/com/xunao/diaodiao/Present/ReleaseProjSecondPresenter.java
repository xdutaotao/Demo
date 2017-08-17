package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseProjSecondModel;
import com.xunao.diaodiao.View.ReleaseProjSecondView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjSecondPresenter extends BasePresenter<ReleaseProjSecondView> {
    @Inject
    ReleaseProjSecondModel model;

    @Inject
    ReleaseProjSecondPresenter() {
    }
}
