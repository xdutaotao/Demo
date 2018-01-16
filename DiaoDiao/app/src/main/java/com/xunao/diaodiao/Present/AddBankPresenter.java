package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Model.AddBankModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddBankView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AddBankPresenter extends BasePresenter<AddBankView> {
    @Inject
    LoginModel model;

    @Inject
    AddBankPresenter() {
    }

    //绑定卡
    public void bindingCard(Context context, BindBankReq req){
        mCompositeSubscription.add(model.bindingCard(req)
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

    //短信
    public void bindingCardGetVerify(Context context, BindBankReq req){
        mCompositeSubscription.add(model.bindingCardGetVerify(req)
                .subscribe(new RxSubUtils<AddBankRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(AddBankRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show("获取验证码失败");
                    }
                }));
    }


    /**
     * bank list
     * @param context
     * @param
     */
    public void getBankList(Context context){
        mCompositeSubscription.add(model.getBankCardList()
                .subscribe(new RxSubUtils<BankListRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(BankListRes token) {
                        getView().getBankList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
