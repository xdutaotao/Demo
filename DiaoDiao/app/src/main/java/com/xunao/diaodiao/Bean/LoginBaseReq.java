package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class LoginBaseReq {
    private int userid;
    private String verify;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
