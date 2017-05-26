package com.demo.cworker.Model;

import android.text.TextUtils;

import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.Utils.RxUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by
 */
@Singleton
public class CollectModel extends BaseModel {
    @Inject
    public CollectModel() {}

    public Observable<PackageBean.ResultBean> packagingForm(){
        return config.getRetrofitService().packagingForm(User.getInstance().getUserInfo().getPerson().getProject())
                .flatMap(packageBean -> {
                    if (TextUtils.equals(packageBean.getMsg(), "200")){
                        return Observable.create(new Observable.OnSubscribe<PackageBean.ResultBean>(){

                            @Override
                            public void call(Subscriber<? super PackageBean.ResultBean> subscriber) {
                                subscriber.onNext(packageBean.getResult());
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new RxUtils.ServerException(""));
                    }
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }


}
