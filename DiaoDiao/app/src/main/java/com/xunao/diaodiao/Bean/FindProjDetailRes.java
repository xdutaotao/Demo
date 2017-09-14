package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjDetailRes {

    /**
     * project_info : {"title":"采暖系统地暖安装","create_time":"1505357062","company_name":"测试账号1","type":"采暖系统","district":"上海上海长宁区","address":"交通路100号","build_time":"1506787200","describe":"需要懂地暖的技术员，工作经验最好是3年","project_fee":"2000","service_fee":"100"}
     * project_drawing : [{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"}]
     * project_expenses : [{"min_cost":"150","expenses_name":"定位费（次）","unit_price":"200","amount":"1","total_price":"200"}]
     * is_apply : false
     */

    private ProjectInfoBean project_info;
    private boolean is_apply;
    private List<ProjectDrawingBean> project_drawing;
    private List<ProjectExpensesBean> project_expenses;

    public ProjectInfoBean getProject_info() {
        return project_info;
    }

    public void setProject_info(ProjectInfoBean project_info) {
        this.project_info = project_info;
    }

    public boolean isIs_apply() {
        return is_apply;
    }

    public void setIs_apply(boolean is_apply) {
        this.is_apply = is_apply;
    }

    public List<ProjectDrawingBean> getProject_drawing() {
        return project_drawing;
    }

    public void setProject_drawing(List<ProjectDrawingBean> project_drawing) {
        this.project_drawing = project_drawing;
    }

    public List<ProjectExpensesBean> getProject_expenses() {
        return project_expenses;
    }

    public void setProject_expenses(List<ProjectExpensesBean> project_expenses) {
        this.project_expenses = project_expenses;
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
        private String create_time;
        private String company_name;
        private String type;
        private String district;
        private String address;
        private String build_time;
        private String describe;
        private String project_fee;
        private String service_fee;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBuild_time() {
            return build_time;
        }

        public void setBuild_time(String build_time) {
            this.build_time = build_time;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
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
    }

    public static class ProjectDrawingBean {
        /**
         * image : http://admintest.diao-diao.com/upload/20170913232004_card1345.png
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        private String min_cost;
        private String expenses_name;
        private String unit_price;
        private String amount;
        private String total_price;

        public String getMin_cost() {
            return min_cost;
        }

        public void setMin_cost(String min_cost) {
            this.min_cost = min_cost;
        }

        public String getExpenses_name() {
            return expenses_name;
        }

        public void setExpenses_name(String expenses_name) {
            this.expenses_name = expenses_name;
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
