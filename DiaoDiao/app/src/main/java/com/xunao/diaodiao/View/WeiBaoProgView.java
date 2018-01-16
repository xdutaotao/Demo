package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface WeiBaoProgView extends BaseView {
    void getList(MyPublishOddWorkRes res);
    void passData(Object res);
}
