package com.demo.cworker.Model;

import com.demo.cworker.Common.Constants;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.RxUtils;
import com.demo.cworker.Utils.ShareUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by
 */
@Singleton
public class LoginModel extends BaseModel {
    @Inject
    public LoginModel() {}

    public Observable<String> checkPhone(String phone){
        return config.getRetrofitService().checkPhone(phone)
                .compose(RxUtils.handleResult());
    }

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
}
