package com.xunao.diaodiao.Present;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SelectSkillModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.SelectSkillView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SelectSkillPresenter extends BasePresenter<SelectSkillView> {
    @Inject
    LoginModel model;

    @Inject
    SelectSkillPresenter() {
    }

    public void fillSkillInfor(Context context, @NonNull FillSkillReq req){
        mCompositeSubscription.add(model.fillSkillInfor(req)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
                        getView().getData(token);
                    }
                }));
    }

}
