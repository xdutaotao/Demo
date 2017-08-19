package com.demo.step.Model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.demo.step.Common.Constants;
import com.demo.step.Utils.JsonUtils;
import com.demo.step.Utils.ShareUtils;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/16 16:35.
 */

public class User {
    private static class SingletonHolder {
        private static final User INSTANCE = new User();
    }

    public static final User getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private UserInfo userInfo;
    private String token;

    public  String getUserId(){
        if (token == null){
            token = ShareUtils.getValue(Constants.TOKEN_KEY, null);
            return token;
        }else{
            if (!TextUtils.equals(token, ShareUtils.getValue(Constants.TOKEN_KEY, null))){
                token = ShareUtils.getValue(Constants.TOKEN_KEY, null);
            }
            return token;
        }
    }

    /**
     * 设置用户Token, 只能UserModel类修改和UserModel同一个包下
     * @param s
     */
    void setUserId(@NonNull String s){
        token = s;
        ShareUtils.putValue(Constants.TOKEN_KEY, s);
    }

    /**
     * 得到用户信息，为空就要再次请求网络
     * @return
     */
    public  UserInfo getUserInfo(){
        if (userInfo == null){
            String s = ShareUtils.getValue(Constants.USER_INFO_KEY, null);
            if (s != null){
                userInfo = JsonUtils.getInstance().JsonToUserInfo(s);
                return userInfo;
            }else{
                return null;
            }
        }else{
            String s = ShareUtils.getValue(Constants.USER_INFO_KEY, null);
            if (s == null){
                s = JsonUtils.getInstance().UserInfoToJson(userInfo);
                ShareUtils.putValue(Constants.USER_INFO_KEY, s);
            }
            return userInfo;
        }
    }

    /**
     * 只能UserModel类修改和UserModel同一个包下
     * @param info
     */
    void setUserInfo(String info){
        userInfo = JsonUtils.getInstance().JsonToUserInfo(info);
        ShareUtils.putValue(Constants.USER_INFO_KEY, info);
    }

    /**
     * 这个权限也应该被限制，应该对其他用户透明，待定？？？？
     */
    public void clearUser(){
        token = null;
        userInfo = null;
        ShareUtils.putValue(Constants.USER_INFO_KEY, null);
        ShareUtils.putValue(Constants.TOKEN_KEY, null);
    }

}
