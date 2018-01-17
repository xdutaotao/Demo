package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Bean.EditBankReq;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddBankView;
import com.xunao.diaodiao.View.EditBankView;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public class EditBankPresenter extends BasePresenter<EditBankView> {
    @Inject
    LoginModel model;

    @Inject
    EditBankPresenter() {
    }

    //绑定卡
    public void bindingCard(Context context, EditBankReq req){
        mCompositeSubscription.add(model.updateBankcard(req)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
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
