package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.EditPersonalModel;
import com.xunao.diaodiao.View.EditPersonalView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class EditPersonalPresenter extends BasePresenter<EditPersonalView> {
    @Inject
    EditPersonalModel model;

    @Inject
    EditPersonalPresenter() {
    }
}
