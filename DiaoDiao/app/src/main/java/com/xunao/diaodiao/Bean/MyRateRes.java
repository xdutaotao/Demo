package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/7.
 */

public class MyRateRes {
    private List<ProjBean> project;

    public List<ProjBean> getProject() {
        return project;
    }

    public void setProject(List<ProjBean> project) {
        this.project = project;
    }

    public static class ProjBean{
        private String title;
        private String address;
        private String type;
        private String price;
        private int project_id;
        private String total_day;
        private String daily_wage;
        private int project_type;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
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

        public int getProject_type() {
            return project_type;
        }

        public void setProject_type(int project_type) {
            this.project_type = project_type;
        }
    }
}
