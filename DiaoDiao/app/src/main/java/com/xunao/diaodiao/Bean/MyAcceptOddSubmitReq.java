package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/20.
 */

public class MyAcceptOddSubmitReq {
    private int userid;
    private int type;
    private int odd_id;
    private int apply_type;
    private String remark;
    private long sign_time;
    private String location;
    private List<String> images;
    private String verify;

    public int getApply_type() {
        return apply_type;
    }

    public void setApply_type(int apply_type) {
        this.apply_type = apply_type;
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

    public int getOdd_id() {
        return odd_id;
    }

    public void setOdd_id(int odd_id) {
        this.odd_id = odd_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getSign_time() {
        return sign_time;
    }

    public void setSign_time(long sign_time) {
        this.sign_time = sign_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
