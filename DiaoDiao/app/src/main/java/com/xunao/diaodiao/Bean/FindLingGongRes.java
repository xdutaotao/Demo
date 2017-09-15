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

    private OddInfoBean odd_info;
    private boolean is_apply;
    private List<OddDrawingBean> odd_drawing;

    public OddInfoBean getOdd_info() {
        return odd_info;
    }

    public void setOdd_info(OddInfoBean odd_info) {
        this.odd_info = odd_info;
    }

    public boolean isIs_apply() {
        return is_apply;
    }

    public void setIs_apply(boolean is_apply) {
        this.is_apply = is_apply;
    }

    public List<OddDrawingBean> getOdd_drawing() {
        return odd_drawing;
    }

    public void setOdd_drawing(List<OddDrawingBean> odd_drawing) {
        this.odd_drawing = odd_drawing;
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
        private String create_time;
        private String publish_name;
        private String type;
        private String district;
        private String address;
        private String build_time;
        private String describe;
        private String total_day;
        private String daily_wage;
        private String service_fee;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPublish_name() {
            return publish_name;
        }

        public void setPublish_name(String publish_name) {
            this.publish_name = publish_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }
    }

    public static class OddDrawingBean {
        /**
         * image : http://admintest.diao-diao.com/upload/20170913232004_card1345.png
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
