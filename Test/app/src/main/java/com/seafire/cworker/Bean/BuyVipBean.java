package com.seafire.cworker.Bean;

import com.seafire.cworker.Model.UserInfo;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/22 20:06.
 */

public class BuyVipBean {
    private String msg;
    private String result;
    private UserInfo.PersonBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UserInfo.PersonBean getData() {
        return data;
    }

    public void setData(UserInfo.PersonBean data) {
        this.data = data;
    }
}
