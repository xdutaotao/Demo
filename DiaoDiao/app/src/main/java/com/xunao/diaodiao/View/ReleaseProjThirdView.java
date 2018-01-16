package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseProjRes;

import java.util.List;

/**
 * Created by
 */
public interface ReleaseProjThirdView extends BaseView {
    void getData(ReleaseProjRes s);
    void getBase64List(GetPercentRes s);
    void updateProject(Object s);
}
