package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.OrderCompHZModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderCompHZView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompHZPresenter extends BasePresenter<OrderCompHZView> {
    @Inject
    LoginModel model;

    @Inject
    OrderCompHZPresenter() {
    }

    public void myProjectWait(int page, int who){
        mCompositeSubscription.add(model.myProjectWait(page, who)
                .subscribe(new RxSubUtils<OrderCompRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderCompRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void allMutualCancel(Context context, int page){
        mCompositeSubscription.add(model.allMutualCancel(page)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
