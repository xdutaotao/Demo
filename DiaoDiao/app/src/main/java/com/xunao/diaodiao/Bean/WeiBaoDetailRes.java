package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/11/4.
 */

public class WeiBaoDetailRes {
    private MaintenanceBean maintenance;
    private WBDetailBean detail;

    public MaintenanceBean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(MaintenanceBean maintenance) {
        this.maintenance = maintenance;
    }

    public WBDetailBean getDetail() {
        return detail;
    }

    public void setDetail(WBDetailBean detail) {
        this.detail = detail;
    }

    public static class MaintenanceBean implements Serializable{
        private String title;
        private String address;
        private String contact;
        private String contact_mobile;
        private String describe;
        private List<String> images;
        private String service_fee;
        private String verify;
        private String region;
        private String equip_type;
        private String door_fee;
        private String door_time;

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

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getEquip_type() {
            return equip_type;
        }

        public void setEquip_type(String equip_type) {
            this.equip_type = equip_type;
        }

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public String getDoor_time() {
            return door_time;
        }

        public void setDoor_time(String door_time) {
            this.door_time = door_time;
        }
    }

    public static class WBDetailBean implements Serializable {
        private int project_type;
        private int project_class;
        private int project_brand;
        private String title;
        private int province;
        private int city;
        private int district;
        private String address;
        private String contact;
        private String contact_mobile;
        private String describe;
        private List<String> images;
        private String service_fee;
        private String verify;
        private String region;
        private String project_type_name;
        private String project_type_class;
        private String project_type_brand;
        private String door_fee;
        private String door_time;
        private int maintenance_id;

        public int getProject_type() {
            return project_type;
        }

        public void setProject_type(int project_type) {
            this.project_type = project_type;
        }

        public int getProject_class() {
            return project_class;
        }

        public void setProject_class(int project_class) {
            this.project_class = project_class;
        }

        public int getProject_brand() {
            return project_brand;
        }

        public void setProject_brand(int project_brand) {
            this.project_brand = project_brand;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getProject_type_name() {
            return project_type_name;
        }

        public void setProject_type_name(String project_type_name) {
            this.project_type_name = project_type_name;
        }

        public String getProject_type_class() {
            return project_type_class;
        }

        public void setProject_type_class(String project_type_class) {
            this.project_type_class = project_type_class;
        }

        public String getProject_type_brand() {
            return project_type_brand;
        }

        public void setProject_type_brand(String project_type_brand) {
            this.project_type_brand = project_type_brand;
        }

        public String getDoor_fee() {
            return door_fee;
        }

        public void setDoor_fee(String door_fee) {
            this.door_fee = door_fee;
        }

        public String getDoor_time() {
            return door_time;
        }

        public void setDoor_time(String door_time) {
            this.door_time = door_time;
        }

        public int getMaintenance_id() {
            return maintenance_id;
        }

        public void setMaintenance_id(int maintenance_id) {
            this.maintenance_id = maintenance_id;
        }
    }
}
