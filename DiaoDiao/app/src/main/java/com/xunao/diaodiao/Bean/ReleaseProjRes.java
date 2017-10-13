package com.xunao.diaodiao.Bean;

import java.io.Serializable;

/**
 * Description:
 * Created by guzhenfu on 2017/10/1.
 */

public class ReleaseProjRes implements Serializable{
    private String order_no;
    private String total_fee;
    private String balance;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
