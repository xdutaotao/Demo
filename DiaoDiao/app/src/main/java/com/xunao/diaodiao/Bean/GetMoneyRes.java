package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/5.
 */

public class GetMoneyRes {
    private String balance;
    private String type;
    private String time;
    private String change;

    private List<Object> changes;


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

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

    public List<Object> getChanges() {
        return changes;
    }

    public void setChanges(List<Object> changes) {
        this.changes = changes;
    }
}
