package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillDoingView;
import com.xunao.diaodiao.View.OrderSkillFinishView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderSkillFinishPresenter extends BasePresenter<OrderSkillFinishView> {

    @Inject
    LoginModel model;

    @Inject
    OrderSkillFinishPresenter() {
    }

    public void mySkillFinish(int page){
        mCompositeSubscription.add(model.mySkillFinish(page)
                .subscribe(new RxSubUtils<OrderSkillFinishRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderSkillFinishRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
