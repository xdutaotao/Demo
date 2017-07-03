package com.bz.fitness.Bean;

import android.text.TextUtils;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/16 16:14.
 */

public class ResponseBean {
    /**
     * msg : 200
     * result : {"token":"040743C11251760F3F5487DA464FE183","time":1484532681}
     */

    private String msg;
    private ResultBean result;

    public boolean isError() {
        //if (TextUtils.equals("MSG00000", CODE) || TextUtils.equals("00000", CODE)){
        if(TextUtils.isEmpty(msg)){
            return true;
        }
        if (TextUtils.equals(msg, "200")){
            return false;
        }else{
            return true;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * token : 040743C11251760F3F5487DA464FE183
         * time : 1484532681
         */

        private String token;
        private int time;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
