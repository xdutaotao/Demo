package com.xunao.diaodiao.Bean;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/16 17:08.
 */

public class TestBean {

    /**
     * msg : 200
     * result : success
     * data : {"person":{"address":"demo","dateline":1488040189,"vipDateline":20,"sex":1,"mobile":"17091260755","project":"1","experience":2,"type":0,"deviceToken":"","token":"F525A704742F0C3A26D9293B6DD1109F","gold":3,"lastLoginTime":1489069120,"deviceRemark":"","face":"http://363600.cicp.net:8080/static/attachment/avatar/201703/20170309081028343_1489018228343.png","password":"","lastPasswordModifiedTime":0,"appid":"1","name":"Kenny2","disabled":0,"id":15,"VIP":false,"locked":0,"email":"Rrrrt@qq.com"},"project":{"queryKey":"哈哈","address":"","contacter":"","dateline":0,"mobile":"","name":"通用","project":"2","disabled":0,"serviceScope":"","shortname":"哈"},"ups":{"today":2,"month":2,"year":2,"total":2}}
     */

    private String msg;
    private String result;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * person : {"address":"demo","dateline":1488040189,"vipDateline":20,"sex":1,"mobile":"17091260755","project":"1","experience":2,"type":0,"deviceToken":"","token":"F525A704742F0C3A26D9293B6DD1109F","gold":3,"lastLoginTime":1489069120,"deviceRemark":"","face":"http://363600.cicp.net:8080/static/attachment/avatar/201703/20170309081028343_1489018228343.png","password":"","lastPasswordModifiedTime":0,"appid":"1","name":"Kenny2","disabled":0,"id":15,"VIP":false,"locked":0,"email":"Rrrrt@qq.com"}
         * project : {"queryKey":"哈哈","address":"","contacter":"","dateline":0,"mobile":"","name":"通用","project":"2","disabled":0,"serviceScope":"","shortname":"哈"}
         * ups : {"today":2,"month":2,"year":2,"total":2}
         */

        private PersonBean person;
        private ProjectBean project;
        private UpsBean ups;

        public PersonBean getPerson() {
            return person;
        }

        public void setPerson(PersonBean person) {
            this.person = person;
        }

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public UpsBean getUps() {
            return ups;
        }

        public void setUps(UpsBean ups) {
            this.ups = ups;
        }

        public static class PersonBean {
            /**
             * address : demo
             * dateline : 1488040189
             * vipDateline : 20
             * sex : 1
             * mobile : 17091260755
             * project : 1
             * experience : 2
             * type : 0
             * deviceToken :
             * token : F525A704742F0C3A26D9293B6DD1109F
             * gold : 3
             * lastLoginTime : 1489069120
             * deviceRemark :
             * face : http://363600.cicp.net:8080/static/attachment/avatar/201703/20170309081028343_1489018228343.png
             * password :
             * lastPasswordModifiedTime : 0
             * appid : 1
             * name : Kenny2
             * disabled : 0
             * id : 15
             * VIP : false
             * locked : 0
             * email : Rrrrt@qq.com
             */

            private String address;
            private int dateline;
            private int vipDateline;
            private int sex;
            private String mobile;
            private String project;
            private int experience;
            private int type;
            private String deviceToken;
            private String token;
            private int gold;
            private int lastLoginTime;
            private String deviceRemark;
            private String face;
            private String password;
            private int lastPasswordModifiedTime;
            private String appid;
            private String name;
            private int disabled;
            private int id;
            private boolean VIP;
            private int locked;
            private String email;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public int getVipDateline() {
                return vipDateline;
            }

            public void setVipDateline(int vipDateline) {
                this.vipDateline = vipDateline;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public int getExperience() {
                return experience;
            }

            public void setExperience(int experience) {
                this.experience = experience;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public int getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(int lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public String getDeviceRemark() {
                return deviceRemark;
            }

            public void setDeviceRemark(String deviceRemark) {
                this.deviceRemark = deviceRemark;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getLastPasswordModifiedTime() {
                return lastPasswordModifiedTime;
            }

            public void setLastPasswordModifiedTime(int lastPasswordModifiedTime) {
                this.lastPasswordModifiedTime = lastPasswordModifiedTime;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public boolean isVIP() {
                return VIP;
            }

            public void setVIP(boolean VIP) {
                this.VIP = VIP;
            }

            public int getLocked() {
                return locked;
            }

            public void setLocked(int locked) {
                this.locked = locked;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

        public static class ProjectBean {
            /**
             * queryKey : 哈哈
             * address :
             * contacter :
             * dateline : 0
             * mobile :
             * name : 通用
             * project : 2
             * disabled : 0
             * serviceScope :
             * shortname : 哈
             */

            private String queryKey;
            private String address;
            private String contacter;
            private int dateline;
            private String mobile;
            private String name;
            private String project;
            private int disabled;
            private String serviceScope;
            private String shortname;

            public String getQueryKey() {
                return queryKey;
            }

            public void setQueryKey(String queryKey) {
                this.queryKey = queryKey;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getContacter() {
                return contacter;
            }

            public void setContacter(String contacter) {
                this.contacter = contacter;
            }

            public int getDateline() {
                return dateline;
            }

            public void setDateline(int dateline) {
                this.dateline = dateline;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProject() {
                return project;
            }

            public void setProject(String project) {
                this.project = project;
            }

            public int getDisabled() {
                return disabled;
            }

            public void setDisabled(int disabled) {
                this.disabled = disabled;
            }

            public String getServiceScope() {
                return serviceScope;
            }

            public void setServiceScope(String serviceScope) {
                this.serviceScope = serviceScope;
            }

            public String getShortname() {
                return shortname;
            }

            public void setShortname(String shortname) {
                this.shortname = shortname;
            }
        }

        public static class UpsBean {
            /**
             * today : 2
             * month : 2
             * year : 2
             * total : 2
             */

            private int today;
            private int month;
            private int year;
            private int total;

            public int getToday() {
                return today;
            }

            public void setToday(int today) {
                this.today = today;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }
}
