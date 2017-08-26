package com.xunao.diaodiao.Model;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Bean.BaseRequestBean;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.JsonUtils;
import com.xunao.diaodiao.Utils.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

import static com.xunao.diaodiao.Common.Constants.CACHE_DIR;

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
     * @return
     */
    public Observable<LoginResBean> login(String mobile, String pwd){
        StringBuilder sb = new StringBuilder("login");
        sb.append(mobile).append(pwd).append(System.currentTimeMillis()+"")
                .append("security");

        String params = String.format("{\"mobile\":\"%s\"," +
                "\"password\":\"%s\",\"verify\":\"%s\"}", mobile , pwd, sb.toString());

        return config.getRetrofitService().login(setBody("login", params))
                .compose(RxUtils.handleResult());
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
    public Observable<String> addVipDuration(long time, int gold){
        return config.getRetrofitService().addVipDuration(User.getInstance().getUserId(), time, gold)
                .compose(RxUtils.handleResultNoThread())
                .flatMap(personBean -> {
                    return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                            .compose(RxUtils.handleResultNoThread());
                })
                .map(userInfo -> {
                    String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfo);
                    User.getInstance().setUserInfo(userInfoString);
                    return "操作成功";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     *  更改个人地址
     */
    public Observable<String> changeAddress(String address){
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("token", User.getInstance().getUserId());
        return config.getRetrofitService().changeAddress(map)
                .flatMap(baseResponseBean -> {
                    if (TextUtils.equals(baseResponseBean.getMsg(), "200")){
                        return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                                .compose(RxUtils.handleResultNoThread());
                    }else{
                        return Observable.error(new RxUtils.ServerException("操作失败")) ;
                    }
                })
                .map(userInfo -> {
                    String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfo);
                    User.getInstance().setUserInfo(userInfoString);
                    return "操作成功";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    /**
     * 更改个人头像
     */
    public Observable<String> changeHeadIcon(String path){
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        //MultipartBody.Part part = MultipartBody.Part.createFormData("photo", path.substring(path.lastIndexOf("/")+1), body);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("photo\"; filename=\""+file.getName(), body);
        map.put("token", RequestBody.create(MediaType.parse("application/json"), User.getInstance().getUserId()));

        return config.getRetrofitService().changeHeadIcon(map)
                .flatMap( responseBody -> {
                    try {
                        String s = responseBody.string();
                        BaseResponseBean bean = JsonUtils.getInstance().JsonToUpdateFile(s);
                        if (TextUtils.equals(bean.getMsg(), "200")){
                            return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                                    .compose(RxUtils.handleResultNoThread());
                        }else if(TextUtils.equals(bean.getResult(), "请登录!")){
                            return Observable.error(new RxUtils.ServerException("请登录")) ;
                        }else if(TextUtils.equals(bean.getMsg(), "501")){
                            return Observable.error(new RxUtils.ServerException("上传文件太大")) ;
                        }else{
                            return Observable.error(new RxUtils.ServerException("操作失败")) ;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Observable.error(new RxUtils.ServerException("操作失败")) ;
                })
                .map(userInfo -> {
                    String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfo);
                    User.getInstance().setUserInfo(userInfoString);
                    return "操作成功";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    /**
     *  更改性别
     */
    public Observable<String> changeSex(int sex){
        return config.getRetrofitService().changeSex(sex, User.getInstance().getUserId())
                .compose(RxUtils.handleResult());
    }

    /**
     *  检查更新
     */
    public Observable<List<UpdateVersionBean.DataBean>> updateVersion(){
        return config.getRetrofitService().updateVersion("caiji")
                .compose(RxUtils.handleResult());
    }

    /**
     *  检查更新
     */
    public Observable<String> submitSuggest(String phone, String content){
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getUserId());
        map.put("mobile", phone);
        map.put("content", content);
        return config.getRetrofitService().submitSuggest(map)
                .compose(RxUtils.handleResult());
    }

    /**
     * 清除缓存
     * @return
     */
    public Observable<String> clearCache(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Glide.get(App.getContext()).clearDiskCache();
                FileUtils.deleteDirectory(CACHE_DIR);
                subscriber.onNext("清除成功");
                subscriber.onCompleted();
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 签到
     * @return
     */
    public Observable<String> signToday(int gold, int experience){
        Map<String, Integer> map = new HashMap<>();
        map.put("gold", gold);
        map.put("experience", experience);
        return config.getRetrofitService().signToday(User.getInstance().getUserId(), map)
                .compose(RxUtils.handleResultNoThread())
                .flatMap(personBean -> {
                    return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                            .compose(RxUtils.handleResultNoThread());
                })
                .map(userInfoBaseBean -> {
                    String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfoBaseBean);
                    User.getInstance().setUserInfo(userInfoString);
                    return "操作成功";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }





}
