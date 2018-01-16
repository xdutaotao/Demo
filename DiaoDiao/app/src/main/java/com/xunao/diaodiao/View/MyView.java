package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.PersonalRes;

/**
 * Created by
 */
public interface MyView extends BaseView {
    void getData(MyBean data);
    void getPersonalData(PersonalRes data);
}
