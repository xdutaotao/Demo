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

    private CompanyInfoBean info;
    private List<EvaluateInfoBean> evaluates;

    public CompanyInfoBean getInfo() {
        return info;
    }

    public void setInfo(CompanyInfoBean info) {
        this.info = info;
    }

    public List<EvaluateInfoBean> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluateInfoBean> evaluates) {
        this.evaluates = evaluates;
    }


    public static class CompanyInfoBean {
        /**
         * name : 测试账号1
         * address : 上海上海长宁区长寿路118号
         * point : 4
         */

        private String title;
        private String region;
        private String point;
        private String map;

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
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
