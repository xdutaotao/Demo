package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Model.CustomerDetailModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.CustomerDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CustomerDetailPresenter extends BasePresenter<CustomerDetailView> {
    @Inject
    LoginModel model;

    @Inject
    CustomerDetailPresenter() {
    }

    public void maintenanceInfo(Context context, GetMoneyReq id){
        mCompositeSubscription.add(model.maintenanceInfo(id)
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


}
