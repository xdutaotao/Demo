package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/8/25.
 */

public class LoginResBean {
    private long userid;
    private String name;
    private int type;
    private int is_frozen;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
