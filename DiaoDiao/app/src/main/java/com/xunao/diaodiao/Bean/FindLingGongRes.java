package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/9/15 21:31.
 */

public class FindLingGongRes {


    /**
     * odd_info : {"title":"空调系统找零工","create_time":"1505390733","publish_name":"宇**","type":"空调系统","district":"上海上海长宁区","address":"长乐路2220号","build_time":"1506787200","describe":"就做空调系统，具体内容看图纸","total_day":"3","daily_wage":"220","service_fee":"19.8"}
     * odd_drawing : [{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"},{"image":"http://admintest.diao-diao.com/upload/20170913232004_card1345.png"}]
     * is_apply : false
     */

    private DetailBean detail;
    private OddInfoBean odd;

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public OddInfoBean getOdd() {
        return odd;
    }

    public void setOdd(OddInfoBean odd) {
        this.odd = odd;
    }

    public static class OddInfoBean {
        /**
         * title : 空调系统找零工
         * create_time : 1505390733
         * publish_name : 宇**
         * type : 空调系统
         * district : 上海上海长宁区
         * address : 长乐路2220号
         * build_time : 1506787200
         * describe : 就做空调系统，具体内容看图纸
         * total_day : 3
         * daily_wage : 220
         * service_fee : 19.8
         */

        private String title;
        private String region;
        private String address;
        private String type;
        private String contact;
        private String contact_mobile;
        private String total_day;
        private String daily_wage;
        private long build_time;
        private String describe;
        private List<String> images;
        private String odd_fee;
        private String service_fee;
        private int province;
        private int city;
        private int district;
        private String total_fee;

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

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

        public long getBuild_time() {
            return build_time;
        }

        public void setBuild_time(long build_time) {
            this.build_time = build_time;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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
    }


    public static class DetailBean {
        /**
         * title : 空调系统找零工
         * create_time : 1505390733
         * publish_name : 宇**
         * type : 空调系统
         * district : 上海上海长宁区
         * address : 长乐路2220号
         * build_time : 1506787200
         * describe : 就做空调系统，具体内容看图纸
         * total_day : 3
         * daily_wage : 220
         * service_fee : 19.8
         */

        private String title;
        private String region;
        private String address;
        private String type;
        private String contact;
        private String contact_mobile;
        private String total_day;
        private String daily_wage;
        private long build_time;
        private String describe;
        private List<String> images;
        private String odd_fee;
        private String service_fee;
        private int province;
        private int city;
        private int district;
        private String total_fee;

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

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

        public long getBuild_time() {
            return build_time;
        }

        public void setBuild_time(long build_time) {
            this.build_time = build_time;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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
    }




}
