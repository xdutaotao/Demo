package com.seafire.cworker.Utils;

import com.seafire.cworker.Bean.BaseBean;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxUtils {
    private static Observable.Transformer ioToMainThreadSchedulerTransformer;
    private static Observable.Transformer newThreadToMainThreadSchedulerTransformer;

    static {
        ioToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
        newThreadToMainThreadSchedulerTransformer = createNewThreadToMainThreadScheduler();
    }

    private static <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> applyIOToMainThreadSchedulers() {
        return ioToMainThreadSchedulerTransformer;
    }

    private static <T> Observable.Transformer<T, T> createNewThreadToMainThreadScheduler(){
        return tObservable -> tObservable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> applyNewThreadToMainThreadSchedulers(){
        return newThreadToMainThreadSchedulerTransformer;
    }
	
	/**
     * 处理服务器返回的数据，进一步处理错误信息， 有线程切换
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseBean<T>, T> handleResult(){
        return new Observable.Transformer<BaseBean<T>, T>(){

            @Override
            public Observable<T> call(Observable<BaseBean<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseBean<T> result) {
                        if (!result.isError()){
                            if (result.getData() != null){
                                return createData(result.getData());
                            }else{
                                return createData((T) result.getResult());
                            }
                        }else {
                            return Observable.error(new ServerException(result.errorMsg()));
                        }
                    }
                }).debounce(1000, TimeUnit.MILLISECONDS)    //防止重复请求
                .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
                        //.retryWhen(new RetryWhenProcess(5));
            }
        };
    }

    /**
     * 处理服务器返回的数据，进一步处理错误信息， 有线程切换
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseBean<T>, T> handleResultNoThread(){
        return new Observable.Transformer<BaseBean<T>, T>(){

            @Override
            public Observable<T> call(Observable<BaseBean<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseBean<T> result) {
                        if (!result.isError()){
                            if (result.getData() != null){
                                return createData(result.getData());
                            }else{
                                return createData((T) result.getResult());
                            }
                        }else {
                            return Observable.error(new ServerException(result.errorMsg()));
                        }
                    }
                }).debounce(1000, TimeUnit.MILLISECONDS)    //防止重复请求
                        .unsubscribeOn(Schedulers.io());
                //.retryWhen(new RetryWhenProcess(5));
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {

                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 自定义 服务器返回异常
     */
    public static class ServerException extends Throwable {
        private String msg;

        public ServerException(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 重试机制
     * 重复次数：5
     * 间隔时间：interval的次方数，比如interval=2，第一次2s, 第二次4s, 第三次8s, 第四次16s, 第五次32s 结束
     * 条件：没网络不重试，自定义逻辑不重试，网络连接并且网络不稳定才重试
     */
    public static class RetryWhenProcess implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private long mInterval;

        public RetryWhenProcess(long interval) {
            mInterval = interval;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {
            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> call(Throwable throwable) {
                            //没有网络，直接返回，不重试了
                            if (throwable instanceof UnknownHostException || !NetWorkUtils.isNetworkAvailable()) {
                                return Observable.error(throwable);
                            }
                            return Observable.just(throwable).zipWith(Observable.range(1, 5), new Func2<Throwable, Integer, Integer>() {
                                @Override
                                public Integer call(Throwable throwable, Integer i) {
                                    return i;
                                }
                            }).flatMap(new Func1<Integer, Observable<? extends Long>>() {
                                @Override
                                public Observable<? extends Long> call(Integer retryCount) {

                                    return Observable.timer((long) Math.pow(mInterval, retryCount), TimeUnit.SECONDS);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

}