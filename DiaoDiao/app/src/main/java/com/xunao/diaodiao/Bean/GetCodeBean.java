package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/8/30.
 */

public class GetCodeBean {
    private String mobile;
    private String verify;
    private int phone_type;

    public int getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(int phone_type) {
        this.phone_type = phone_type;
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
