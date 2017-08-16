package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.BankModel;
import com.xunao.diaodiao.View.BankView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class BankPresenter extends BasePresenter<BankView> {
    @Inject
    BankModel model;

    @Inject
    BankPresenter() {
    }
}
