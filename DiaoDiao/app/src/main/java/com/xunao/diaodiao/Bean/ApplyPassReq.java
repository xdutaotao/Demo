package com.xunao.diaodiao.Bean;

import java.io.Serializable;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class ApplyPassReq implements Serializable{
    private int technician_id;
    private int project_id;
    private int project_type;
    private String verify;


    public int getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(int technician_id) {
        this.technician_id = technician_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getProject_type() {
        return project_type;
    }

    public void setProject_type(int project_type) {
        this.project_type = project_type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
