package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface ReleaseHelpInfoView extends BaseView {
    void getAddressData(ArrayList<Province> res);
    void getData(ReleaseProjRes res);
}
