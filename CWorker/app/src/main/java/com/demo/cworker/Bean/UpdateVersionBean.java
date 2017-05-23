package com.demo.cworker.Bean;

import java.util.List;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/23 17:43.
 */

public class UpdateVersionBean {

    /**
     * msg : 200
     * result : 获取成功!
     * data : [{"lastVersion":"","forcedUpdate":0,"downloadUrl":"","remark":"","id":1,"version":"","appSize":"","platform":"ios"}]
     */

    private String msg;
    private String result;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lastVersion :
         * forcedUpdate : 0
         * downloadUrl :
         * remark :
         * id : 1
         * version :
         * appSize :
         * platform : ios
         */

        private String lastVersion;
        private int forcedUpdate;
        private String downloadUrl;
        private String remark;
        private int id;
        private String version;
        private String appSize;
        private String platform;

        public String getLastVersion() {
            return lastVersion;
        }

        public void setLastVersion(String lastVersion) {
            this.lastVersion = lastVersion;
        }

        public int getForcedUpdate() {
            return forcedUpdate;
        }

        public void setForcedUpdate(int forcedUpdate) {
            this.forcedUpdate = forcedUpdate;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppSize() {
            return appSize;
        }

        public void setAppSize(String appSize) {
            this.appSize = appSize;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }
}
