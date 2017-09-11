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

    public void getCollectList(Context context){
        mCompositeSubscription.add(model.getCollectList()
                .subscribe(new RxSubUtils<MyFavoriteRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(MyFavoriteRes token) {
                        getView().getData(token);
                    }
                }));
    }


}
