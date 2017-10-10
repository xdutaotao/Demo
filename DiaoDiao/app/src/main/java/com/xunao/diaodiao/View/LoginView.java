package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.UserInfo;

/**
 * Created by
 */
public interface LoginView extends BaseView {
    void getData(LoginResBean data);
    void getWXData(UserInfo data);
}
