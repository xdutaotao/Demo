package com.demo.cworker.Present;

import com.demo.cworker.Model.CollectModel;
import com.demo.cworker.View.CollectView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CollectPresenter extends BasePresenter<CollectView> {
    @Inject
    CollectModel model;

    @Inject
    CollectPresenter() {
    }
}
