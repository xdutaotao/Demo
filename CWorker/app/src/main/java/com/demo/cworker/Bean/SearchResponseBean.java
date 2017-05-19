package com.demo.cworker.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/18 20:32.
 */

public class SearchResponseBean {

    /**
     * msg : 200
     * result : success
     * count : 2
     * data : [{"groupType":0,"dateline":1487174531,"keywords":"","author":"海火科技","bannerPicUrl":"http: //363600.cicp.net: 8080/static/home/banner/b1.jpg","description":"三生三世，共守相思，开启桃花新纪元。那一世，大荒之中一处荒山，成就她与他的初见。桃花灼灼，枝叶蓁蓁，妖娆伤眼。记忆可以封存，可心有时也会背叛，忘得了前世情缘，忘不了桃林十里，亦忘不了十里桃林中玄衣的少年。这一世，东海水晶宫，他们不期而遇。不是每个人都能看透这三生三世的爱恨交织，只要你还在，只要我还爱，那么，这世间，刀山火海，毫不畏惧。有些爱，藏在嘴边，挂在心尖。浮生若梦，情如流水，爱似桃花\u2026\u2026","pageSize":0,"project":"1","updateTime":1487179531,"pic":"http: //363600.cicp.net: 8080/static/home/banner/b1.jpg","sort":0,"authorId":0,"title":"123%","type":7,"url":"https: //m.baidu.com","urlType":1,"vipRes":1,"pageNo":0,"price":0,"disabled":0,"id":1,"user":null},{"groupType":3,"dateline":1487174531,"keywords":"","author":"海火科技","bannerPicUrl":"","description":"三生三世，共守相思，开启桃花新纪元。那一世，大荒之中一处荒山，成就她与他的初见。桃花灼灼，枝叶蓁蓁，妖娆伤眼。记忆可以封存，可心有时也会背叛，忘得了前世情缘，忘不了桃林十里，亦忘不了十里桃林中玄衣的少年。这一世，东海水晶宫，他们不期而遇。不是每个人都能看透这三生三世的爱恨交织，只要你还在，只要我还爱，那么，这世间，刀山火海，毫不畏惧。有些爱，藏在嘴边，挂在心尖。浮生若梦，情如流水，爱似桃花\u2026\u2026","pageSize":0,"project":"","updateTime":1487179531,"pic":"http: //363600.cicp.net: 8080/static/home/7.jpg","sort":0,"authorId":0,"title":"123%","type":1,"url":"http: //363600.cicp.net: 8080/static/document/教程1.ppt","urlType":2,"vipRes":0,"pageNo":0,"price":0,"disabled":0,"id":15,"user":null}]
     */

    private String msg;
    private String result;
    private int count;
    private List<HomeResponseBean.TopicBean.GroupDataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<HomeResponseBean.TopicBean.GroupDataBean> getData() {
        return data;
    }

    public void setData(List<HomeResponseBean.TopicBean.GroupDataBean> data) {
        this.data = data;
    }

//    public static class DataBean {
//        /**
//         * groupType : 0
//         * dateline : 1487174531
//         * keywords :
//         * author : 海火科技
//         * bannerPicUrl : http: //363600.cicp.net: 8080/static/home/banner/b1.jpg
//         * description : 三生三世，共守相思，开启桃花新纪元。那一世，大荒之中一处荒山，成就她与他的初见。桃花灼灼，枝叶蓁蓁，妖娆伤眼。记忆可以封存，可心有时也会背叛，忘得了前世情缘，忘不了桃林十里，亦忘不了十里桃林中玄衣的少年。这一世，东海水晶宫，他们不期而遇。不是每个人都能看透这三生三世的爱恨交织，只要你还在，只要我还爱，那么，这世间，刀山火海，毫不畏惧。有些爱，藏在嘴边，挂在心尖。浮生若梦，情如流水，爱似桃花……
//         * pageSize : 0
//         * project : 1
//         * updateTime : 1487179531
//         * pic : http: //363600.cicp.net: 8080/static/home/banner/b1.jpg
//         * sort : 0
//         * authorId : 0
//         * title : 123%
//         * type : 7
//         * url : https: //m.baidu.com
//         * urlType : 1
//         * vipRes : 1
//         * pageNo : 0
//         * price : 0
//         * disabled : 0
//         * id : 1
//         * user : null
//         */
//
//        private int groupType;
//        private int dateline;
//        private String keywords;
//        private String author;
//        private String bannerPicUrl;
//        private String description;
//        private int pageSize;
//        private String project;
//        private int updateTime;
//        private String pic;
//        private int sort;
//        private int authorId;
//        private String title;
//        private int type;
//        private String url;
//        private int urlType;
//        private int vipRes;
//        private int pageNo;
//        private int price;
//        private int disabled;
//        private int id;
//        private Object user;
//
//        public int getGroupType() {
//            return groupType;
//        }
//
//        public void setGroupType(int groupType) {
//            this.groupType = groupType;
//        }
//
//        public int getDateline() {
//            return dateline;
//        }
//
//        public void setDateline(int dateline) {
//            this.dateline = dateline;
//        }
//
//        public String getKeywords() {
//            return keywords;
//        }
//
//        public void setKeywords(String keywords) {
//            this.keywords = keywords;
//        }
//
//        public String getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(String author) {
//            this.author = author;
//        }
//
//        public String getBannerPicUrl() {
//            return bannerPicUrl;
//        }
//
//        public void setBannerPicUrl(String bannerPicUrl) {
//            this.bannerPicUrl = bannerPicUrl;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public int getPageSize() {
//            return pageSize;
//        }
//
//        public void setPageSize(int pageSize) {
//            this.pageSize = pageSize;
//        }
//
//        public String getProject() {
//            return project;
//        }
//
//        public void setProject(String project) {
//            this.project = project;
//        }
//
//        public int getUpdateTime() {
//            return updateTime;
//        }
//
//        public void setUpdateTime(int updateTime) {
//            this.updateTime = updateTime;
//        }
//
//        public String getPic() {
//            return pic;
//        }
//
//        public void setPic(String pic) {
//            this.pic = pic;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//
//        public int getAuthorId() {
//            return authorId;
//        }
//
//        public void setAuthorId(int authorId) {
//            this.authorId = authorId;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public int getUrlType() {
//            return urlType;
//        }
//
//        public void setUrlType(int urlType) {
//            this.urlType = urlType;
//        }
//
//        public int getVipRes() {
//            return vipRes;
//        }
//
//        public void setVipRes(int vipRes) {
//            this.vipRes = vipRes;
//        }
//
//        public int getPageNo() {
//            return pageNo;
//        }
//
//        public void setPageNo(int pageNo) {
//            this.pageNo = pageNo;
//        }
//
//        public int getPrice() {
//            return price;
//        }
//
//        public void setPrice(int price) {
//            this.price = price;
//        }
//
//        public int getDisabled() {
//            return disabled;
//        }
//
//        public void setDisabled(int disabled) {
//            this.disabled = disabled;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public Object getUser() {
//            return user;
//        }
//
//        public void setUser(Object user) {
//            this.user = user;
//        }
//    }
}
