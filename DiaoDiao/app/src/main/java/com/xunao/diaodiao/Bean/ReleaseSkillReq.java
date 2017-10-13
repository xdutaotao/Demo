package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/28.
 */

public class ReleaseSkillReq implements Serializable{
    private int userid;
    private int type;
    private String title;
    private int province;
    private int city;
    private int district;
    private String address;
    private String project_type;
    private String contact;
    private String contact_mobile;
    private String daily_wage;
    private String total_day;
    private long build_time;
    private String describe;
    private String service_fee;
    private String odd_fee;
    private String total_fee;
    private List<String> images;
    private String verify;

    private String region;
    private String odd_id;

    public String getOdd_id() {
        return odd_id;
    }

    public void setOdd_id(String odd_id) {
        this.odd_id = odd_id;
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

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
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

    public String getDaily_wage() {
        return daily_wage;
    }

    public void setDaily_wage(String daily_wage) {
        this.daily_wage = daily_wage;
    }

    public String getTotal_day() {
        return total_day;
    }

    public void setTotal_day(String total_day) {
        this.total_day = total_day;
    }

    public long getBuild_time() {
        return build_time;
    }

    public void setBuild_time(long build_time) {
        this.build_time = build_time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getOdd_fee() {
        return odd_fee;
    }

    public void setOdd_fee(String odd_fee) {
        this.odd_fee = odd_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
