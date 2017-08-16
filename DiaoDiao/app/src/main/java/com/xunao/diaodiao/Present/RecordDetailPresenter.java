package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.RecordDetailModel;
import com.xunao.diaodiao.View.RecordDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class RecordDetailPresenter extends BasePresenter<RecordDetailView> {
    @Inject
    RecordDetailModel model;

    @Inject
    RecordDetailPresenter() {
    }
}
