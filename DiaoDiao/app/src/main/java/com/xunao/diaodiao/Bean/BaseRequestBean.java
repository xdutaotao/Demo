package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/8/25.
 */

public class BaseRequestBean<T> {
    private String cmd;
    private long ts;
    private T params;

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

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
