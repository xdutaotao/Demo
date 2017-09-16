package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.OrderCompProjDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.OrderCompProjDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class OrderCompProjDetailPresenter extends BasePresenter<OrderCompProjDetailView> {
    @Inject
    LoginModel model;

    @Inject
    OrderCompProjDetailPresenter() {
    }

//    public void myProjectWait(int id, int type){
//        mCompositeSubscription.add(model.getApplyList(id, type)
//                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
//                    @Override
//                    protected void _onNext(String token) {
//                        getView().getData(token);
//                    }
//
//                    @Override
//                    public void _onError(String s) {
//                        ToastUtil.show(s);
//                    }
//                }));
//    }
}
