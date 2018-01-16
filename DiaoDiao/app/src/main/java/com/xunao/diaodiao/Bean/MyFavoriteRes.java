package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/11 09:45.
 */

public class MyFavoriteRes {
    private List<Project> listInfo;

    public List<Project> getListInfo() {
        return listInfo;
    }

    public void setListInfo(List<Project> listInfo) {
        this.listInfo = listInfo;
    }

    public static class Project {
        private int id;
        private String title;
        private String classes;
        private String address;
        private String type;
        private String project_fee;
        private int collect_id;
        private String url;
        private String supervisor_fee;

        private int total_day;
        private String daily_wage;
        private String door_fee;
        private int info_type;
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getInfo_type() {
            return info_type;
        }

        public void setInfo_type(int info_type) {
            this.info_type = info_type;
        }

        public String getSupervisor_fee() {
            return supervisor_fee;
        }

        public void setSupervisor_fee(String supervisor_fee) {
            this.supervisor_fee = supervisor_fee;
        }

        public int getTotal_day() {
            return total_day;
        }

        public void setTotal_day(int total_day) {
            this.total_day = total_day;
        }

        public String getDaily_wage() {
            return daily_wage;
        }

        public void setDaily_wage(String daily_wage) {
            this.daily_wage = daily_wage;
        }

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
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

        public String getProject_fee() {
            return project_fee;
        }

        public void setProject_fee(String project_fee) {
            this.project_fee = project_fee;
        }

        public int getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(int collect_id) {
            this.collect_id = collect_id;
        }
    }


}
