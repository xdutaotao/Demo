package com.bz.fitness.Present;

import com.bz.fitness.Bean.SearchBean;
import com.bz.fitness.Bean.SearchResponseBean;
import com.bz.fitness.Model.SearchResultModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.View.SearchResultView;

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
