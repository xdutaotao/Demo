package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SkillProjReceiveProgressModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SkillProjReceiveProgressView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SkillProjReceiveProgressPresenter extends BasePresenter<SkillProjReceiveProgressView> {
    @Inject
    LoginModel model;

    @Inject
    SkillProjReceiveProgressPresenter() {
    }

    public void myAcceptProjectWork(int req, int who){
        mCompositeSubscription.add(model.myAcceptProjectWork(req, who)
                .subscribe(new RxSubUtils<MyAcceptProjectWorkRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(MyAcceptProjectWorkRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

}
