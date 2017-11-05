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
    private List<FindProject> maintenance;
    private List<FindProject> supervisor;
    private List<FindProject> mutual;
    private List<FindProject> info;

    public List<FindProject> getInfo() {
        return info;
    }

    public void setInfo(List<FindProject> info) {
        this.info = info;
    }

    public List<FindProject> getMutual() {
        return mutual;
    }

    public void setMutual(List<FindProject> mutual) {
        this.mutual = mutual;
    }

    public List<FindProject> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(List<FindProject> supervisor) {
        this.supervisor = supervisor;
    }

    public List<FindProject> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(List<FindProject> maintenance) {
        this.maintenance = maintenance;
    }

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
        private long build_time;
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
        private long publish_time;
        private String name;
        private String address;
        private String tel;
        private String plat_point;
        private String user_point;
        private String contact_mobile;

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPlat_point() {
            return plat_point;
        }

        public void setPlat_point(String plat_point) {
            this.plat_point = plat_point;
        }

        public String getUser_point() {
            return user_point;
        }

        public void setUser_point(String user_point) {
            this.user_point = user_point;
        }

        public long getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(long publish_time) {
            this.publish_time = publish_time;
        }

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

        public long getBuild_time() {
            return build_time;
        }

        public void setBuild_time(long build_time) {
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
