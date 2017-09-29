package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.MyAcceptOddSubmitReq;
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderProjProgressView;
import com.xunao.diaodiao.View.OrderProjRecieveProgressView;

import javax.inject.Inject;

/**
 * Created by
 */
public class OrderProjRecieveProgressPresenter extends BasePresenter<OrderProjRecieveProgressView> {
    @Inject
    LoginModel model;

    @Inject
    OrderProjRecieveProgressPresenter() {
    }

    //进度列表
    public void myAcceptOddWork(int page){
        mCompositeSubscription.add(model.myAcceptOddWork(page)
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

    //提交进度
    public void myAcceptOddSubmit(MyAcceptOddSubmitReq page){
        mCompositeSubscription.add(model.myAcceptOddSubmit(page)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().passData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


    public void myPublishOddFail(MyPublicOddFailReq page){
        mCompositeSubscription.add(model.myPublishOddFail(page)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().giveMoney(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

}
