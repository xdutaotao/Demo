package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Model.SearchModel;
import com.xunao.diaodiao.Model.SearchResultModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.SearchView;

import java.util.List;

import javax.inject.Inject;

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
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "请求失败";
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
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "请求失败";
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
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "请求失败";
                        getView().fail(msg);
                    }
                }));
    }


}
