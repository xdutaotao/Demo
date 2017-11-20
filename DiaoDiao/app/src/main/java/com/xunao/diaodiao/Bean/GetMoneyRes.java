package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/5.
 */

public class GetMoneyRes {
    private String balance;
    private int has_binding;

    private List<MoneyDetail> changes;

    public int getHas_binding() {
        return has_binding;
    }

    public void setHas_binding(int has_binding) {
        this.has_binding = has_binding;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<MoneyDetail> getChanges() {
        return changes;
    }

    public void setChanges(List<MoneyDetail> changes) {
        this.changes = changes;
    }


    public static class MoneyDetail{
        private String type;
        private String time;
        private String change;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getChange() {
            return change;
        }

        public void setChange(String change) {
            this.change = change;
        }
    }
}
