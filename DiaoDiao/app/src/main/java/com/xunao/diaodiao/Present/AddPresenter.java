package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.AddressBeanReq;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
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


    public void getMutualList(FindProjReq req, int type){
        mCompositeSubscription.add(model.getFindProjectList(req, type)
                .subscribe(new RxSubUtils<FindProjectRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(FindProjectRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


}
