package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjReq {
    private int page;
    private int pageSize;
    private String type;
    private int nearby;
    private int time_type;
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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
