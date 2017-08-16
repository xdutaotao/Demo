package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ComplaintRecordModel;
import com.xunao.diaodiao.View.ComplaintRecordView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ComplaintRecordPresenter extends BasePresenter<ComplaintRecordView> {
    @Inject
    ComplaintRecordModel model;

    @Inject
    ComplaintRecordPresenter() {
    }
}
