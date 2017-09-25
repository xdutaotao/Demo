package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/25 16:44.
 */

public class EvaluateReq {
    private int userid;
    private int type;
    private int project_id;
    private int project_type;
    private String point;
    private String content;
    private List<String> images;
    private String verify;

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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
