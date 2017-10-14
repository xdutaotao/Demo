package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjReq {
    private int page;
    private int pageSize;
    private int type;
    private int nearby;
    private int time_type;
    private String lat;
    private String lng;
    private String verify;
    private String city;
    private String keyword;
    private int userid;
    private int usertype;
    private int project_type;

    public int getProject_type() {
        return project_type;
    }

    public void setProject_type(int project_type) {
        this.project_type = project_type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getType() {
        return type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public int getNearby() {
        return nearby;
    }

    public void setNearby(int nearby) {
        this.nearby = nearby;
    }

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
    }
}
