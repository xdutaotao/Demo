package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.MoneyModel;
import com.xunao.diaodiao.View.MoneyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MoneyPresenter extends BasePresenter<MoneyView> {
    @Inject
    MoneyModel model;

    @Inject
    MoneyPresenter() {
    }
}
