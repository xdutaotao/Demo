package com.demo.cworker.Model;

import com.demo.cworker.Bean.LoginBean;
import com.demo.cworker.Bean.RegisterBean;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.RxUtils;
import com.demo.cworker.Utils.Utils;

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
                    RegisterBean bean = new RegisterBean();
                    bean.setName(name);
                    bean.setPassword(pwd);
                    bean.setMobile(phone);
                    bean.setEmail(email);
                    String param = JsonUtils.getInstance().RegisterBeanToJson(bean);
                    return config.getRetrofitService().register(getBody(param))
                            .compose(RxUtils.handleResultNoThread());
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    public Observable<String> login(String phone, String pwd, String cliendID){
//        String s = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"clientId\":\"%s\"}",phone, pwd, cliendID);
        LoginBean bean = new LoginBean();
        bean.setUsername(phone);
        bean.setPassword(pwd);
        bean.setClientId(cliendID);
        String s = JsonUtils.getInstance().LoginBeanToJson(bean);
        return config.getRetrofitService().login(getBody(s))
                .compose(RxUtils.handleResult());
    }
}
