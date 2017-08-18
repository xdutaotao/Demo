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

}
