package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/23.
 */

public class MyAppealDetailRes {
    private AppealBean appeal;

    public AppealBean getAppeal() {
        return appeal;
    }

    public void setAppeal(AppealBean appeal) {
        this.appeal = appeal;
    }

    public static class AppealBean{
        private String appeal_operate;
        private String content;
        private String remark;
        private int status;
        private List<String> images;

        public String getAppeal_operate() {
            return appeal_operate;
        }

        public void setAppeal_operate(String appeal_operate) {
            this.appeal_operate = appeal_operate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
