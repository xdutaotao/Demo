package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface EditSkillView extends BaseView {
    void getData(LoginResBean bean);
    void getData(TypeInfoRes bean);
    void getAddressData(ArrayList<Province> bean);
}
