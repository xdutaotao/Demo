package com.bz.fitness.Utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Description: no sticky
 * Created by guzhenfu on 2016/11/4 16:19.
 */

public class RxBus {
    // 主题
    private final SerializedSubject<Object, Object> mBus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    private static class RxBusHolder{
        public final static RxBus instance = new RxBus();
    }

    // 单例RxBus
    public static RxBus getInstance() {
        return RxBusHolder.instance;
    }

    // 提供了一个新的事件
    public void post(Object o) {
        mBus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

}
