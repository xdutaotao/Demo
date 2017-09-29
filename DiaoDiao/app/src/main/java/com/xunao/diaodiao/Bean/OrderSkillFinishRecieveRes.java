package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/18.
 */

public class OrderSkillFinishRecieveRes {
    private List<OddBean> odd;

    public List<OddBean> getOdd() {
        return odd;
    }

    public void setOdd(List<OddBean> odd) {
        this.odd = odd;
    }

    private List<OddBean> project;

    public List<OddBean> getProject() {
        return project;
    }

    public void setProject(List<OddBean> project) {
        this.project = project;
    }

    public static class OddBean{
        private int odd_id;
        private String title;
        private long publish_time;
        private String address;
        private String project_type;
        private String total_day;
        private String daily_wage;
        private long build_time;
        private int status;
        private long cancel_time;
        private long finish_time;
        private String url;

        private int project_id;
        private String project_price;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getOdd_id() {
            return odd_id;
        }

        public void setOdd_id(int odd_id) {
            this.odd_id = odd_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public long getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(long publish_time) {
            this.publish_time = publish_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getBuild_time() {
            return build_time;
        }

        public void setBuild_time(long build_time) {
            this.build_time = build_time;
        }

        public long getCancel_time() {
            return cancel_time;
        }

        public void setCancel_time(long cancel_time) {
            this.cancel_time = cancel_time;
        }

        public long getFinish_time() {
            return finish_time;
        }

        public void setFinish_time(long finish_time) {
            this.finish_time = finish_time;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getProject_price() {
            return project_price;
        }

        public void setProject_price(String project_price) {
            this.project_price = project_price;
        }
    }
}
