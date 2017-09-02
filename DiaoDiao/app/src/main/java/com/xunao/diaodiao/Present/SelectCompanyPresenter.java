package com.xunao.diaodiao.Present;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.SelectCompanyModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.SelectCompanyView;

import javax.inject.Inject;

/**
 * Created by
 */
public class SelectCompanyPresenter extends BasePresenter<SelectCompanyView> {
    @Inject
    LoginModel model;

    @Inject
    SelectCompanyPresenter() {
    }


    public void fillInfor(Context context, @NonNull FillCompanyReq req){
        mCompositeSubscription.add(model.fillInfor(req)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
                        getView().getData(token);
                    }
                }));
    }

}
