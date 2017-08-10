package com.seafire.cworker.Model;

import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Utils.RxUtils;
import com.seafire.cworker.Utils.ShareUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by
 */
@Singleton
public class SearchModel extends BaseModel {
    @Inject
    public SearchModel() {
    }

    public Observable<List<String>> getHistoryData(){
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> list = ShareUtils.getStringList(Constants.SEARCH_KEY);
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    public Observable<List<String>> getHotWord(){
        return config.getRetrofitService().getHotWord()
                .compose(RxUtils.handleResult());
    }
}
