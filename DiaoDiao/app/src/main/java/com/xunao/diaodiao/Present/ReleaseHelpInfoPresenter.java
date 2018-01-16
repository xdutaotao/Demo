package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseHelpInfoModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseHelpInfoView;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;
import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseHelpInfoPresenter extends BasePresenter<ReleaseHelpInfoView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseHelpInfoPresenter() {
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

    public void publishMutual(Context context, ReleaseSkillReq address){
        mCompositeSubscription.add(model.publishMutual(address)
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

}
