package com.demo.cworker.Present;

import com.demo.cworker.Model.SearchModel;
import com.demo.cworker.View.SearchView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SearchPresenter extends BasePresenter<SearchView> {
    @Inject
    SearchModel model;

    @Inject
    SearchPresenter() {
    }
}
