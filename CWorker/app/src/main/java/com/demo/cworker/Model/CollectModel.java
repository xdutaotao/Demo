package com.demo.cworker.Model;

import com.demo.cworker.Bean.PackageBean;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by
 */
@Singleton
public class CollectModel extends BaseModel {
    @Inject
    public CollectModel() {
    }

    public Observable<PackageBean> packagingForm(){
        return config.getRetrofitService().packagingForm(User.getInstance().getUserInfo().getPerson().getProject());
    }
}
