package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillDoingView;
import com.xunao.diaodiao.View.OrderSkillView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderSkillDoingPresenter extends BasePresenter<OrderSkillDoingView> {

    @Inject
    LoginModel model;

    @Inject
    OrderSkillDoingPresenter() {
    }

    public void mySkillDoing(int page, int who){
        mCompositeSubscription.add(model.mySkillDoing(page, who)
                .subscribe(new RxSubUtils<OrderSkillDoingRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderSkillDoingRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
