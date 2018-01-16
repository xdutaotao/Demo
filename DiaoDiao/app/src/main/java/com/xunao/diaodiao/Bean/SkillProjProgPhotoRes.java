package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/21.
 */

public class SkillProjProgPhotoRes {
    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    private List<InfoBean> work;

    public List<InfoBean> getWork() {
        return work;
    }

    public void setWork(List<InfoBean> work) {
        this.work = work;
    }

    public static class InfoBean{
        private long date;
        private String location;
        private String remark;
        private List<String> images;
        private int audit;
        private int audit_status;
        private String feedback;
        private int work_id;
        private List<String> audit_images;
        private long sign_time;
        private int pass;
        private int paid;
        private int apply;

        public int getApply() {
            return apply;
        }

        public void setApply(int apply) {
            this.apply = apply;
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

        public long getSign_time() {
            return sign_time;
        }

        public void setSign_time(long sign_time) {
            this.sign_time = sign_time;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public int getWork_id() {
            return work_id;
        }

        public void setWork_id(int work_id) {
            this.work_id = work_id;
        }

        public List<String> getAudit_images() {
            return audit_images;
        }

        public void setAudit_images(List<String> audit_images) {
            this.audit_images = audit_images;
        }
    }
}
