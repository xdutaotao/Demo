package com.xunao.diaodiao.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjectRes implements Serializable{
    private List<FindProject> project;
    private List<FindProject> odd;

    public List<FindProject> getOdd() {
        return odd;
    }

    public void setOdd(List<FindProject> odd) {
        this.odd = odd;
    }

    public List<FindProject> getProject() {
        return project;
    }

    public void setProject(List<FindProject> project) {
        this.project = project;
    }

    public static class FindProject implements Serializable{
        private int id;
        private String title;
        private int build_time;
        private String desc;
        private String distance;
        private String type;
        private String price;
        private String total_day;
        private String daily_wage;
        private String url;
        private String issue_time;
        private int collected;
        private int apply;

        public int getCollected() {
            return collected;
        }

        public void setCollected(int collected) {
            this.collected = collected;
        }

        public int getApply() {
            return apply;
        }

        public void setApply(int apply) {
            this.apply = apply;
        }

        public String getIssue_time() {
            return issue_time;
        }

        public void setIssue_time(String issue_time) {
            this.issue_time = issue_time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getBuild_time() {
            return build_time;
        }

        public void setBuild_time(int build_time) {
            this.build_time = build_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotal_day() {
            return total_day;
        }

        public void setTotal_day(String total_day) {
            this.total_day = total_day;
        }

        public String getDaily_wage() {
            return daily_wage;
        }

        public void setDaily_wage(String daily_wage) {
            this.daily_wage = daily_wage;
        }
    }

}
