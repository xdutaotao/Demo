package com.seafire.cworker.Model;

import android.text.TextUtils;

import com.seafire.cworker.Bean.SearchResponseBean;
import com.seafire.cworker.Bean.SearchBean;
import com.seafire.cworker.Utils.RxUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by
 */
@Singleton
public class SearchResultModel extends BaseModel {
    @Inject
    public SearchResultModel() {
    }

    public Observable<SearchResponseBean> getSearchResult(SearchBean bean){
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(bean.getKeywords()))
            map.put("keywords", bean.getKeywords());
        map.put("token", bean.getToken());
        if (!TextUtils.isEmpty(bean.getProject()))
            map.put("project", bean.getProject());

        Map<String, Integer> integerMap = new HashMap<>();
        if (bean.getGroupType() > 0)
            integerMap.put("groupType", bean.getGroupType());
        integerMap.put("pageNo", bean.getPageNo());
        integerMap.put("pageSize", bean.getPageSize());

        //integerMap.put("type", bean.getType());
        //integerMap.put("vipRes", bean.getVipRes());
        //integerMap.put("sort", bean.getSort());

        return config.getRetrofitService().getSearchResult(map, integerMap)
                .flatMap(searchResponseBean -> {
                    if (TextUtils.equals("200", searchResponseBean.getMsg())){
                        return Observable.create(new Observable.OnSubscribe<SearchResponseBean>() {
                            @Override
                            public void call(Subscriber<? super SearchResponseBean> subscriber) {
                                subscriber.onNext(searchResponseBean);
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new RxUtils.ServerException(searchResponseBean.getResult()));
                    }
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }
}
