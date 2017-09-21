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

    public void myAcceptProjectWork(Context context, int req){
        mCompositeSubscription.add(model.myAcceptProjectWork(req)
                .subscribe(new RxSubUtils<MyAcceptProjectWorkRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(MyAcceptProjectWorkRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
