package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.MyAppealDetailRes;
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.RecordDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.RecordDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class RecordDetailPresenter extends BasePresenter<RecordDetailView> {
    @Inject
    LoginModel model;

    @Inject
    RecordDetailPresenter() {
    }

    public void myAppealDetail(int page){
        mCompositeSubscription.add(model.myAppealDetail(page)
                .subscribe(new RxSubUtils<MyAppealDetailRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyAppealDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
