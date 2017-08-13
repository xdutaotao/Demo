package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Model.SearchResultModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.SearchResultView;

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
