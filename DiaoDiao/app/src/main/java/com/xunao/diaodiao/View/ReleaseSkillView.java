package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ProjectTypeRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface ReleaseSkillView extends BaseView {
    void getData(GetPercentRes res);
    void getProjectType(ProjectTypeRes res);
}
