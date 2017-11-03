package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.WeiBaoProgRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.WeiBaoProjModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.WeiBaoProjView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class WeiBaoProjPresenter extends BasePresenter<WeiBaoProjView> {
    @Inject
    LoginModel model;

    @Inject
    WeiBaoProjPresenter() {
    }

    //列表
    public void myAcceptMaintenanceWork(Context context, int req, int who){
        mCompositeSubscription.add(model.myAcceptMaintenanceWork(req, who)
                .subscribe(new RxSubUtils<WeiBaoProgRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(WeiBaoProgRes token) {
                        getView().getList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myAcceptMaintenanceSubmit(Context context, GetMoneyReq req, int who){
        mCompositeSubscription.add(model.myAcceptMaintenanceSubmit(req, who)
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
