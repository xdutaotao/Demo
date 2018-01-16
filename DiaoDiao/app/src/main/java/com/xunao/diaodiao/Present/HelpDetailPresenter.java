package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.HelpDetailModel;
import com.xunao.diaodiao.View.HelpDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class HelpDetailPresenter extends BasePresenter<HelpDetailView> {
    @Inject
    HelpDetailModel model;

    @Inject
    HelpDetailPresenter() {
    }
}
