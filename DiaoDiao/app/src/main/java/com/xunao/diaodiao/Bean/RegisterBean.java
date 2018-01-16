package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/15 17:36.
 */

public class RegisterBean {
    private String code;
    private String password;
    private String mobile;
    private String verify;
    private int phone_type;

    public int getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(int phone_type) {
        this.phone_type = phone_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
