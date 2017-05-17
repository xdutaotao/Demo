package com.demo.cworker.Model;

import android.text.TextUtils;

import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.Utils.RxUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:44.
 */
@Singleton
public class HomeModel extends BaseModel {
    @Inject
    public HomeModel() {}

    public Observable<HomeResponseBean> getFirstPage(){
        return config.getRetrofitService().getFirstPage()
                .flatMap(homeResponseBean -> {
                    if (TextUtils.equals("200", homeResponseBean.getMsg())){
                        return Observable.create(new Observable.OnSubscribe<HomeResponseBean>() {
                            @Override
                            public void call(Subscriber<? super HomeResponseBean> subscriber) {
                                subscriber.onNext(homeResponseBean);
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new Throwable(homeResponseBean.getResult()));
                    }
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }
}
