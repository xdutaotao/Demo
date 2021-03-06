package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/23 17:25.
 */

public interface PersonalView extends BaseView{
    void getData(HeadIconRes s);
    void getData(TypeInfoRes s);
    void getData(CheckFinishRes s);
    void getPersonalData(PersonalRes s);
}
