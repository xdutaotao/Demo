package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.WeiBaoDetailRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface WebViewDetailView extends BaseView {
    void myProjectCancel(Object res);
    void getData(FindProjDetailRes res);
    void getData(WeiBaoDetailRes res);
}
