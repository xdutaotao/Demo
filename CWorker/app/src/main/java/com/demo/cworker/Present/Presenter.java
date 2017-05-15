package com.demo.cworker.Present;

public interface Presenter<T> {
    void attachView(T t);
    void detachView();
}