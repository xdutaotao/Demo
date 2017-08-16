package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.MyFavoriteModel;
import com.xunao.diaodiao.View.MyFavoriteView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MyFavoritePresenter extends BasePresenter<MyFavoriteView> {
    @Inject
    MyFavoriteModel model;

    @Inject
    MyFavoritePresenter() {
    }
}
