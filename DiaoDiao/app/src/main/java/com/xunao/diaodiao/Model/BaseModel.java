package com.xunao.diaodiao.Model;

import com.xunao.diaodiao.Bean.BaseRequestBean;
import com.xunao.diaodiao.Common.RetrofitConfig;
import com.xunao.diaodiao.Utils.JsonUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseModel {

    protected RetrofitConfig config;

    public BaseModel() {
        config = RetrofitConfig.getInstance();
    }

    public RequestBody setPicBody(String s) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json"), s);
    }

    public RequestBody setBody(String cmd, String params){
        BaseRequestBean bean = new BaseRequestBean();
        bean.setCmd(cmd);
        bean.setParams(params);
        bean.setTs(System.currentTimeMillis()/1000);
        String s = JsonUtils.getInstance().BaseRequestBeanToJson(bean);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),s);
    }

}