package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.SignDetailModel;
import com.xunao.diaodiao.View.SignDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SignDetailPresenter extends BasePresenter<SignDetailView> {
    @Inject
    SignDetailModel model;

    @Inject
    SignDetailPresenter() {
    }
}
