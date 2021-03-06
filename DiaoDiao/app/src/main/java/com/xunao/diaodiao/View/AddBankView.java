package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.View.BaseView;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface AddBankView extends BaseView {
    void getData(AddBankRes s);
    void getData(Object s);
    void getBankList(BankListRes res);
    void getAddressData(ArrayList<Province> list);
}
