package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseProjModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseProjView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjPresenter extends BasePresenter<ReleaseProjView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseProjPresenter() {
    }

    public void getTypeInfo(Context context){
        mCompositeSubscription.add(model.getTypeInfo()
                .subscribe(new RxSubUtils<TypeInfoRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(TypeInfoRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
