package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/18.
 */

public class OrderSkillRes {
    private List<OddBean> odd;

    public List<OddBean> getOdd() {
        return odd;
    }

    public void setOdd(List<OddBean> odd) {
        this.odd = odd;
    }

    public static class OddBean{
        private int odd_id;
        private String title;
        private long publish_time;
        private String address;
        private String project_type;
        private String total_day;
        private String daily_wage;
        private int apply_count;
        private String url;

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

        public int getApply_count() {
            return apply_count;
        }

        public void setApply_count(int apply_count) {
            this.apply_count = apply_count;
        }
    }
}
