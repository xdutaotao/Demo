package com.demo.step.Present;

import com.demo.step.Bean.SearchBean;
import com.demo.step.Bean.SearchResponseBean;
import com.demo.step.Model.SearchResultModel;
import com.demo.step.Utils.RxSubUtils;
import com.demo.step.View.SearchResultView;

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
