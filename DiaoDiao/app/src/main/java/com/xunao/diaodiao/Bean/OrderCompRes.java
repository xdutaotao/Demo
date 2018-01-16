package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class OrderCompRes {
    private List<Project> project;

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    private List<Project> supervisor;

    public List<Project> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(List<Project> supervisor) {
        this.supervisor = supervisor;
    }

    private List<Project> maintenance;

    public List<Project> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(List<Project> maintenance) {
        this.maintenance = maintenance;
    }

    private List<Project> mutual;

    public List<Project> getMutual() {
        return mutual;
    }

    public void setMutual(List<Project> mutual) {
        this.mutual = mutual;
    }

    public static class  Project implements Serializable{
        private int project_id;
        private String title;
        private int publish_time;
        private String address;
        private String project_type;
        private String project_fee;
        private int apply_count;
        private long build_time;
        private long cancel_time;
        private int evaluate_status;
        private int status;
        private String url;
        private String issue_time;
        private String supervisor_fee;
        private int supervisor_id;
        private int maintenance_id;
        private String door_fee;
        private int mutual_id;
        private int technician_id;
        private int apply_id;

        public int getApply_id() {
            return apply_id;
        }

        public void setApply_id(int apply_id) {
            this.apply_id = apply_id;
        }

        public int getTechnician_id() {
            return technician_id;
        }

        public void setTechnician_id(int technician_id) {
            this.technician_id = technician_id;
        }

        public int getMutual_id() {
            return mutual_id;
        }

        public void setMutual_id(int mutual_id) {
            this.mutual_id = mutual_id;
        }

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public String getSupervisor_fee() {
            return supervisor_fee;
        }

        public void setSupervisor_fee(String supervisor_fee) {
            this.supervisor_fee = supervisor_fee;
        }

        public int getMaintenance_id() {
            return maintenance_id;
        }

        public void setMaintenance_id(int maintenance_id) {
            this.maintenance_id = maintenance_id;
        }

        public int getSupervisor_id() {
            return supervisor_id;
        }

        public void setSupervisor_id(int supervisor_id) {
            this.supervisor_id = supervisor_id;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(int publish_time) {
            this.publish_time = publish_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public String getProject_fee() {
            return project_fee;
        }

        public void setProject_fee(String project_fee) {
            this.project_fee = project_fee;
        }

        public int getApply_count() {
            return apply_count;
        }

        public void setApply_count(int apply_count) {
            this.apply_count = apply_count;
        }

        public long getBuild_time() {
            return build_time;
        }

        public void setBuild_time(long build_time) {
            this.build_time = build_time;
        }

        public long getCancel_time() {
            return cancel_time;
        }

        public void setCancel_time(long cancel_time) {
            this.cancel_time = cancel_time;
        }

        public int getEvaluate_status() {
            return evaluate_status;
        }

        public void setEvaluate_status(int evaluate_status) {
            this.evaluate_status = evaluate_status;
        }
    }
}
