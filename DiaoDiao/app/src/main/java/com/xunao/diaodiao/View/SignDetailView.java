package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.List;

/**
 * Created by
 */
public interface SignDetailView extends BaseView {
    void getData(String s);
    void getList(SignRes list);
}
