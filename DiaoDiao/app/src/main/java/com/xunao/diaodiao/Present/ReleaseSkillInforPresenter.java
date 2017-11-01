package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseSkillInforModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseSkillInforView;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;
import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillInforPresenter extends BasePresenter<ReleaseSkillInforView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseSkillInforPresenter() {
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
