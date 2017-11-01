package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseSkillSureInfoModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseSkillSureInfoView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillSureInfoPresenter extends BasePresenter<ReleaseSkillSureInfoView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseSkillSureInfoPresenter() {
    }

    //服务费
    public void countMaintenanceExpenses(Context context, String door_fee){
        mCompositeSubscription.add(model.countMaintenanceExpenses(door_fee)
                .subscribe(new RxSubUtils<GetPercentRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(GetPercentRes token) {
                        getView().getPercent(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }
}
