package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class GetOddInfoRes {
    private OddInfoBean odd_info;
    private List<EvaluateInfoBean> evaluate_Info;

    public OddInfoBean getOdd_info() {
        return odd_info;
    }

    public void setOdd_info(OddInfoBean odd_info) {
        this.odd_info = odd_info;
    }

    public List<EvaluateInfoBean> getEvaluate_Info() {
        return evaluate_Info;
    }

    public void setEvaluate_Info(List<EvaluateInfoBean> evaluate_Info) {
        this.evaluate_Info = evaluate_Info;
    }

    public static class OddInfoBean{
        private String name;
        private String address;
        private String point;

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

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }
    }

    public static class EvaluateInfoBean{
        private String name;
        private String content;
        private String head_img;

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

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }
    }
}
