package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/5.
 */

public class GetMoneyReq implements Serializable{
    private int userid;
    private int type;
    private String verify;
    private int id;
    private String content;
    private int page;
    private int pageSize;
    private int project_id;
    private int project_type;
    private int odd_id;
    private int work_id;
    private String location;
    private List<String> images;
    private int works_id;
    private String remark;
    private int audit;
    private long sign_time;
    private int stage;
    private String reason;
    private int appeal_id;
    private int collect_id;
    private int user_type;
    private String type_ids;
    private String appeal_operate;
    private String openID;
    private String technician_id;
    private String device_token;
    private int device_type;
    private String door_fee;
    private String supervisor_fee;
    private int maintenance_id;
    private int apply_type;
    private int supervisor_id;
    private int mutual_id;
    private int company_id;

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getMutual_id() {
        return mutual_id;
    }

    public void setMutual_id(int mutual_id) {
        this.mutual_id = mutual_id;
    }

    public int getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public int getApply_type() {
        return apply_type;
    }

    public void setApply_type(int apply_type) {
        this.apply_type = apply_type;
    }

    public int getMaintenance_id() {
        return maintenance_id;
    }

    public void setMaintenance_id(int maintenance_id) {
        this.maintenance_id = maintenance_id;
    }

    public String getSupervisor_fee() {
        return supervisor_fee;
    }

    public void setSupervisor_fee(String supervisor_fee) {
        this.supervisor_fee = supervisor_fee;
    }

    public String getDoor_fee() {
        return door_fee;
    }

    public void setDoor_fee(String door_fee) {
        this.door_fee = door_fee;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    public String getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(String technician_id) {
        this.technician_id = technician_id;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getAppeal_operate() {
        return appeal_operate;
    }

    public void setAppeal_operate(String appeal_operate) {
        this.appeal_operate = appeal_operate;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getCollect_id() {
        return collect_id;
    }

    public void setCollect_id(int collect_id) {
        this.collect_id = collect_id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public int getOdd_id() {
        return odd_id;
    }

    public void setOdd_id(int odd_id) {
        this.odd_id = odd_id;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getWorks_id() {
        return works_id;
    }

    public void setWorks_id(int works_id) {
        this.works_id = works_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public long getSign_time() {
        return sign_time;
    }

    public void setSign_time(long sign_time) {
        this.sign_time = sign_time;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAppeal_id() {
        return appeal_id;
    }

    public void setAppeal_id(int appeal_id) {
        this.appeal_id = appeal_id;
    }

    public String getType_ids() {
        return type_ids;
    }

    public void setType_ids(String type_ids) {
        this.type_ids = type_ids;
    }
}
