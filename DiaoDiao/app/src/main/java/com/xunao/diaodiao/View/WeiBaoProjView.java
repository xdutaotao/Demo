package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.WeiBaoProgRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface WeiBaoProjView extends BaseView {
    void getList(WeiBaoProgRes res);
    void getData(Object object);
}
