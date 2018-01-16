package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/11/3 10:42.
 */

public class WeiBaoProgRes {

    private List<WorkBean> work;

    public List<WorkBean> getWork() {
        return work;
    }

    public void setWork(List<WorkBean> work) {
        this.work = work;
    }

    public static class WorkBean{
        private int  work_id;
        private long sign_time;
        private String location;
        private String remark;
        private int pass;
        private int paid;
        private int apply;
        private List<String> images;

        public int getWork_id() {
            return work_id;
        }

        public void setWork_id(int work_id) {
            this.work_id = work_id;
        }

        public long getSign_time() {
            return sign_time;
        }

        public void setSign_time(long sign_time) {
            this.sign_time = sign_time;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getPaid() {
            return paid;
        }

        public void setPaid(int paid) {
            this.paid = paid;
        }

        public int getApply() {
            return apply;
        }

        public void setApply(int apply) {
            this.apply = apply;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
