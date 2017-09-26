package com.seafire.cworker.Model;

import android.text.TextUtils;

import com.seafire.cworker.Bean.HomeResponseBean;
import com.seafire.cworker.Utils.RxUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

import static com.seafire.cworker.Common.Constants.LOGIN_AGAIN;

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


    public Observable<String> checkToken(){
        return config.getRetrofitService().checkToken(User.getInstance().getUserId())
                .map(baseResponseBean -> {
                    if (TextUtils.equals(baseResponseBean.getMsg(), "200")){

                        return "ok";
                    }else if (TextUtils.equals(baseResponseBean.getMsg(), "500")){
                        User.getInstance().clearUser();
                        return LOGIN_AGAIN;
                    }else{
                        return " ";
                    }
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

}
