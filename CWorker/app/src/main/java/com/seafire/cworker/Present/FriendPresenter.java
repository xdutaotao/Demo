package com.seafire.cworker.Present;

import android.content.Context;

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

    public void getFriends(){
        mCompositeSubscription.add(model.getFriends()
                .subscribe(new RxSubUtils<List<FreindBean>>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(List<FreindBean> token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }
}
