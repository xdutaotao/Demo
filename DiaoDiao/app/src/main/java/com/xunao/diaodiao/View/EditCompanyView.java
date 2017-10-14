package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.PersonalRes;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface EditCompanyView extends BaseView {
    void getData(LoginResBean bean);
    void getAddressData(ArrayList<Province> result);
}
