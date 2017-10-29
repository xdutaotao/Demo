package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.MyFavoriteModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.MyFavoriteView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MyFavoritePresenter extends BasePresenter<MyFavoriteView> {
    @Inject
    LoginModel model;

    @Inject
    MyFavoritePresenter() {
    }

    public void getCollectList(int page){
        mCompositeSubscription.add(model.getCollectList(page)
                .subscribe(new RxSubUtils<MyFavoriteRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyFavoriteRes token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        super._onError(msg);
                        getView().onFailure();
                    }
                }));
    }


    /**
     * 取消收藏
     * @param page
     */
    public void cancelCollect(int page){
        mCompositeSubscription.add(model.cancelCollect(page)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().cancelCollect(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        super._onError(msg);
                        getView().onFailure();
                    }
                }));
    }

}
