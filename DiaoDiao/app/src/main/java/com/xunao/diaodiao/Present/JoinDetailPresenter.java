package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Model.JoinDetailModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.JoinDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class JoinDetailPresenter extends BasePresenter<JoinDetailView> {
    @Inject
    LoginModel model;

    @Inject
    JoinDetailPresenter() {
    }

    public void getCompanyInfo(int type){
        mCompositeSubscription.add(model.getCompanyInfo(type)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


    public void getOddInfo(int type){
        mCompositeSubscription.add(model.getOddInfo(type)
                .subscribe(new RxSubUtils<GetOddInfoRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(GetOddInfoRes token) {
                        getView().getOddInfo(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }



}
