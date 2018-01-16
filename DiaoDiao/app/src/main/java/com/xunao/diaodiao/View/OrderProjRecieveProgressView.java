package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;

/**
 * Created by
 */
public interface OrderProjRecieveProgressView extends BaseView {
    void getData(MyPublishOddWorkRes res);
    void passData(Object s);
    void giveMoney(String s);

}
