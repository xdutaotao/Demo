package com.xunao.diaodiao.Bean;

import java.util.List;

/**
 * Description:
 * Created by guzhenfu on 2017/9/21.
 */

public class SignRes {
    private List<SignBean> sign;

    public List<SignBean> getSign() {
        return sign;
    }

    public void setSign(List<SignBean> sign) {
        this.sign = sign;
    }

    public static class SignBean{
        private long date;
        private String location;
        private List<String> images;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
