package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.SkillRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;

/**
 * Created by
 */
public interface SuggestView extends BaseView {
    void getData(Object s);
    void getData(SkillRes s);
    void getData(String s);
    void getData(TypeInfoRes s);
}
