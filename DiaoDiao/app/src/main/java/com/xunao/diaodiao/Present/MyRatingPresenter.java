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

    public void getRating(Context context, int type){
        mCompositeSubscription.add(model.getRating(type)
                .subscribe(new RxSubUtils<MyRateRes>(mCompositeSubscription,context) {
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
