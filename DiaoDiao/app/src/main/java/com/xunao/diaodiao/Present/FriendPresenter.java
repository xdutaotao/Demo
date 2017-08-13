package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.FriendView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by
 */
public class FriendPresenter extends BasePresenter<FriendView> {
    @Inject
    LoginModel model;

    @Inject
    FriendPresenter() {
    }

    public void getFriends(Context context){
        mCompositeSubscription.add(model.getFriends()
                .subscribe(new RxSubUtils<List<FreindBean>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(List<FreindBean> token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "获取失败";
                        ToastUtil.show(s);
                        getView().onFailure();
                    }
                }));
    }
}
