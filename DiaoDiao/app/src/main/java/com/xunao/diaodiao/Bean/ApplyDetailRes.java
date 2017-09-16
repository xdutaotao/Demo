package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class ApplyDetailRes {
    private String name;
    private String point;
    private String experience;
    private String project_type;
    private int project_amount;
    private String evaluate;
    private String mobile;
    private List<EvaluateBean> evaluates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public int getProject_amount() {
        return project_amount;
    }

    public void setProject_amount(int project_amount) {
        this.project_amount = project_amount;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<EvaluateBean> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluateBean> evaluates) {
        this.evaluates = evaluates;
    }

    public static class EvaluateBean{
        private String head_img;
        private String name;
        private String content;

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
