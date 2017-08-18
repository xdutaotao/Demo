package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseHelpModel;
import com.xunao.diaodiao.View.ReleaseHelpView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseHelpPresenter extends BasePresenter<ReleaseHelpView> {
    @Inject
    ReleaseHelpModel model;

    @Inject
    ReleaseHelpPresenter() {
    }
}
