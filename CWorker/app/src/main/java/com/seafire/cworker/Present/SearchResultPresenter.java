package com.seafire.cworker.Present;

import com.seafire.cworker.Bean.SearchResponseBean;
import com.seafire.cworker.Bean.SearchBean;
import com.seafire.cworker.Model.SearchResultModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.View.SearchResultView;

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
