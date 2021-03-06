package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/18.
 */

public class OrderSkillFinishRes {
    private List<OddBean> odd;

    public List<OddBean> getOdd() {
        return odd;
    }

    public void setOdd(List<OddBean> odd) {
        this.odd = odd;
    }

    private List<OddBean> maintenance;

    public List<OddBean> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(List<OddBean> maintenance) {
        this.maintenance = maintenance;
    }

    public static class OddBean{
        private int odd_id;
        private String title;
        private long publish_time;
        private String address;
        private String project_type;
        private String total_day;
        private String daily_wage;
        private int status;
        private int cancel_time;
        private int finish_time;
        private String url;
        private int evaluate_status;
        private String issue_time;
        private int maintenance_id;
        private String door_fee;
        private int technician_id;

        public int getTechnician_id() {
            return technician_id;
        }

        public void setTechnician_id(int technician_id) {
            this.technician_id = technician_id;
        }

        public int getMaintenance_id() {
            return maintenance_id;
        }

        public void setMaintenance_id(int maintenance_id) {
            this.maintenance_id = maintenance_id;
        }

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public String getIssue_time() {
            return issue_time;
        }

        public void setIssue_time(String issue_time) {
            this.issue_time = issue_time;
        }

        public int getEvaluate_status() {
            return evaluate_status;
        }

        public void setEvaluate_status(int evaluate_status) {
            this.evaluate_status = evaluate_status;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCancel_time() {
            return cancel_time;
        }

        public void setCancel_time(int cancel_time) {
            this.cancel_time = cancel_time;
        }

        public int getFinish_time() {
            return finish_time;
        }

        public void setFinish_time(int finish_time) {
            this.finish_time = finish_time;
        }
    }
}
