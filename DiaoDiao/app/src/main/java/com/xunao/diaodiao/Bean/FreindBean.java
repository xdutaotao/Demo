package com.xunao.diaodiao.Bean;

import java.util.List;

public class FreindBean {

    /**
     * queryKey : 呵呵
     * address :
     * contacter :
     * dateline : 0
     * mobile :
     * name : 上汽
     * project : 1
     * disabled : 0
     * serviceScope :
     * shortname : 呵
     * users : [{"address":"rer","dateline":1490533898,"vipDateline":4070880000,"sex":0,"mobile":"18521090089","project":"1","experience":130,"type":0,"deviceToken":"","token":"","gold":195,"lastLoginTime":1490533910,"deviceRemark":"","face":"http://363600.cicp.net:8080/static/attachment/avatar/201703/20170319183529824_1489919729824.png","password":"","lastPasswordModifiedTime":0,"appid":"234qwer","name":"哈毛毛虫哈","disabled":0,"id":14,"VIP":1,"locked":0,"email":"Eeeee@qq.com"}]
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
    private List<UsersBean> users;

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

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean {
        /**
         * address : rer
         * dateline : 1490533898
         * vipDateline : 4070880000
         * sex : 0
         * mobile : 18521090089
         * project : 1
         * experience : 130
         * type : 0
         * deviceToken :
         * token :
         * gold : 195
         * lastLoginTime : 1490533910
         * deviceRemark :
         * face : http://363600.cicp.net:8080/static/attachment/avatar/201703/20170319183529824_1489919729824.png
         * password :
         * lastPasswordModifiedTime : 0
         * appid : 234qwer
         * name : 哈毛毛虫哈
         * disabled : 0
         * id : 14
         * VIP : 1
         * locked : 0
         * email : Eeeee@qq.com
         */

        private String address;
        private int dateline;
        private long vipDateline;
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
        private int VIP;
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

        public long getVipDateline() {
            return vipDateline;
        }

        public void setVipDateline(long vipDateline) {
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

        public int getVIP() {
            return VIP;
        }

        public void setVIP(int VIP) {
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
}
