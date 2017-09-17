package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseSKillTypeModel;
import com.xunao.diaodiao.View.ReleaseSKillTypeView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSKillTypePresenter extends BasePresenter<ReleaseSKillTypeView> {
    @Inject
    ReleaseSKillTypeModel model;

    @Inject
    ReleaseSKillTypePresenter() {
    }
}
