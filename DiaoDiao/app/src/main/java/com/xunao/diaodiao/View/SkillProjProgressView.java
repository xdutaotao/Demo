package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;

/**
 * Created by
 */
public interface SkillProjProgressView extends BaseView {
    void getData(String s);
    void getPass(Object s);
    void getList(SkillProjProgPhotoRes list);
}
