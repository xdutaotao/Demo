package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseSkillSecondModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.ReleaseSkillSecondView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillSecondPresenter extends BasePresenter<ReleaseSkillSecondView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseSkillSecondPresenter() {
    }

    public void publishOdd(Context context, ReleaseSkillReq address){
        mCompositeSubscription.add(model.publishOdd(address)
                .subscribe(new RxSubUtils<ReleaseProjRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ReleaseProjRes token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }


    public void updateOdd(Context context, ReleaseSkillReq address){
        mCompositeSubscription.add(model.updateOdd(address)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

}
