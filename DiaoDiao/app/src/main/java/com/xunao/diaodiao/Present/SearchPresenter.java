package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Model.LoginModel;
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
    LoginModel model;

    @Inject
    SearchPresenter() {
    }

    public void checkFinish(){
        mCompositeSubscription.add(model.checkFinish()
                .subscribe(new RxSubUtils<CheckFinishRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(CheckFinishRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "请求失败";
                        getView().onFailure();
                    }
                }));
    }




}
