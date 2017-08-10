package com.seafire.cworker.Bean;

/**
 * Description:
 * Created by guzhenfu on 17/6/24.
 */

public class RxBusEvent {
    private String msg;
    private String type;

    public RxBusEvent(String type, String msg) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
