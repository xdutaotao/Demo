package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface ReleaseProjSecondView extends BaseView {
    void getData(ExpensesInfoRes res);
    void getPercent(GetPercentRes res);
    void getLowProce(GetPercentRes res);
    void getAddressData(ArrayList<Province> res);
}
