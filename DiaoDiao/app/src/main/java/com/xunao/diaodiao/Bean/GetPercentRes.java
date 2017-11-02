package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by guzhenfu on 2017/9/27.
 */

public class GetPercentRes {
    private String percent;
    private String price;
    private String door_fee;
    private String service_fee;
    private String total_fee;
    private String supervisor_fee;

    public String getSupervisor_fee() {
        return supervisor_fee;
    }

    public void setSupervisor_fee(String supervisor_fee) {
        this.supervisor_fee = supervisor_fee;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getDoor_fee() {
        return door_fee;
    }

    public void setDoor_fee(String door_fee) {
        this.door_fee = door_fee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
