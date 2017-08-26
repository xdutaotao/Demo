package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/8/25.
 */

public class BaseRequestBean {
    private String cmd;
    private long ts;
    private String params;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
