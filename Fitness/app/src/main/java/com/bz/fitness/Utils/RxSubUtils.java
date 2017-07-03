package com.bz.fitness.Utils;

import android.content.Context;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Description:
 * Created by guzhenfu on 2016/11/1 16:52.
 */

public abstract class RxSubUtils<T> extends Subscriber<T> {
    private RxSafeSubUtils<T> rxSafeSubUtils;

    public RxSubUtils() {
        rxSafeSubUtils = new RxSafeSubUtils<T>(this);
    }

    public RxSubUtils(CompositeSubscription mCompositeSubscription) {
        rxSafeSubUtils = new RxSafeSubUtils<T>(this, mCompositeSubscription);
    }

    /**
     * @param context context
     */
    public RxSubUtils(CompositeSubscription mCompositeSubscription, Context context) {
        rxSafeSubUtils = new RxSafeSubUtils<T>(this, mCompositeSubscription, context);
    }

    /**
     * 这个一定要有 Presenter的逻辑在这里处理
     * @param t
     */
    public void onNext(T t) {
        rxSafeSubUtils.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        rxSafeSubUtils.onError(e);
    }

    @Override
    public void onCompleted() {
        rxSafeSubUtils.onCompleted();
    }

    @Override
    public void onStart() {
        rxSafeSubUtils.onStart();
    }

    protected abstract void _onNext(T t);

    /**
     * 错误处理，需要的话重写这个方法
     */
    protected void _onError(){

    }

    /**
     * 错误处理，需要的话重写这个方法
     */
    protected void _onError(String msg){

    }

}
