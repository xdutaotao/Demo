package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.MyComplaintRecordModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MyComplaintRecordView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class MyComplaintRecordPresenter extends BasePresenter<MyComplaintRecordView> {
    @Inject
    LoginModel model;

    @Inject
    MyComplaintRecordPresenter() {
    }

    public void getMyComplaint(int page, int who){
        mCompositeSubscription.add(model.getMyComplaint(page, who)
                .subscribe(new RxSubUtils<MyComplaintRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyComplaintRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                        getView().onFailure();
                    }
                }));
    }

}
