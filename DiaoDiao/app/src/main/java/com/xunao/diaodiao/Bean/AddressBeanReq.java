package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/27.
 */

public class AddressBeanReq {
    private String name;
    private int prarent_id;
    private int region_type;
    private String verify;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrarent_id() {
        return prarent_id;
    }

    public void setPrarent_id(int prarent_id) {
        this.prarent_id = prarent_id;
    }

    public int getRegion_type() {
        return region_type;
    }

    public void setRegion_type(int region_type) {
        this.region_type = region_type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
