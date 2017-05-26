package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.Model.CollectModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.View.CollectView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CollectPresenter extends BasePresenter<CollectView> {
    @Inject
    CollectModel model;

    @Inject
    CollectPresenter() {
    }

    public void packagingForm(Context context){
        mCompositeSubscription.add(model.packagingForm()
                .subscribe(new RxSubUtils<PackageBean.ResultBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(PackageBean.ResultBean token) {
                        getView().getData(token);
                    }

                }));
    }
}
