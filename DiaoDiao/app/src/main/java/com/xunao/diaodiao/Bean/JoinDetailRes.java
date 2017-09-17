package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/17.
 */

public class JoinDetailRes {

    /**
     * company_info : {"name":"测试账号1","address":"上海上海长宁区长寿路118号","point":"4"}
     * evaluate_Info : [{"name":"宇**","content":"第一","head_img":"http://admintest.diao-diao.com/upload/20170912204135_head307.jpg"},{"name":"宇**","content":"第二","head_img":"http://admintest.diao-diao.com/upload/20170912204135_head307.jpg"}]
     */

    private CompanyInfoBean company_info;
    private List<EvaluateInfoBean> evaluate_Info;

    public CompanyInfoBean getCompany_info() {
        return company_info;
    }

    public void setCompany_info(CompanyInfoBean company_info) {
        this.company_info = company_info;
    }

    public List<EvaluateInfoBean> getEvaluate_Info() {
        return evaluate_Info;
    }

    public void setEvaluate_Info(List<EvaluateInfoBean> evaluate_Info) {
        this.evaluate_Info = evaluate_Info;
    }

    public static class CompanyInfoBean {
        /**
         * name : 测试账号1
         * address : 上海上海长宁区长寿路118号
         * point : 4
         */

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

    public static class EvaluateInfoBean {
        /**
         * name : 宇**
         * content : 第一
         * head_img : http://admintest.diao-diao.com/upload/20170912204135_head307.jpg
         */

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
