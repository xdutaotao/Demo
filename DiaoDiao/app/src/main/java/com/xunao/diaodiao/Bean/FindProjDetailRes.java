package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjDetailRes implements Serializable{

    /**
     * project_info : {"title":"采暖系统地暖安装","create_time":"1505357062","company_name":"测试账号1","type":"采暖系统","district":"上海上海长宁区","address":"交通路100号","build_time":"1506787200","describe":"需要懂地暖的技术员，工作经验最好是3年","project_fee":"2000","service_fee":"100"}
     * project_drawing : [{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"}]
     * project_expenses : [{"min_cost":"150","expenses_name":"定位费（次）","unit_price":"200","amount":"1","total_price":"200"}]
     * is_apply : false
     */

    private ProjectInfoBean project;
    private DetailBean detail;
    private String url;

    public ProjectInfoBean getProject() {
        return project;
    }

    public void setProject(ProjectInfoBean project) {
        this.project = project;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class ProjectInfoBean {
        /**
         * title : 采暖系统地暖安装
         * create_time : 1505357062
         * company_name : 测试账号1
         * type : 采暖系统
         * district : 上海上海长宁区
         * address : 交通路100号
         * build_time : 1506787200
         * describe : 需要懂地暖的技术员，工作经验最好是3年
         * project_fee : 2000
         * service_fee : 100
         */

        private String title;
        private String region;
        private String address;
        private String contact;
        private String contact_mobile;
        private String close_time;
        private String describe;
        private List<String> images;
        private List<ProjectExpensesBean> expenses;
        private String project_fee;
        private String service_fee;
        private int publish_type;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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

        public String getClose_time() {
            return close_time;
        }

        public void setClose_time(String close_time) {
            this.close_time = close_time;
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

        public List<ProjectExpensesBean> getExpenses() {
            return expenses;
        }

        public void setExpenses(List<ProjectExpensesBean> expenses) {
            this.expenses = expenses;
        }

        public String getProject_fee() {
            return project_fee;
        }

        public void setProject_fee(String project_fee) {
            this.project_fee = project_fee;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public int getPublish_type() {
            return publish_type;
        }

        public void setPublish_type(int publish_type) {
            this.publish_type = publish_type;
        }
    }

    public static class DetailBean{
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
        private List<ReleaseProjReq.ExpensesBean> expenses;
        private String verify;
        private String region;
        private String project_type_name;
        private String project_type_class;

        public String getProject_type_name() {
            return project_type_name;
        }

        public void setProject_type_name(String project_type_name) {
            this.project_type_name = project_type_name;
        }

        public String getProject_type_class() {
            return project_type_class;
        }

        public void setProject_type_class(String project_type_class) {
            this.project_type_class = project_type_class;
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

        public List<ReleaseProjReq.ExpensesBean> getExpenses() {
            return expenses;
        }

        public void setExpenses(List<ReleaseProjReq.ExpensesBean> expenses) {
            this.expenses = expenses;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }


    public static class ProjectExpensesBean {
        /**
         * min_cost : 150
         * expenses_name : 定位费（次）
         * unit_price : 200
         * amount : 1
         * total_price : 200
         */

        private int expenses_id;
        private String name;
        private String unit;
        private String unit_price;
        private String amount;
        private String total_price;

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
