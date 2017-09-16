package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface JoinDetailView extends BaseView {
    void getData(String s);
    void getOddInfo(GetOddInfoRes res);
}
