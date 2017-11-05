package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.FindProjectRes;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface AddView extends BaseView {
    void getData(FindProjectRes s);
    void getAddressData(ArrayList<Province> s);
}
