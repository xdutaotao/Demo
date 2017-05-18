package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.Bean.SearchBean;
import com.demo.cworker.Model.SearchResultModel;
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

    public void getSearchResult(Context context, SearchBean bean){
        mCompositeSubscription.add(model.getSearchResult(bean)
                .subscribe(new RxSubUtils<SearchResponseBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(SearchResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError() {
                        ToastUtil.show("请求失败");
                    }
                }));
    }
}
