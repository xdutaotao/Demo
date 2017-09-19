package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.OrderSkillFinishRecieveView;
import com.xunao.diaodiao.View.OrderSkillFinishView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderSkillFinishRecievePresenter extends BasePresenter<OrderSkillFinishRecieveView> {

    @Inject
    LoginModel model;

    @Inject
    OrderSkillFinishRecievePresenter() {
    }

    public void myAcceptOddFinish(int page){
        mCompositeSubscription.add(model.myAcceptOddFinish(page)
                .subscribe(new RxSubUtils<OrderSkillFinishRecieveRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderSkillFinishRecieveRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }
}
