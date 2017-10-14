package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Model.ApplyDetailModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ApplyDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ApplyDetailPresenter extends BasePresenter<ApplyDetailView> {
    @Inject
    LoginModel model;

    @Inject
    ApplyDetailPresenter() {
    }

    public void getApplyDetail(Context context, int id){
        mCompositeSubscription.add(model.getApplyDetail(id)
                .subscribe(new RxSubUtils<ApplyDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ApplyDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


    public void getApplyPass(ApplyPassReq req){
        mCompositeSubscription.add(model.getApplyPass(req)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getPass(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
