package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.EditCompanyModel;
import com.xunao.diaodiao.View.EditCompanyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class EditCompanyPresenter extends BasePresenter<EditCompanyView> {
    @Inject
    EditCompanyModel model;

    @Inject
    EditCompanyPresenter() {
    }
}
