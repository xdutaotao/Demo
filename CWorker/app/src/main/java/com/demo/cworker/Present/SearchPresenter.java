package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.SearchBean;
import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.Model.SearchModel;
import com.demo.cworker.Model.SearchResultModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.View.SearchView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SearchPresenter extends BasePresenter<SearchView> {
    @Inject
    SearchModel model;

    @Inject
    SearchResultModel searchResultModel;

    @Inject
    SearchPresenter() {
    }

    public void getSearchResult(SearchBean bean){
        mCompositeSubscription.add(searchResultModel.getSearchResult(bean)
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

    public void getHistoryData(){
        mCompositeSubscription.add(model.getHistoryData()
                .subscribe(new RxSubUtils<List<String>>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(List<String> token) {
                        getView().getHistoryList(token);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().fail(msg);
                    }
                }));
    }

    public void getHotWord(Context context){
        mCompositeSubscription.add(model.getHotWord()
                .subscribe(new RxSubUtils<List<String>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(List<String> token) {
                        getView().getHotWord(token);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().fail(msg);
                    }
                }));
    }


}
