package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/11/1.
 */

public class ReleaseHelpReq implements Serializable {
    private int userid;
    private int type;
    private String title;
    private String contact;
    private String contact_mobile;
    private int province;
    private int city;
    private int district;
    private String address;
    private String describe;
    private List<String> images;
    private String door_fee;
    private long door_time;
    private String service_fee;
    private int project_class;
    private int project_brand;
    private int project_type;
    private String verify;
    private String buildTimeString;
    private String region;
    private String typeString;

    public int getProject_brand() {
        return project_brand;
    }

    public void setProject_brand(int project_brand) {
        this.project_brand = project_brand;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getBuildTimeString() {
        return buildTimeString;
    }

    public void setBuildTimeString(String buildTimeString) {
        this.buildTimeString = buildTimeString;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDoor_fee() {
        return door_fee;
    }

    public void setDoor_fee(String door_fee) {
        this.door_fee = door_fee;
    }

    public long getDoor_time() {
        return door_time;
    }

    public void setDoor_time(long door_time) {
        this.door_time = door_time;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public int getProject_class() {
        return project_class;
    }

    public void setProject_class(int project_class) {
        this.project_class = project_class;
    }

    public int getProject_type() {
        return project_type;
    }

    public void setProject_type(int project_type) {
        this.project_type = project_type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
