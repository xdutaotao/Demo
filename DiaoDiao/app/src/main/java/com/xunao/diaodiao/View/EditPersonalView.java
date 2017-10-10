package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface EditPersonalView extends BaseView {
        void getData(LoginResBean resBean);
        void getAddressData(ArrayList<Province> result);
}
