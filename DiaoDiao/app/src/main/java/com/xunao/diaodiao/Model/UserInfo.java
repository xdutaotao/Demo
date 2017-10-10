package com.xunao.diaodiao.Model;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/16 16:37.
 */

public class UserInfo {
    private int userid;
    private String mobile;
    private int type;
    private int is_frozen;
    private int has_binding;

    public int getHas_binding() {
        return has_binding;
    }

    public void setHas_binding(int has_binding) {
        this.has_binding = has_binding;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(int is_frozen) {
        this.is_frozen = is_frozen;
    }
}
