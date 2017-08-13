package com.xunao.diaodiao.Bean;

import java.io.Serializable;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/18 20:06.
 */

public class SearchBean implements Serializable {
    private int groupType;
    private int pageNo;
    private int pageSize;
    private String keywords;
    private String token;
    private int type;
    private int vipRes;
    private int sort;
    private String project;

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVipRes() {
        return vipRes;
    }

    public void setVipRes(int vipRes) {
        this.vipRes = vipRes;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
