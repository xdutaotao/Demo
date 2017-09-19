package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public interface OrderSkillRecieveView extends BaseView {
    void getData(OrderSkillRecieveRes list);
    void cancle(String list);
}
