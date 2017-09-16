package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Model.ApplyModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ApplyView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ApplyPresenter extends BasePresenter<ApplyView> {
    @Inject
    LoginModel model;

    @Inject
    ApplyPresenter() {
    }

    public void myProjectWait(int id, int type){
        mCompositeSubscription.add(model.getApplyList(id, type)
                .subscribe(new RxSubUtils<ApplyProjRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(ApplyProjRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
