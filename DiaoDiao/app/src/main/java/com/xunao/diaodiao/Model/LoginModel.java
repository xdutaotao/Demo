package com.xunao.diaodiao.Model;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Bean.BaseRequestBean;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Bean.GetCodeBean;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Bean.LoginBean;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.RegisterBean;
import com.xunao.diaodiao.Bean.RegisterRespBean;
import com.xunao.diaodiao.Bean.SelectBean;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.JsonUtils;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.jpush.im.android.api.model.*;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

import static com.xunao.diaodiao.Common.Constants.CACHE_DIR;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

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
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("getVerify");
        sb.append(time+"").append(phone)
                .append("security");

        GetCodeBean bean = new GetCodeBean();
        bean.setMobile(phone);
        bean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().checkPhone(setBody("getVerify", time, bean))
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
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("login");
        sb.append(time+"").append(mobile).append(pwd)
                .append("security");

        LoginBean loginBean = new LoginBean();
        loginBean.setMobile(mobile);
        loginBean.setPassword(pwd);
        loginBean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().login(setBody("login", time, loginBean))
                .compose(RxUtils.handleResultNoThread())
                .map(loginResBean -> {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setType(loginResBean.getType());
                    userInfo.setMobile(loginResBean.getMobile());
                    userInfo.setUserid(loginResBean.getUserid());
                    userInfo.setIs_frozen(loginResBean.getIs_frozen());
                    User.getInstance().setUserInfo(JsonUtils.getInstance().UserInfoToJson(userInfo));
                    User.getInstance().setUserId(loginResBean.getUserid()+"");
                    return loginResBean;
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 注册
     * @param phone
     * @return
     */
    public Observable<String> checkExistPhone(String phone, String pwd, String code, int type){
        String actionName = "";
        switch (type){
            case 0:
                actionName = "register";
                break;

            case 1:
                actionName = "forgetpwd";
                break;

            case 2:
                actionName = "forgetpwd";
                break;
        }


        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(actionName);
        sb.append(time+"").append(code).append(phone).append(pwd)
                .append("security");

        RegisterBean registerBean = new RegisterBean();
        registerBean.setMobile(phone);
        registerBean.setPassword(pwd);
        registerBean.setCode(code);
        registerBean.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().checkExistPhone(setBody(actionName, time, registerBean))
                .compose(RxUtils.handleResultNoThread())
                .map(registerRespBean -> {
                    String userid = registerRespBean.getUserid();
                    User.getInstance().setUserId(userid);
                    return userid;
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }



    /**
     * 注册
     * @param phone
     * @return
     */
    public Observable<String> forgetPwd(String phone, String pwd, String code, int type){
        String actionName = "";
        switch (type){
            case 0:
                actionName = "register";
                break;

            case 1:
                actionName = "forgetpwd";
                break;

            case 2:
                actionName = "forgetpwd";
                break;
        }


        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(actionName);
        sb.append(time+"").append(code).append(phone).append(pwd)
                .append("security");

        RegisterBean registerBean = new RegisterBean();
        registerBean.setMobile(phone);
        registerBean.setPassword(pwd);
        registerBean.setCode(code);
        registerBean.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().forgetPwd(setBody(actionName, time, registerBean))
                .compose(RxUtils.handleResult());
    }






    /**
     * 选择角色
     * @param
     * @return
     */
    public Observable<String> select(int type){
        String useid = User.getInstance().getUserId();
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("chooseRole");
        sb.append(time+"").append(type).append(useid)
                .append("security");

        SelectBean selectBean = new SelectBean();
        selectBean.setType(type);
        selectBean.setUserid(Integer.valueOf(useid));
        selectBean.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().select(setBody("chooseRole", time, selectBean))
                .compose(RxUtils.handleResultNoThread())
                .map(selectRespBean -> {
                    return selectRespBean.getType()+"";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    /**
     * 完善信息
     * @param
     * @return
     */
    public Observable<LoginResBean> fillInfor(FillCompanyReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("completeCompany");
        sb.append(time+"").append(req.getAddress()).append(req.getCity())
                .append(req.getContact()).append(req.getContact_mobile())
                .append(req.getDistrict()).append(req.getName())
                .append(req.getProvince()).append(req.getTel())
                .append(req.getUserid())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().fillInfor(setBody("completeCompany", time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 完善信息 技工
     * @param
     * @return
     */
    public Observable<LoginResBean> fillSkillInfor(FillSkillReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("completeTechnician");
        sb.append(time+"").append(req.getAddress()).append(req.getCity())
                .append(req.getDistrict()).append(req.getEvaluate())
                .append(req.getExperience()).append(req.getMobile())
                .append(req.getName()).append(req.getProject_type())
                .append(req.getProvince()).append(req.getUserid())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().fillInfor(setBody("completeTechnician", time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 完善信息 技工
     * @param
     * @return
     */
    public Observable<LoginResBean> fillNormalInfor(FillNormalReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("completeFamily");
        sb.append(time+"").append(req.getAddress()).append(req.getCity())
                .append(req.getDistrict()).append(req.getMobile())
                .append(req.getName())
                .append(req.getProvince()).append(req.getUserid())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().fillInfor(setBody("completeFamily", time, req))
                .compose(RxUtils.handleResult());
    }




    public Observable<GetMoneyRes> getMoney(){
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder("balanceInfo");
        sb.append(time+"").append(type+"")
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getMoney(setBody("balanceInfo", time, req))
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





}
