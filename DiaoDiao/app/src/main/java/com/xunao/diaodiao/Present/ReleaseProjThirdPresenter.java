package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseProjThirdModel;
import com.xunao.diaodiao.View.ReleaseProjThirdView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjThirdPresenter extends BasePresenter<ReleaseProjThirdView> {
    @Inject
    ReleaseProjThirdModel model;

    @Inject
    ReleaseProjThirdPresenter() {
    }
}
