package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.MessageListRes;
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

    public void messageList(Context context, int page){
        mCompositeSubscription.add(model.messageList(page)
                .subscribe(new RxSubUtils<MessageListRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(MessageListRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
