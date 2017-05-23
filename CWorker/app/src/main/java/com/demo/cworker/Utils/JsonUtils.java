package com.demo.cworker.Utils;

import com.demo.cworker.Bean.LoginBean;
import com.demo.cworker.Bean.LoginResponseBean;
import com.demo.cworker.Bean.RegisterBean;
import com.demo.cworker.Bean.UpdateFileBean;
import com.demo.cworker.Model.UserInfo;
import com.google.gson.Gson;

/**
 * Description:
 * Created by guzhenfu on 2016/11/21 17:02.
 */
public class JsonUtils {

    private JsonUtils(){
        initializeGson();
    }

    private static class SingletonHolder {
        private static final JsonUtils INSTANCE = new JsonUtils();
    }

    public static final JsonUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Gson gson;

    private Gson initializeGson(){
        if (gson==null){
            gson = new Gson();
        }
        return gson;
    }

    public String RegisterBeanToJson(RegisterBean bean){
        return gson.toJson(bean);
    }

    public String LoginBeanToJson(LoginBean bean){
        return gson.toJson(bean);
    }

    public LoginResponseBean jsonToLoginResponseBean(String json){
        return gson.fromJson(json, LoginResponseBean.class);
    }

    public UserInfo JsonToUserInfo(String json){
        return gson.fromJson(json, UserInfo.class);
    }

    public String UserInfoToJson(UserInfo userInfo){
        return gson.toJson(userInfo);
    }

    public UpdateFileBean JsonToUpdateFile(String json){
        return gson.fromJson(json, UpdateFileBean.class);
    }

}
