package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/19 13:14.
 */

public class SkillProjDetailRes {

    private List<OddBean> odd;

    public List<OddBean> getOdd() {
        return odd;
    }

    public void setOdd(List<OddBean> odd) {
        this.odd = odd;
    }

    public static class OddBean {
        /**
         * title : 空调系统找零工
         * region : 上海上海长宁区
         * address : 长乐路2220号
         * type : 空调系统
         * contact : 李四
         * contact_mobile : 123456789
         * daily_wage : 220
         * total_day : 3
         * build_time : 1506787200
         * describe : 就做空调系统，具体内容看图纸
         * images : ["http://admintest.diao-diao.com/upload/20170913232004_card1345.png","http://admintest.diao-diao.com/upload/20170913232004_card1345.png"]
         * odd_fee : 660
         * service_fee : 19.8
         */

        private String title;
        private String region;
        private String address;
        private String type;
        private String contact;
        private String contact_mobile;
        private String daily_wage;
        private String total_day;
        private String build_time;
        private String describe;
        private String odd_fee;
        private String service_fee;
        private List<String> images;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContact_mobile() {
            return contact_mobile;
        }

        public void setContact_mobile(String contact_mobile) {
            this.contact_mobile = contact_mobile;
        }

        public String getDaily_wage() {
            return daily_wage;
        }

        public void setDaily_wage(String daily_wage) {
            this.daily_wage = daily_wage;
        }

        public String getTotal_day() {
            return total_day;
        }

        public void setTotal_day(String total_day) {
            this.total_day = total_day;
        }

        public String getBuild_time() {
            return build_time;
        }

        public void setBuild_time(String build_time) {
            this.build_time = build_time;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getOdd_fee() {
            return odd_fee;
        }

        public void setOdd_fee(String odd_fee) {
            this.odd_fee = odd_fee;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
