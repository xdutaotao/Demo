package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface ProjectDetailView extends BaseView {
    void getData(FindProjDetailRes res);
    void getLingGongData(FindLingGongRes res);
    void postProject(String res);
    void collectWork(String s);
    void cancleCollect(String s);
}
