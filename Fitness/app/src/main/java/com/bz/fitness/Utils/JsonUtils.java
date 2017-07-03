package com.bz.fitness.Utils;

import com.bz.fitness.Bean.BaseResponseBean;
import com.bz.fitness.Bean.CollectBean;
import com.bz.fitness.Bean.LoginBean;
import com.bz.fitness.Bean.LoginResponseBean;
import com.bz.fitness.Bean.RegisterBean;
import com.bz.fitness.Bean.UploadBean;
import com.bz.fitness.Model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

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

    public BaseResponseBean JsonToUpdateFile(String json){
        return gson.fromJson(json, BaseResponseBean.class);
    }

    public String ListToJson(List<String> data){
        return gson.toJson(data);
    }

    public UploadBean JsonToUploadBean(String json){
        return gson.fromJson(json, UploadBean.class);
    }

    public List<String> JsonToList(String json){
        return gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
    }

    public List<CollectBean> JsonToCollectList(String json){
        return gson.fromJson(json, new TypeToken<List<CollectBean>>() {}.getType());
    }

    public String CollectListToJson(List<CollectBean> list){
        return gson.toJson(list);
    }

}
