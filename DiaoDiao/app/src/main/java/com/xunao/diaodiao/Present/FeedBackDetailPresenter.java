package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.FeedBackDetailModel;
import com.xunao.diaodiao.View.FeedBackDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class FeedBackDetailPresenter extends BasePresenter<FeedBackDetailView> {
    @Inject
    FeedBackDetailModel model;

    @Inject
    FeedBackDetailPresenter() {
    }
}
