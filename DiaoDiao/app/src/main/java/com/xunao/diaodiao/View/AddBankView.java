package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface AddBankView extends BaseView {
    void getData(String s);
    void getBankList(BankListRes res);
}
