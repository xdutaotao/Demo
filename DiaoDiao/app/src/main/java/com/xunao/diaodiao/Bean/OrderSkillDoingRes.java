package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/18.
 */

public class OrderSkillDoingRes {
    private List<OddBean> odd;

    public List<OddBean> getOdd() {
        return odd;
    }

    public void setOdd(List<OddBean> odd) {
        this.odd = odd;
    }

    private List<OddBean> project;

    public List<OddBean> getProject() {
        return project;
    }

    public void setProject(List<OddBean> project) {
        this.project = project;
    }

    private List<OddBean> maintenance;

    public List<OddBean> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(List<OddBean> maintenance) {
        this.maintenance = maintenance;
    }

    private List<OddBean> supervisor;

    public List<OddBean> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(List<OddBean> supervisor) {
        this.supervisor = supervisor;
    }

    public static class OddBean{
        private int odd_id;
        private String title;
        private long publish_time;
        private String address;
        private String project_type;
        private String total_day;
        private String daily_wage;
        private int build_time;
        private String url;
        private int project_id;
        private String project_price;
        private String issue_time;

        private int maintenance_id;
        private int supervisor_id;
        private String door_fee;

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public int getSupervisor_id() {
            return supervisor_id;
        }

        public void setSupervisor_id(int supervisor_id) {
            this.supervisor_id = supervisor_id;
        }

        public int getMaintenance_id() {
            return maintenance_id;
        }

        public void setMaintenance_id(int maintenance_id) {
            this.maintenance_id = maintenance_id;
        }

        public String getIssue_time() {
            return issue_time;
        }

        public void setIssue_time(String issue_time) {
            this.issue_time = issue_time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getOdd_id() {
            return odd_id;
        }

        public void setOdd_id(int odd_id) {
            this.odd_id = odd_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public long getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(long publish_time) {
            this.publish_time = publish_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public int getBuild_time() {
            return build_time;
        }

        public void setBuild_time(int build_time) {
            this.build_time = build_time;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getProject_price() {
            return project_price;
        }

        public void setProject_price(String project_price) {
            this.project_price = project_price;
        }
    }
}
