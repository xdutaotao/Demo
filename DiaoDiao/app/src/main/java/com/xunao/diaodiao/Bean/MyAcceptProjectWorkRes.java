package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/21.
 */

public class MyAcceptProjectWorkRes {
    private WorksBean works;

    public WorksBean getWorks() {
        return works;
    }

    public void setWorks(WorksBean works) {
        this.works = works;
    }

    public static class WorksBean{
        private String title;
        private String contact_mobile;
        private String region;
        private String address;
        private List<WorkBean> work;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<WorkBean> getWork() {
            return work;
        }

        public void setWork(List<WorkBean> work) {
            this.work = work;
        }
    }

    public static class WorkBean{
        private String title;
        private int stage1;
        private int stage2;
        private int works_id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStage1() {
            return stage1;
        }

        public void setStage1(int stage1) {
            this.stage1 = stage1;
        }

        public int getStage2() {
            return stage2;
        }

        public void setStage2(int stage2) {
            this.stage2 = stage2;
        }

        public int getWorks_id() {
            return works_id;
        }

        public void setWorks_id(int works_id) {
            this.works_id = works_id;
        }
    }
}
