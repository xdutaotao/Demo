package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/1.
 */

public class SelectRespBean {
    private String mobile;
    private int type;
    private int is_finish;

    public int getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(int is_finish) {
        this.is_finish = is_finish;
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
}
