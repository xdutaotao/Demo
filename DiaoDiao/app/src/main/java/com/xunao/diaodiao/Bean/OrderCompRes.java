package com.xunao.diaodiao.Bean;

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

    public static class  Project{
        private int project_id;
        private String title;
        private int publish_time;
        private String address;
        private String project_type;
        private String project_fee;
        private int apply_count;


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
    }
}
