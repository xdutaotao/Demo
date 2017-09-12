package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.MyRatingModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MyRatingView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MyRatingPresenter extends BasePresenter<MyRatingView> {
    @Inject
    LoginModel model;

    @Inject
    MyRatingPresenter() {
    }

    /**
     * 评论列表
     * @param
     * @param
     */
    public void getRating(int page){
        mCompositeSubscription.add(model.getRating(page)
                .subscribe(new RxSubUtils<MyRateRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyRateRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
