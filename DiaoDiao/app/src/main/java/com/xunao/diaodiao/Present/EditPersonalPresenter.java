package com.xunao.diaodiao.Present;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.EditPersonalModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.EditPersonalView;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;
import rx.Subscriber;

/**
 * Created by
 */
public class EditPersonalPresenter extends BasePresenter<EditPersonalView> {
    @Inject
    LoginModel model;

    @Inject
    EditPersonalPresenter() {
    }

    public void fillInfor(Context context, @NonNull FillNormalReq req){
        mCompositeSubscription.add(model.fillNormalInfor(req)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

    //省市区
    public void getAddressData(Context context){
        mCompositeSubscription.add(model.getAddressData()
                .subscribe(new RxSubUtils<ArrayList<Province>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ArrayList<Province> token) {
                        getView().getAddressData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }


}
