package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.AddBankModel;
import com.xunao.diaodiao.View.AddBankView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AddBankPresenter extends BasePresenter<AddBankView> {
    @Inject
    AddBankModel model;

    @Inject
    AddBankPresenter() {
    }
}
