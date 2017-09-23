package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface MyFavoriteView extends BaseView {
    void getData(MyFavoriteRes data);
    void cancelCollect(String s);
}
