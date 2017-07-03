package com.bz.fitness.View;

import com.bz.fitness.Bean.HomeResponseBean;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:44.
 */

public interface HomeView extends BaseView {
    void getData(HomeResponseBean bean);
    void getTokenResult(String s);
}
