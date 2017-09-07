package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/7.
 */

public class MyRateRes {
    private List<ProjBean> project;
    private List<ProjBean> supervisor;
    private List<ProjBean> maintenance;
    private List<ProjBean> odd;

    public List<ProjBean> getProject() {
        return project;
    }

    public void setProject(List<ProjBean> project) {
        this.project = project;
    }

    public List<ProjBean> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(List<ProjBean> supervisor) {
        this.supervisor = supervisor;
    }

    public List<ProjBean> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(List<ProjBean> maintenance) {
        this.maintenance = maintenance;
    }

    public List<ProjBean> getOdd() {
        return odd;
    }

    public void setOdd(List<ProjBean> odd) {
        this.odd = odd;
    }

    public static class ProjBean{
        private String title;
        private String address;
        private String type;
        private String price;
        private int project_id;
        private int evaluate_type;

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

        public int getEvaluate_type() {
            return evaluate_type;
        }

        public void setEvaluate_type(int evaluate_type) {
            this.evaluate_type = evaluate_type;
        }
    }
}
