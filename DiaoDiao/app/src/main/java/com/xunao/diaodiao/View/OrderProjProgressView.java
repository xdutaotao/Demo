package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface OrderProjProgressView extends BaseView {
    void getData(MyPublishOddWorkRes res);
    void passData(String s);
    void giveMoney(Object s);

}
