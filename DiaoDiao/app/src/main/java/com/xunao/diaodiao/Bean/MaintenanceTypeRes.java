package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/10/31.
 */

public class MaintenanceTypeRes {

    private List<RepairBean> repair;
    private List<RepairBean> maintain;

    public List<RepairBean> getRepair() {
        return repair;
    }

    public void setRepair(List<RepairBean> repair) {
        this.repair = repair;
    }

    public List<RepairBean> getMaintain() {
        return maintain;
    }

    public void setMaintain(List<RepairBean> maintain) {
        this.maintain = maintain;
    }

    public static class RepairBean implements Serializable{
        private String class_id;
        private String class_name;
        private List<BrandBean> brands;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public List<BrandBean> getBrands() {
            return brands;
        }

        public void setBrands(List<BrandBean> brands) {
            this.brands = brands;
        }

        public static class BrandBean implements Serializable{
            private String brand_id;
            private String brand_name;

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }
        }

    }

}
