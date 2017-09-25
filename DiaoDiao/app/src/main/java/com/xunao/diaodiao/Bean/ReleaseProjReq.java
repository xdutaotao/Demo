package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/25.
 */

public class ReleaseProjReq implements Serializable{
    private int userid;
    private int type;
    private String project_type;
    private String project_class;
    private String title;
    private int province;
    private int city;
    private int district;
    private String address;
    private String contact;
    private String contact_mobile;
    private long build_time;
    private String describe;
    private List<String> images;
    private String service_cost;
    private String project_fee;
    private String total_price;
    private List<ExpensesBean> expenses;
    private String verify;

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

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getProject_class() {
        return project_class;
    }

    public void setProject_class(String project_class) {
        this.project_class = project_class;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getService_cost() {
        return service_cost;
    }

    public void setService_cost(String service_cost) {
        this.service_cost = service_cost;
    }

    public String getProject_fee() {
        return project_fee;
    }

    public void setProject_fee(String project_fee) {
        this.project_fee = project_fee;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public List<ExpensesBean> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpensesBean> expenses) {
        this.expenses = expenses;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public static class ExpensesBean{
        private int expenses_id;
        private String unit_price;
        private String amount;
        private String total_price;

        public int getExpenses_id() {
            return expenses_id;
        }

        public void setExpenses_id(int expenses_id) {
            this.expenses_id = expenses_id;
        }

        public String getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(String unit_price) {
            this.unit_price = unit_price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }
    }
}
