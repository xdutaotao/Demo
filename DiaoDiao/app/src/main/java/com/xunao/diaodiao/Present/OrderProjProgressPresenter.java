package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.OrderProjProgressModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderProjProgressView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderProjProgressPresenter extends BasePresenter<OrderProjProgressView> {
    @Inject
    LoginModel model;

    @Inject
    OrderProjProgressPresenter() {
    }

    public void myPublishOddWorkProgress(int page){
        mCompositeSubscription.add(model.myPublishOddWorkProgress(page)
                .subscribe(new RxSubUtils<MyPublishOddWorkRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyPublishOddWorkRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
