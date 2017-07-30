package com.seafire.cworker.Present;

import android.content.Context;
import android.text.TextUtils;

import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Model.LoginModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.FriendView;

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
