package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class FindProjectRes {
    private List<FindProject> project;

    public List<FindProject> getProject() {
        return project;
    }

    public void setProject(List<FindProject> project) {
        this.project = project;
    }

    public static class FindProject{
        private int id;
        private String title;
        private int build_time;
        private String desc;
        private String distance;
        private String type;
        private String price;
        private String total_day;

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
    }

}
