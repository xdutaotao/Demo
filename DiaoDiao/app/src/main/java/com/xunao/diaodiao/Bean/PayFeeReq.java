package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/10/1.
 */

public class PayFeeReq {
    private int userid;
    private int type;
    private String verify;
    private String order_no;
    private String pay_fee;
    private int project_type;
    private String price;
    private String ip;
    private String balance;
    private int is_combination;

    public int getIs_combination() {
        return is_combination;
    }

    public void setIs_combination(int is_combination) {
        this.is_combination = is_combination;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(String pay_fee) {
        this.pay_fee = pay_fee;
    }

    public int getProject_type() {
        return project_type;
    }

    public void setProject_type(int project_type) {
        this.project_type = project_type;
    }
}
