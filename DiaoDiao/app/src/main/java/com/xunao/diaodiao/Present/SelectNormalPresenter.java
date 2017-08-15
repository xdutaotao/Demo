package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.SelectNormalModel;
import com.xunao.diaodiao.View.SelectNormalView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SelectNormalPresenter extends BasePresenter<SelectNormalView> {
    @Inject
    SelectNormalModel model;

    @Inject
    SelectNormalPresenter() {
    }
}
