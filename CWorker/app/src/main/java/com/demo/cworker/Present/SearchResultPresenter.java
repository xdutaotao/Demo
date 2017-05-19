package com.demo.cworker.Present;

import android.content.Context;
import android.text.TextUtils;

import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.Bean.SearchBean;
import com.demo.cworker.Model.SearchResultModel;
import com.demo.cworker.Model.User;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.SearchResultView;

import javax.inject.Inject;

/**
 * Created by
 */
public class SearchResultPresenter extends BasePresenter<SearchResultView> {
    @Inject
    SearchResultModel model;

    @Inject
    SearchResultPresenter() {}

    public void getSearchResult(SearchBean bean){
        mCompositeSubscription.add(model.getSearchResult(bean)
                .subscribe(new RxSubUtils<SearchResponseBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(SearchResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().fail(msg);
                    }
                }));
    }
}
