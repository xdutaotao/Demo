package com.seafire.cworker.Model;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.bumptech.glide.Glide;
import com.seafire.cworker.App;
import com.seafire.cworker.Bean.BaseResponseBean;
import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.UpdateVersionBean;
import com.seafire.cworker.Utils.FileUtils;
import com.seafire.cworker.Utils.JsonUtils;
import com.seafire.cworker.Utils.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.*;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.chatting.utils.SharePreferenceManager;
import io.jchat.android.database.FriendEntry;
import io.jchat.android.database.FriendRecommendEntry;
import io.jchat.android.database.UserEntry;
import io.jchat.android.entity.FriendInvitation;
import io.jchat.android.tools.HanziToPinyin;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

import static com.seafire.cworker.Common.Constants.CACHE_DIR;

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

                    return config.getRetrofitService().getUserInfo(s)
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


    public Observable<List<FreindBean>> getFriends(){
        return config.getRetrofitService().getFriends(User.getInstance().getUserId())
                .compose(RxUtils.handleResultNoThread())
                .map(freindBeen -> {
                    if (freindBeen.size() > 0){
                        ActiveAndroid.beginTransaction();
                        try {
                            UserEntry user = UserEntry.getUser(JMessageClient.getMyInfo().getUserName(),
                                    JMessageClient.getMyInfo().getAppKey());
                            for (FreindBean bean : freindBeen) {
                                for (FreindBean.UsersBean usersBean : bean.getUsers()){
                                    freindBeanTo(usersBean, user);
                                    //freindToRecommand(usersBean, user);
                                }

                            }
                        ActiveAndroid.setTransactionSuccessful();
                        }finally {
                            ActiveAndroid.endTransaction();
                        }
                    }

                    return freindBeen;
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    //添加为朋友
    private void freindBeanTo(FreindBean.UsersBean bean, UserEntry user){

        String displayName = bean.getName();
        if (TextUtils.isEmpty(displayName)) {
            return;
        }
        String letter;
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance()
                .get(displayName);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (token.type == HanziToPinyin.Token.PINYIN) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        String sortString = sb.toString().substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase();
        } else {
            letter = "#";
        }
        //避免重复请求时导致数据重复
        FriendEntry friend = FriendEntry.getFriend(user,
                bean.getMobile(), bean.getAppid());
        if (null == friend) {
            if (TextUtils.isEmpty(bean.getFace())) {
                friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
                        null, displayName, letter, user);
            } else {
                if (bean.getFace() != null) {
                    friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
                            bean.getFace(), displayName, letter, user);
                }else {
                    friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
                            "", displayName, letter, user);
                }
            }
            friend.save();
        }







        //add friend to contact
//        JMessageClient.getUserInfo(bean.getMobile(), bean.getAppid(), new GetUserInfoCallback() {
//            @Override
//            public void gotResult(int status, String desc, final cn.jpush.im.android.api.model.UserInfo userInfo) {
//                if (status == 0) {
//                    String displayName = bean.getName();
//                    if (TextUtils.isEmpty(displayName)) {
//                        return;
//                    }
//                    String letter;
//                    ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance()
//                            .get(displayName);
//                    StringBuilder sb = new StringBuilder();
//                    if (tokens != null && tokens.size() > 0) {
//                        for (HanziToPinyin.Token token : tokens) {
//                            if (token.type == HanziToPinyin.Token.PINYIN) {
//                                sb.append(token.target);
//                            } else {
//                                sb.append(token.source);
//                            }
//                        }
//                    }
//                    String sortString = sb.toString().substring(0, 1).toUpperCase();
//                    if (sortString.matches("[A-Z]")) {
//                        letter = sortString.toUpperCase();
//                    } else {
//                        letter = "#";
//                    }
//                    //避免重复请求时导致数据重复
//                    FriendEntry friend = FriendEntry.getFriend(user,
//                            bean.getMobile(), bean.getAppid());
//                    if (null == friend) {
//                        if (TextUtils.isEmpty(bean.getFace())) {
//                            friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
//                                    null, displayName, letter, user);
//                        } else {
//                            if (bean.getFace() != null) {
//                                friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
//                                        bean.getFace(), displayName, letter, user);
//                            }else {
//                                friend = new FriendEntry(bean.getMobile(), bean.getAppid(),
//                                        "", displayName, letter, user);
//                            }
//                        }
//                        friend.save();
//                    }
//                }
//            }
//        });
//        FriendRecommendEntry entry = FriendRecommendEntry.getEntry(user, bean.getMobile(), bean.getAppid());
//        entry.state = FriendInvitation.ACCEPTED.getValue();
//        entry.save();

    }

    //添加组成员
    private void freindToRecommand(FreindBean.UsersBean userInfo, UserEntry user){
        FriendRecommendEntry entry = FriendRecommendEntry.getEntry(user, userInfo.getName(), userInfo.getAppid());
        if (null == entry) {
            if (null != userInfo.getFace()) {
                String path = userInfo.getFace();
                entry = new FriendRecommendEntry(userInfo.getName(), userInfo.getAppid(), path,
                        userInfo.getName(), "", FriendInvitation.INVITED.getValue(), user);
            } else {
                entry = new FriendRecommendEntry(userInfo.getName(), userInfo.getAppid(), null,
                        userInfo.getName(), "", FriendInvitation.INVITED.getValue(), user);
            }
        } else {
            entry.state = FriendInvitation.INVITED.getValue();
        }
        entry.save();
        int showNum = SharePreferenceManager.getCachedNewFriendNum() + 1;
        SharePreferenceManager.setCachedNewFriendNum(showNum);
    }

}
