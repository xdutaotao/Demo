package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.BalancePayRes;
import com.xunao.diaodiao.Bean.PayRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface PayView extends BaseView {
    void getData(BalancePayRes s);
    void paySuccess(Object s);
    void payAli(PayRes s);
    void payWX(PayRes s);
    void canclePublish(Object s);
    void destoryOrder(Object s);
    void onFail(String s);
}
