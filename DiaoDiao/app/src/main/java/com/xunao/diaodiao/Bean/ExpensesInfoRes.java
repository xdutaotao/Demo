package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/26.
 */

public class ExpensesInfoRes {
    private List<ExpensesInfoBean> expenses_info;

    public List<ExpensesInfoBean> getExpenses_info() {
        return expenses_info;
    }

    public void setExpenses_info(List<ExpensesInfoBean> expenses_info) {
        this.expenses_info = expenses_info;
    }

    public static class ExpensesInfoBean{
        private int expenses_id;
        private String name;
        private String unit;
        private String cost;

        public int getExpenses_id() {
            return expenses_id;
        }

        public void setExpenses_id(int expenses_id) {
            this.expenses_id = expenses_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }
    }
}
