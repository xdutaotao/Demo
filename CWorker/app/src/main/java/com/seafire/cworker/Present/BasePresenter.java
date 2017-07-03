package com.seafire.cworker.Present;

import com.seafire.cworker.View.BaseView;

import rx.subscriptions.CompositeSubscription;

public class BasePresenter<T extends BaseView> implements Presenter<T>{
    public CompositeSubscription mCompositeSubscription;
    public T view;

    @Override
    public void attachView(T t) {
        mCompositeSubscription = new CompositeSubscription();
        view = t;
    }

    @Override
    public void detachView() {
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
        view = null;
    }

    public T getView(){
        return view;
    }
}