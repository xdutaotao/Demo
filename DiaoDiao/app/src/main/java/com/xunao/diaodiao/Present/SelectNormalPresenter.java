package com.xunao.diaodiao.Present;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SelectNormalModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.SelectNormalView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SelectNormalPresenter extends BasePresenter<SelectNormalView> {
    @Inject
    LoginModel model;

    @Inject
    SelectNormalPresenter() {
    }

    public void fillNormalInfor(Context context, @NonNull FillNormalReq req){
        mCompositeSubscription.add(model.fillNormalInfor(req)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
                        getView().getData(token);
                    }
                }));
    }
}
