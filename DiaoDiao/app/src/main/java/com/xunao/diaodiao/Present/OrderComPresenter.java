package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.DocView;
import com.xunao.diaodiao.View.OrderCompView;

import java.util.List;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class OrderComPresenter extends BasePresenter<OrderCompView> {

    @Inject
    LoginModel model;

    @Inject
    OrderComPresenter() {
    }

    public void myProjectWait(int page){
        mCompositeSubscription.add(model.myProjectWait(page)
                .subscribe(new RxSubUtils<OrderCompRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(OrderCompRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
