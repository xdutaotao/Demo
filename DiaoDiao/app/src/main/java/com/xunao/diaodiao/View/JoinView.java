package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface JoinView extends BaseView {
    void getData(FindProjectRes res);
    void getAddressData(ArrayList<Province> res);
}
