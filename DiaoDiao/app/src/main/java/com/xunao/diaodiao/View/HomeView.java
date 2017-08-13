package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.HomeResponseBean;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:44.
 */

public interface HomeView extends BaseView {
    void getData(HomeResponseBean bean);
    void getTokenResult(String s);
}
