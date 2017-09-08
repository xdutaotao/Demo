package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/7.
 */

public class HasRateRes {
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
        private int evaluate_id;

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

        public int getEvaluate_id() {
            return evaluate_id;
        }

        public void setEvaluate_id(int evaluate_id) {
            this.evaluate_id = evaluate_id;
        }
    }
}
