package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/8/25.
 */

public class LoginResBean {
    private int userid;
    private String mobile;
    private int type;
    private int is_frozen;

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
