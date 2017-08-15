package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.SelectCompanyModel;
import com.xunao.diaodiao.View.SelectCompanyView;

import javax.inject.Inject;

/**
 * Created by
 */
public class SelectCompanyPresenter extends BasePresenter<SelectCompanyView> {
    @Inject
    SelectCompanyModel model;

    @Inject
    SelectCompanyPresenter() {
    }
}
