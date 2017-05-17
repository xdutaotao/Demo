package com.demo.cworker.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:49.
 */

public class HomeResponseBean {

    /**
     * banner : [{"groupType":0,"dateline":0,"keywords":"","author":"","bannerPicUrl":"","description":"","pageSize":0,"updateTime":0,"pic":"http://363600.cicp.net:8080/static/home/1.png","sort":0,"authorId":0,"title":"","type":1,"url":"http: //www.123.com2.http: //363600.cicp.net: 8080/static/document/123.xls","urlType":1,"vipRes":0,"pageNo":0,"price":0,"disabled":0,"id":1,"user":null}]
     * topic : [{"groupTitle":"jingHua","groupType":1,"groupData":[{"groupType":1,"dateline":0,"keywords":"","author":"","bannerPicUrl":"","description":"","pageSize":0,"updateTime":0,"pic":"http://363600.cicp.net:8080/static/home/16.jpg","sort":0,"authorId":0,"title":"","type":6,"url":"http://363600.cicp.net:8080/static/home/16.jpg","urlType":1,"vipRes":0,"pageNo":0,"price":0,"disabled":0,"id":8,"user":null}]},{"groupTitle":"caiNiXiHuan","groupType":2,"groupData":[]},{"groupTitle":"jingDian","groupType":3,"groupData":[]}]
     * msg : 200
     * result : success
     */

    private String msg;
    private String result;
    private List<BannerBean> banner;
    private List<TopicBean> topic;

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

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<TopicBean> getTopic() {
        return topic;
    }

    public void setTopic(List<TopicBean> topic) {
        this.topic = topic;
    }

    public static class BannerBean {
        /**
         * groupType : 0
         * dateline : 0
         * keywords :
         * author :
         * bannerPicUrl :
         * description :
         * pageSize : 0
         * updateTime : 0
         * pic : http://363600.cicp.net:8080/static/home/1.png
         * sort : 0
         * authorId : 0
         * title :
         * type : 1
         * url : http: //www.123.com2.http: //363600.cicp.net: 8080/static/document/123.xls
         * urlType : 1
         * vipRes : 0
         * pageNo : 0
         * price : 0
         * disabled : 0
         * id : 1
         * user : null
         */

        private int groupType;
        private int dateline;
        private String keywords;
        private String author;
        private String bannerPicUrl;
        private String description;
        private int pageSize;
        private int updateTime;
        private String pic;
        private int sort;
        private int authorId;
        private String title;
        private int type;
        private String url;
        private int urlType;
        private int vipRes;
        private int pageNo;
        private int price;
        private int disabled;
        private int id;
        private Object user;

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public int getDateline() {
            return dateline;
        }

        public void setDateline(int dateline) {
            this.dateline = dateline;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getBannerPicUrl() {
            return bannerPicUrl;
        }

        public void setBannerPicUrl(String bannerPicUrl) {
            this.bannerPicUrl = bannerPicUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUrlType() {
            return urlType;
        }

        public void setUrlType(int urlType) {
            this.urlType = urlType;
        }

        public int getVipRes() {
            return vipRes;
        }

        public void setVipRes(int vipRes) {
            this.vipRes = vipRes;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }
    }

    public static class TopicBean {
        /**
         * groupTitle : jingHua
         * groupType : 1
         * groupData : [{"groupType":1,"dateline":0,"keywords":"","author":"","bannerPicUrl":"","description":"","pageSize":0,"updateTime":0,"pic":"http://363600.cicp.net:8080/static/home/16.jpg","sort":0,"authorId":0,"title":"","type":6,"url":"http://363600.cicp.net:8080/static/home/16.jpg","urlType":1,"vipRes":0,"pageNo":0,"price":0,"disabled":0,"id":8,"user":null}]
         */

        private String groupTitle;
        private int groupType;
        private List<GroupDataBean> groupData;

        public String getGroupTitle() {
            return groupTitle;
        }

        public void setGroupTitle(String groupTitle) {
            this.groupTitle = groupTitle;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public List<GroupDataBean> getGroupData() {
            return groupData;
        }

        public void setGroupData(List<GroupDataBean> groupData) {
            this.groupData = groupData;
        }

        public static class GroupDataBean {
            /**
             * groupType : 1
             * dateline : 0
             * keywords :
             * author :
             * bannerPicUrl :
             * description :
             * pageSize : 0
             * updateTime : 0
             * pic : http://363600.cicp.net:8080/static/home/16.jpg
             * sort : 0
             * authorId : 0
             * title :
             * type : 6
             * url : http://363600.cicp.net:8080/static/home/16.jpg
             * urlType : 1
             * vipRes : 0
             * pageNo : 0
             * price : 0
             * disabled : 0
             * id : 8
             * user : null
             */

            private int groupType;
            private int dateline;
            private String keywords;
            private String author;
            private String bannerPicUrl;
            private String description;
            private int pageSize;
            private int updateTime;
            private String pic;
            private int sort;
            private int authorId;
            private String title;
            private int type;
            private String url;
            private int urlType;
            private int vipRes;
            private int pageNo;
            private int price;
            private int disabled;
            private int id;
            private Object user;

            public int getGroupType() {
                return groupType;
            }

            public void setGroupType(int groupType) {
                this.groupType = groupType;
            }

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getBannerPicUrl() {
                return bannerPicUrl;
            }

            public void setBannerPicUrl(String bannerPicUrl) {
                this.bannerPicUrl = bannerPicUrl;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getAuthorId() {
                return authorId;
            }

            public void setAuthorId(int authorId) {
                this.authorId = authorId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getUrlType() {
                return urlType;
            }

            public void setUrlType(int urlType) {
                this.urlType = urlType;
            }

            public int getVipRes() {
                return vipRes;
            }

            public void setVipRes(int vipRes) {
                this.vipRes = vipRes;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getDisabled() {
                return disabled;
            }

            public void setDisabled(int disabled) {
                this.disabled = disabled;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getUser() {
                return user;
            }

            public void setUser(Object user) {
                this.user = user;
            }
        }
    }
}
