package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface GetMoneyView extends BaseView {
    void getData(String s);
    void getBankList(BankListRes res);
}
