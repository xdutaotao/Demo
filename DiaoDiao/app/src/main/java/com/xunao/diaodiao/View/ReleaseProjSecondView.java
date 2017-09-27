package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface ReleaseProjSecondView extends BaseView {
    void getData(ExpensesInfoRes res);
    void getPercent(GetPercentRes res);
}
