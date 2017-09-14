package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjReq {
    private int page;
    private int pageSize;
    private int district;
    private int minPrice;
    private int maxPrice;
    private String type;
    private int minBuildTime;
    private int maxBuildTime;
    private String lat;
    private String lng;
    private String verify;

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

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinBuildTime() {
        return minBuildTime;
    }

    public void setMinBuildTime(int minBuildTime) {
        this.minBuildTime = minBuildTime;
    }

    public int getMaxBuildTime() {
        return maxBuildTime;
    }

    public void setMaxBuildTime(int maxBuildTime) {
        this.maxBuildTime = maxBuildTime;
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
}
