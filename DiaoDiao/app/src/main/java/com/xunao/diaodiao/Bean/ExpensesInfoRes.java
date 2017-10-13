package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/26.
 */

public class ExpensesInfoRes {
    private List<ReleaseProjReq.ExpensesBean> expenses_info;

    public List<ReleaseProjReq.ExpensesBean> getExpenses_info() {
        return expenses_info;
    }

    public void setExpenses_info(List<ReleaseProjReq.ExpensesBean> expenses_info) {
        this.expenses_info = expenses_info;
    }

}
