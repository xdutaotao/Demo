package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.OrderCompView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderSkillPresenter extends BasePresenter<OrderSkillView> {

    @Inject
    LoginModel model;

    @Inject
    OrderSkillPresenter() {
    }

    public void mySkillWait(int page, int who){
        mCompositeSubscription.add(model.mySkillWait(page, who)
                .subscribe(new RxSubUtils<OrderSkillRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderSkillRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
