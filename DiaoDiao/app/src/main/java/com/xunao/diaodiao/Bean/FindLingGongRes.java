package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/15 21:31.
 */

public class FindLingGongRes {


    /**
     * odd_info : {"title":"空调系统找零工","create_time":"1505390733","publish_name":"宇**","type":"空调系统","district":"上海上海长宁区","address":"长乐路2220号","build_time":"1506787200","describe":"就做空调系统，具体内容看图纸","total_day":"3","daily_wage":"220","service_fee":"19.8"}
     * odd_drawing : [{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"}]
     * is_apply : false
     */

    private OddInfoBean odd_info;
    private OddInfoBean project_info;
    private int is_apply;
    private List<String> odd_drawing;
    private List<String> project_drawing;
    private ProjectExpensesBean project_expenses;


    public List<String> getOdd_drawing() {
        return odd_drawing;
    }

    public void setOdd_drawing(List<String> odd_drawing) {
        this.odd_drawing = odd_drawing;
    }

    public ProjectExpensesBean getProject_expenses() {
        return project_expenses;
    }

    public void setProject_expenses(ProjectExpensesBean project_expenses) {
        this.project_expenses = project_expenses;
    }

    public List<String> getProject_drawing() {
        return project_drawing;
    }

    public void setProject_drawing(List<String> project_drawing) {
        this.project_drawing = project_drawing;
    }

    public OddInfoBean getOdd_info() {
        return odd_info;
    }

    public void setOdd_info(OddInfoBean odd_info) {
        this.odd_info = odd_info;
    }

    public OddInfoBean getProject_info() {
        return project_info;
    }

    public void setProject_info(OddInfoBean project_info) {
        this.project_info = project_info;
    }

    public int getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(int is_apply) {
        this.is_apply = is_apply;
    }

    public static class OddInfoBean {
        /**
         * title : 空调系统找零工
         * create_time : 1505390733
         * publish_name : 宇**
         * type : 空调系统
         * district : 上海上海长宁区
         * address : 长乐路2220号
         * build_time : 1506787200
         * describe : 就做空调系统，具体内容看图纸
         * total_day : 3
         * daily_wage : 220
         * service_fee : 19.8
         */

        private String title;
        private String create_time;
        private String publish_name;
        private int public_type;
        private String type;
        private String district;
        private String address;
        private String build_time;
        private String describe;
        private String project_fee;
        private String total_day;
        private String daily_wage;
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

        public String getPublish_name() {
            return publish_name;
        }

        public void setPublish_name(String publish_name) {
            this.publish_name = publish_name;
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

        public String getTotal_day() {
            return total_day;
        }

        public void setTotal_day(String total_day) {
            this.total_day = total_day;
        }

        public String getDaily_wage() {
            return daily_wage;
        }

        public void setDaily_wage(String daily_wage) {
            this.daily_wage = daily_wage;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public int getPublic_type() {
            return public_type;
        }

        public void setPublic_type(int public_type) {
            this.public_type = public_type;
        }

        public String getProject_fee() {
            return project_fee;
        }

        public void setProject_fee(String project_fee) {
            this.project_fee = project_fee;
        }
    }


    public static class ProjectExpensesBean{
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
