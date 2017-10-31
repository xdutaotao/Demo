package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.MaintenanceTypeRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseHelpModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.ReleaseHelpView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseHelpPresenter extends BasePresenter<ReleaseHelpView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseHelpPresenter() {
    }


    public void maintenanceType(Context context){
        mCompositeSubscription.add(model.maintenanceType()
                .subscribe(new RxSubUtils<MaintenanceTypeRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(MaintenanceTypeRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "请求失败";
                        getView().onFailure();
                    }
                }));
    }


}
