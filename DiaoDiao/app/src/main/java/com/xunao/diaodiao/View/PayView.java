package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.PayRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface PayView extends BaseView {
    void getData(Object s);
    void paySuccess(Object s);
    void payAli(PayRes s);
    void canclePublish(Object s);
}
