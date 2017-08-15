package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.FindProjectModel;
import com.xunao.diaodiao.View.FindProjectView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class FindProjectPresenter extends BasePresenter<FindProjectView> {
    @Inject
    FindProjectModel model;

    @Inject
    FindProjectPresenter() {
    }
}
