package com.demo.cworker.Model;

import android.text.TextUtils;

import com.demo.cworker.Bean.UpdateFileBean;
import com.demo.cworker.Bean.UpdateVersionBean;
import com.demo.cworker.Common.Constants;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.RxUtils;
import com.demo.cworker.Utils.ShareUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by
 */
@Singleton
public class LoginModel extends BaseModel {
    @Inject
    public LoginModel() {}

    /**
     * 登录验证手机号
     * @param phone
     * @return
     */
    public Observable<String> checkPhone(String phone){
        return config.getRetrofitService().checkPhone(phone)
                .compose(RxUtils.handleResult());
    }

    /**
     * 注册
     * @param email
     * @param pwd
     * @param phone
     * @param name
     * @return
     */
    public Observable<String> checkEmail(String email, String pwd, String phone, String name){
        return config.getRetrofitService().checkEmail(email)
                .compose(RxUtils.handleResultNoThread())
                .flatMap(s -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("password", pwd);
                    map.put("mobile", phone);
                    map.put("email", email);
                    return config.getRetrofitService().register(map)
                            .compose(RxUtils.handleResultNoThread());
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 登录
     * @param phone
     * @param pwd
     * @param cliendID
     * @return
     */
    public Observable<String> login(String phone, String pwd, String cliendID){
        Map<String, String> map = new HashMap<>();
        map.put("username", phone);
        map.put("password", pwd);
        map.put("clientId", cliendID);

        return config.getRetrofitService().login(map)
                .flatMap(testBean -> {
                    String s = testBean.getResult().getToken();
                    User.getInstance().setUserId(s);

                    map.clear();
                    map.put("token", s);
                    return config.getRetrofitService().getUserInfo(map)
                            .compose(RxUtils.handleResultNoThread())
                            .map(userInfo -> {
                                String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfo);
                                User.getInstance().setUserInfo(userInfoString);
                                return userInfoString;
                            });
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 修改密码 检查手机号
     * @param phone
     * @return
     */
    public Observable<String> checkExistPhone(String phone){
        return config.getRetrofitService().checkExistPhone(phone)
                .compose(RxUtils.handleResult());
    }

    /**
     * 修改密码
     * @param phone
     * @return
     */
    public Observable<String> changePwd(String phone, String pwd){
        return config.getRetrofitService().changePwd(phone, pwd)
                .compose(RxUtils.handleResult());
    }

    /**
     * 修改密码
     * @param token
     * @return
     */
    public Observable<String> logout(String token){
        return config.getRetrofitService().logout(token)
                .compose(RxUtils.handleResult());
    }

    /**
     *  购买VIP
     */
    public Observable<UserInfo.PersonBean> addVipDuration(long time, long glod){
        Map<String, Long> map = new HashMap<>();
        map.put("vipDateline", time);
        map.put("gold", glod);
        return config.getRetrofitService().addVipDuration(User.getInstance().getUserInfo().getPerson().getToken(), map)
                .compose(RxUtils.handleResult());
    }

    /**
     *  更改个人地址
     */
    public Observable<String> changeAddress(String address){
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("token", User.getInstance().getUserInfo().getPerson().getToken());
        return config.getRetrofitService().changeAddress(map)
                .compose(RxUtils.handleResult());
    }


    /**
     * 更改个人头像
     */
    public Observable<String> changeHeadIcon(String path){
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", path.substring(path.lastIndexOf("/")+1), body);

        return config.getRetrofitService().changeHeadIcon(part, User.getInstance().getUserInfo().getPerson().getToken())
                .map( responseBody -> {
                    try {
                        String s = responseBody.string();
                        UpdateFileBean bean = JsonUtils.getInstance().JsonToUpdateFile(s);
                        if (TextUtils.equals(bean.getMsg(), "200")){
                            return bean.getResult();
                        }else if(TextUtils.equals(bean.getResult(), "请登录")){
                            return bean.getResult();
                        }else if(TextUtils.equals(bean.getMsg(), "501")){
                            return "上传文件太大";
                        }else{
                            return "上传失败";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "上传失败";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    /**
     *  更改性别
     */
    public Observable<String> changeSex(int sex){
        return config.getRetrofitService().changeSex(sex, User.getInstance().getUserInfo().getPerson().getToken())
                .compose(RxUtils.handleResult());
    }

    /**
     *  检查更新
     */
    public Observable<UpdateVersionBean.DataBean> updateVersion(){
        return config.getRetrofitService().updateVersion("caiji")
                .compose(RxUtils.handleResult());
    }

    /**
     *  检查更新
     */
    public Observable<String> submitSuggest(String phone, String content){
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getUserInfo().getPerson().getToken());
        map.put("mobile", phone);
        map.put("content", content);
        return config.getRetrofitService().submitSuggest(map)
                .compose(RxUtils.handleResult());
    }

}
