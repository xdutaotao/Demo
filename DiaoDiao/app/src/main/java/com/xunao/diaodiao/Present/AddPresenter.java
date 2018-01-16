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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public class AddPresenter extends BasePresenter<AddView> {
    @Inject
    LoginModel model;

    @Inject
    AddPresenter() {
    }


    public void getMutualList(Context context, FindProjReq req, int type){
        mCompositeSubscription.add(model.getFindProjectList(req, type)
                .subscribe(new RxSubUtils<FindProjectRes>(mCompositeSubscription, context) {
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
