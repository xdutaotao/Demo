package com.seafire.cworker.Present;

public interface Presenter<T> {
    void attachView(T t);
    void detachView();
}