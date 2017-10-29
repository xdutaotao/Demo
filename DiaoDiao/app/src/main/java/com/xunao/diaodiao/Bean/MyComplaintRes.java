package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/16.
 */

public class MyComplaintRes {
    private List<Appeal> appeal;

    public List<Appeal> getAppeal() {
        return appeal;
    }

    public void setAppeal(List<Appeal> appeal) {
        this.appeal = appeal;
    }

    public static class  Appeal{
        private long appeal_order;
        private int status;
        private String title;
        private String content;
        private int project_id;
        private int project_type;
        private int appeal_id;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getAppeal_order() {
            return appeal_order;
        }

        public void setAppeal_order(long appeal_order) {
            this.appeal_order = appeal_order;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public int getProject_type() {
            return project_type;
        }

        public void setProject_type(int project_type) {
            this.project_type = project_type;
        }

        public int getAppeal_id() {
            return appeal_id;
        }

        public void setAppeal_id(int appeal_id) {
            this.appeal_id = appeal_id;
        }
    }
}
