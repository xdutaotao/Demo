package com.demo.cworker.Present;

import com.demo.cworker.Model.AddModel;
import com.demo.cworker.View.AddView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AddPresenter extends BasePresenter<AddView> {
    @Inject
    AddModel model;

    @Inject
    AddPresenter() {
    }
}
