package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface ReleaseSkillSureInfoView extends BaseView {
    void getPercent(GetPercentRes res);
    void getData(ReleaseProjRes res);
    void getData(Object res);
}
