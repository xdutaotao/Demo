package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.AddressBeanReq;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by
 */
public class AddPresenter extends BasePresenter<AddView> {
    @Inject
    LoginModel model;

    @Inject
    AddPresenter() {
    }

//    public void getRegionId(Context context, List<CitiesBean.DatasBean> address){
//        mCompositeSubscription.add(model.getRegionId(address)
//                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
//                    @Override
//                    protected void _onNext(String token) {
//                        getView().getData(token);
//                    }
//
//                    @Override
//                    protected void _onError(String msg) {
//                        ToastUtil.show(msg);
//                    }
//                }));
//    }
}
