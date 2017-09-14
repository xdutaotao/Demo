package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.MessageModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MessageView;

import javax.inject.Inject;

/**
 * Created by
 */
public class MessagePresenter extends BasePresenter<MessageView> {
    @Inject
    LoginModel model;

    @Inject
    MessagePresenter() {
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
