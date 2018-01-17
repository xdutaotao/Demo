package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.BankListRes;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;

/**
 * Created by
 */
public interface EditBankView extends BaseView {
    void getData(Object s);
    void getAddressData(ArrayList<Province> list);
}
