package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.List;

/**
 * Created by
 */
public interface FindProjectView extends BaseView {
    void getData(FindProjectRes list);
    void getNoMore(String msg);
    void getUrl(String url);
    void getProjType(TypeInfoRes res);
}
