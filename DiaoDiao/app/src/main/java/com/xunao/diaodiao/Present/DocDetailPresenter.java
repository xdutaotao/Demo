package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.DocDetailModel;
import com.xunao.diaodiao.View.DocDetailView;

import javax.inject.Inject;

/**
 * Created by
 */
public class DocDetailPresenter extends BasePresenter<DocDetailView> {
    @Inject
    DocDetailModel model;

    @Inject
    DocDetailPresenter() {
    }
}
