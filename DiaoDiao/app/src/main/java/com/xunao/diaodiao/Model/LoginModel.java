package com.xunao.diaodiao.Model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.AddressBeanReq;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Bean.BalancePayRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BaseRequestBean;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.CitiesBean;
import com.xunao.diaodiao.Bean.CityBean;
import com.xunao.diaodiao.Bean.CollectRes;
import com.xunao.diaodiao.Bean.DocReq;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.EditBankReq;
import com.xunao.diaodiao.Bean.EvaluateReq;
import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Bean.GetCashRes;
import com.xunao.diaodiao.Bean.GetCodeBean;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.HasRateRes;
import com.xunao.diaodiao.Bean.HeadIconReq;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.HomeSearchRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Bean.LoginBaseReq;
import com.xunao.diaodiao.Bean.LoginBean;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.MaintenanceTypeRes;
import com.xunao.diaodiao.Bean.MessageListReq;
import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.MyAcceptOddSubmitReq;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Bean.MyAppealDetailRes;
import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Bean.OddFeeRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Bean.PayFeeReq;
import com.xunao.diaodiao.Bean.PayRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.ProjectTypeRes;
import com.xunao.diaodiao.Bean.RateDetailReq;
import com.xunao.diaodiao.Bean.RateDetailRes;
import com.xunao.diaodiao.Bean.RateReq;
import com.xunao.diaodiao.Bean.RegisterBean;
import com.xunao.diaodiao.Bean.RegisterRespBean;
import com.xunao.diaodiao.Bean.ReleaseHelpReq;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Bean.SelectBean;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Bean.SkillProjRecieveDetailRes;
import com.xunao.diaodiao.Bean.SkillRecieveProjDetailRes;
import com.xunao.diaodiao.Bean.SkillRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Bean.WeiBaoDetailRes;
import com.xunao.diaodiao.Bean.WeiBaoProgRes;
import com.xunao.diaodiao.Bean.WeiXinReq;
import com.xunao.diaodiao.Bean.WeiXinRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillRecievePresenter;
import com.xunao.diaodiao.Present.ProjectRes;
import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.JsonUtils;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.Utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.jpush.im.android.api.model.*;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

import static android.R.attr.id;
import static com.xunao.diaodiao.Activity.WebViewActivity.HOME_HZ_DETAIL;
import static com.xunao.diaodiao.Common.Constants.CACHE_DIR;
import static com.xunao.diaodiao.Common.Constants.COMPANY_PROJECT_NO_PASS;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.HOME_HZ;
import static com.xunao.diaodiao.Common.Constants.JIA_TYPE;
import static com.xunao.diaodiao.Common.Constants.NO_PASS;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_NO_PASS;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.YI_TYPE;
import static com.xunao.diaodiao.Common.Constants.address;
import static com.xunao.diaodiao.Common.Constants.city;
import static com.xunao.diaodiao.Common.Constants.selectCity;

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
    public Observable<Object> checkPhone(String phone){
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
     * 绑定微信 验证码
     * @param phone
     * @return
     */
    public Observable<Object> wxPhone(String phone){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("bindingVerify");
        sb.append(time+"").append(phone)
                .append("security");

        GetCodeBean bean = new GetCodeBean();
        bean.setMobile(phone);
        bean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().checkPhone(setBody("bindingVerify", time, bean))
                .compose(RxUtils.handleResult());
    }

    /**
     * 绑定微信
     * @param
     * @return
     */
    public Observable<UserInfo> wxPhone(WeiXinReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("bindingWx");
        sb.append(time+"").append(req.getCode()).append(req.getDevice_token())
                .append(req.getDevice_type())
                .append(req.getMobile())
                .append(req.getOpenID())
                .append(req.getPassword())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().bindingWx(setBody("bindingWx", time, req))
                .compose(RxUtils.handleResult())
                .map(weiXinRes -> {
                    User.getInstance().setUserId(weiXinRes.getUserid()+"");
                    User.getInstance().setUserInfo(JsonUtils.getInstance().UserInfoToJson(weiXinRes));
                    return weiXinRes;
                });
    }

    /**
     * 绑定微信
     * @param
     * @return
     */
    public Observable<UserInfo> WxLogin(GetMoneyReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("WxLogin");
        sb.append(time+"").append(req.getDevice_token()).append(req.getDevice_type())
                .append(req.getOpenID())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().bindingWx(setBody("WxLogin", time, req))
                .compose(RxUtils.handleResult())
                .map(weiXinRes -> {
                    User.getInstance().setUserId(weiXinRes.getUserid()+"");
                    User.getInstance().setUserInfo(JsonUtils.getInstance().UserInfoToJson(weiXinRes));
                    ShareUtils.putValue(TYPE_KEY, weiXinRes.getType());
                    return weiXinRes;
                });
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
    public Observable<LoginResBean> login(String mobile, String pwd, String deviceToken, int deviceType){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("login");
        sb.append(time+"").append(mobile).append(pwd)
                .append("security");

        LoginBean loginBean = new LoginBean();
        loginBean.setMobile(mobile);
        loginBean.setPassword(pwd);
        loginBean.setDevice_type(2);
        loginBean.setDevice_token(deviceToken);
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
                .append(2)
                .append("security");

        RegisterBean registerBean = new RegisterBean();
        registerBean.setPhone_type(2);
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
    public Observable<Object> forgetPwd(String phone, String pwd, String code, int type){
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
        String useid = User.getInstance().getUserId();
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder("completeInfo");
        sb.append(time+"").append(req.getAddress()).append(req.getCard_back())
                .append(req.getCard_front()).append(req.getCity())
                .append(req.getContact())
                .append(req.getContact_card()).append(req.getContact_mobile())
                .append(req.getDistrict()).append(req.getEstablish_time()).append(req.getName())
                .append(req.getProvince()).append(req.getTel()).append(req.getType())
                .append(useid).append(req.getYears())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));
        req.setUserid(Integer.valueOf(useid));
        req.setType(type);


        return config.getRetrofitService().fillInfor(setBody("completeInfo", time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 完善信息 技工
     * @param
     * @return
     */
    public Observable<LoginResBean> fillSkillInfor(FillSkillReq req){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("completeInfo");
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        sb.append(time+"").append(req.getAddress()).append(req.getCard())
                .append(req.getCard_back()).append(req.getCard_front()).append(req.getCertificate())
                .append(req.getCity())
                .append(req.getDistrict()).append(req.getEvaluate())
                .append(req.getExperience()).append(req.getMobile())
                .append(req.getName()).append(req.getProject_type())
                .append(req.getProvince()).append(req.getTeam_number()).append(type).append(req.getUserid())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));
        req.setType(type);


        return config.getRetrofitService().fillInfor(setBody("completeInfo", time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 完善信息 技工
     * @param
     * @return
     */
    public Observable<LoginResBean> fillNormalInfor(FillNormalReq req){
        String useid = User.getInstance().getUserId();
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("completeInfo");
        sb.append(time+"").append(req.getAddress()).append(req.getCity())
                .append(req.getDistrict()).append(req.getMobile())
                .append(req.getName())
                .append(req.getProvince()).append(type)
                .append(useid)
                .append("security");

        req.setUserid(Integer.valueOf(useid));
        req.setVerify(Utils.getMD5(sb.toString()));
        req.setType(type);

        return config.getRetrofitService().fillInfor(setBody("completeInfo", time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 余额
     * @return
     */
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
     * 未评价列表
     * @return
     */
    public Observable<MyRateRes> getRating(int page){
        String rateKey = "notEvaluated";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type+"")
                .append(User.getInstance().getUserId())
                .append("security");

        RateReq req = new RateReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getRate(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 已评价列表
     * @return
     */
    public Observable<HasRateRes> getHasRating(int page){
        String rateKey = "hasEvaluated";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type+"")
                .append(User.getInstance().getUserId())
                .append("security");

        RateReq req = new RateReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getHasRate(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 我的投诉列表
     * @return
     */
    public Observable<MyComplaintRes> getMyComplaint(int page, int who){
        String rateKey = "myAppealList";
        if (who == 1){
            rateKey = "otherAppealList";
        }

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type+"")
                .append(User.getInstance().getUserId())
                .append("security");

        RateReq req = new RateReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getMyComplaint(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 申诉详情
     * @param
     * @return
     */
    public Observable<MyAppealDetailRes> myAppealDetail(int id){
        String rateKey = "myAppealDetail";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type+"")
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setAppeal_id(id);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().myAppealDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 评价列表
     * @return
     */
    public Observable<RateDetailRes> getRatingDetail(int detailID){
        String rateKey = "evaluateDetail";

        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(detailID)
                .append("security");

        RateDetailReq req = new RateDetailReq();
        req.setEvaluate_id(detailID);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getRateDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 评价列表
     * @return
     */
    public Observable<MyBean> getInfo(){
        String rateKey = "personalCenter";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getUserInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 收藏列表
     * @return
     */
    public Observable<MyFavoriteRes> getCollectList(int page){
        String rateKey = "collectList";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getCollectList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *
     * @param
     * @return
     */
    public Observable<Object> cancelCollect(int id){
        String rateKey = "cancelCollect";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setCollect_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().cancelCollect(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  取消已经发布的项目
     * @param
     * @return
     */
    public Observable<Object> myProjectCancel(int id, int who){
        String rateKey = "myProjectCancel";
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            rateKey = "allMaintenanceCancel";
        }else if(who == 1){
            rateKey = "myPublishOddCancel";
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            rateKey = "mySupervisorCancel";
        }else if(who == SKILL_RELEASE_WEIBAO_WAIT){
            rateKey = "allMaintenanceCancel";
        }

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            req.setMaintenance_id(id);
        }else if(who == 1){
            req.setOdd_id(id);
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT) {
            req.setSupervisor_id(id);
        }else if(who == SKILL_RELEASE_WEIBAO_WAIT) {
            req.setMaintenance_id(id);
        }else{
            req.setProject_id(id);
        }

        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().myProjectCancel(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 银行卡列表
     * @return
     */
    public Observable<BankListRes> getBankList(){
        String rateKey = "bankList";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getBankList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<BankListRes> getBankCardList(){
        String rateKey = "bindingCardBankList";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getBankList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 提现记录
     * @return
     */
    public Observable<CashRecordRes> cashRecord(int page){
        String rateKey = "cashRecord";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().cashRecord(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 银行卡列表
     * @return
     */
    public Observable<Object> applyCash(GetCashRes req){
        String rateKey = "applyCash";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getCard()).append(req.getCash())
                .append(type).append(User.getInstance().getUserId())
                .append("security");


        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().applyCash(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 银行卡列表
     * @return
     */
    public Observable<AddBankRes> bindingCardGetVerify(BindBankReq req){
        String rateKey = "bindingCard";
        boolean isCode = TextUtils.isEmpty(req.getTrade_no());
        if (isCode){
            rateKey = "bindingCardGetVerify";
        }

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getCard()).append(req.getCard_type());
        if (!isCode){
            sb.append(req.getCode());
        }
        sb.append(req.getIdentity_card())
                .append(req.getMobile()).append(req.getBank_name());
        if(!isCode){
            sb.append(req.getTrade_no());
        }
        sb.append(req.getType())
                .append(User.getInstance().getUserId())
                .append("security");


        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().bindingCardGetVerify(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 银行卡列表
     * @return
     */
    public Observable<Object> bindingCard(BindBankReq req){
        String rateKey = "bindingCard";
        boolean isCode = TextUtils.isEmpty(req.getTrade_no());
        if (isCode){
            rateKey = "bindingCardGetVerify";
        }

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"");
        if (!isCode){
            sb.append(req.getBank_branch());
        }
        sb.append(req.getBank_name()).append(req.getCard()).append(req.getCard_type());
        if (!isCode){
            sb.append(req.getCity()).append(req.getCode()).append(req.getDistrict());
        }
        sb.append(req.getIdentity_card())
                .append(req.getMobile());
        if(!isCode){
            sb.append(req.getProvince()).append(req.getTrade_no());
        }
        sb.append(req.getType())
                .append(User.getInstance().getUserId())
                .append("security");


        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().bindingCard(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 银行卡列表
     * @return
     */
    public Observable<Object> updateBankcard(EditBankReq req){
        String rateKey = "updateBankcard";


        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getBank_branch())
                .append(req.getCard())
                .append(req.getCity())
                .append(req.getDistrict())
                .append(req.getProvince())
                .append(req.getType())
                .append(User.getInstance().getUserId())
                .append("security");


        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().bindingCard(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 更改个人头像
     */
    public Observable<HeadIconRes> changeHeadIcon(String path){
        String rateKey = "headInfo";

        long time = System.currentTimeMillis()/1000;
        String base = Utils.Bitmap2StrByBase64(path);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(base)
                .append(User.getInstance().getUserId())
                .append("security");

        HeadIconReq req = new HeadIconReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setHead_img(base);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().changeHeadIcon(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());

    }

    public Observable<PersonalRes> getPersonalInfo(){
        String rateKey = "personalInfo";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getPersonalInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());

    }

    /**
     *  拿到资料库文件
     */
    public Observable<List<DocRes>> getDataBase(){
        String rateKey = "databaseType";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"")
                .append("security");

        DocReq req = new DocReq();
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getDataBase(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  拿到资料库文件
     */
    public Observable<List<DocRes>> database(int id, int page){
        String rateKey = "database";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(page).append(10)
                .append("security");

        DocReq req = new DocReq();
        req.setId(id);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getDataBase(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  项目类型
     */
    public Observable<TypeInfoRes> getTypeInfo(){
        String rateKey = "typeInfo";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(userid)
                .append("security");

        LoginBaseReq req = new LoginBaseReq();
        req.setUserid(userid);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getTypeInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  项目类型
     */
    public Observable<MessageListRes> messageList(int page){
        String rateKey = "messageList";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }

        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type)
                .append(userid)
                .append("security");

        MessageListReq req = new MessageListReq();
        req.setPage(page);
        req.setPageSize(10);
        req.setType(type);
        req.setUserid(userid);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().messageList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  阅读消息
     */
    public Observable<Object> readMessage(int msgID){
        String rateKey = "readMessage";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }

        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(msgID)
                .append("security");

        MessageListReq req = new MessageListReq();
        req.setMessage_id(msgID);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().readMessage(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  阅读消息
     */
    public Observable<Object> cancelMessage(int msgID){
        String rateKey = "cancelMessage";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }

        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(msgID)
                .append("security");

        MessageListReq req = new MessageListReq();
        req.setMessage_id(msgID);
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().readMessage(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  拿到资料库文件
     */
    public Observable<ProjectRes> getMyWork(){
        String rateKey = "myWork";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }


        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getMyWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  找项目
     */
    public Observable<FindProjectRes> getFindProjectList(FindProjReq req, int type){
        String rateKey = "projectList";
        switch (type){
            case 0:
                break;
            case 1:
                rateKey = "oddList";
                break;

            case 2:
                rateKey = "maintenanceList";
                break;

            case 4:
                rateKey = "supervisorList";
                break;

            case 3:
                rateKey = "mutualList";
                break;
        }
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(city).append(req.getKeyword()).append(req.getLat())
                .append(req.getLng()).append(req.getNearby())
                .append(req.getPage())
                .append(req.getPageSize()).append(req.getTime_type())
                .append(req.getType())
                .append("security");

        req.setCity(city);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getProjectList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  合作商家
     */
    public Observable<FindProjectRes> businesses(FindProjReq req){
        String rateKey = "businesses";

        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getCity())
                .append(req.getDistrict()).append(req.getKeyword())
                .append(req.getLat()).append(req.getLng())
                .append(req.getNearby())
                .append(req.getPage())
                .append(req.getPageSize()).append(req.getSort())
                .append("security");


        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getProjectList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  项目详情
     */
    public Observable<String> projectDetail(int id, int types){
        String rateKey = "projectDetail";
        switch (types){
            case 0:
                break;
            case 1:
                rateKey = "oddList";
                break;

            case 2:
                rateKey = "maintenanceList";
                break;
        }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setType(type);
        req.setUserid(userid);
        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().projectDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  项目详情
     */
    public Observable<FindProjDetailRes> getFindProjDetail(int id, int who){
        String rateKey = "myProjectDetail";
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            rateKey = "allMaintenanceDetail";
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            rateKey = "mySupervisorDetail";
        }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
            rateKey = "allMutualDetail";
        }else if(who == HOME_HZ){
            rateKey = "mutualDetail";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setProject_id(id);
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            req.setMaintenance_id(id);
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            req.setSupervisor_id(id);
        }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
            req.setMutual_id(id);
        }
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFindProjDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  维保详情
     */
    public Observable<WeiBaoDetailRes> getFindWBDetail(int id, int who){
        String rateKey = "myProjectDetail";
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            rateKey = "allMaintenanceDetail";
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            rateKey = "mySupervisorDetail";
        }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
            rateKey = "allMutualDetail";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setProject_id(id);
        if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            req.setMaintenance_id(id);
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            req.setSupervisor_id(id);
        }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
            req.setMutual_id(id);
        }
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFindWBDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //申请项目
    public Observable<Object> postProject(int id, int types){
        String rateKey = "applyProject";
        if (types == 0){

        }else if(types == 1){
            rateKey = "applyOdd";
        }else if(types == 2){
            rateKey = "applyMaintenance";
        }else if(types == 4){
            rateKey = "applySupervisor";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().postProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //收藏
    public Observable<CollectRes> collectWork(int id, int types){
        String rateKey = "collectWork";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(types)
                .append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setProject_id(id);
        req.setUser_type(type);
        req.setProject_type(types);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().collectWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 零工详情
     * @param id
     * @return
     */
    public Observable<FindLingGongRes> getFindLingGongDetail(int id){
        String rateKey = "myPublishOddDetail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setOdd_id(id);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFindLingGongDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 维保
     * @param id
     * @return
     */
    public Observable<FindLingGongRes> getFindWBDetail(int id){
        String rateKey = "allMaintenanceDetail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setMaintenance_id(id);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFindLingGongDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<JoinDetailRes> getCompanyInfo(int id, int page){
        String rateKey = "businessInfo";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(page).append(10).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setCompany_id(id);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getCompanyInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    public Observable<GetOddInfoRes> getOddInfo(int id, int page){
        String rateKey = "oddInfo";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(page).append(10).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setPageSize(10);
        req.setPage(page);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getOddInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //我发布的  详情
    public Observable<SkillProjDetailRes> mySkillProjDetail(int id){
        String rateKey = "myPublishOddDetail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().mySkillProjDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //我接的 零工 详情
    public Observable<SkillProjRecieveDetailRes> mySkillProjRecieveDetail(int id){
        String rateKey = "myAcceptOddDetail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().mySkillProjRecieveDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 提交意见
     */
    public Observable<Object> submitSuggest(String content){
        String rateKey = "feedBack";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(content).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setContent(content);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().submitSuggest(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 评价
     */
    public Observable<Object> toEvaluate(EvaluateReq req){
        String rateKey = "toEvaluate";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContent()).append(req.getImages())
                .append(req.getPoint()).append(req.getProject_id()).append(req.getProject_type())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().submitSuggest(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 互助关闭
     */
    public Observable<Object> allMutualCancel(int id){
        String rateKey = "allMutualCancel";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id)
                .append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setMutual_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().submitSuggest(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 项目进行中
     */
    public Observable<OrderCompRes> myProjectWait(int page, int who){
        String rateKey = "myProjectWait";

        if (who == COMPANY_RELEASE_PROJECT_DOING){
            rateKey = "myProjectDoing";
        }else if (who == COMPANY_RELEASE_PROJECT_DONE){
            rateKey = "myProjectFinish";
        }else if(who == COMPANY_RELEASE_JIANLI_DOING){
            rateKey = "mySupervisorDoing";
        }else if(who == COMPANY_RELEASE_JIANLI_DONE){
            rateKey = "mySupervisorFinish";
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            rateKey = "mySupervisorWait";
        }else if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            rateKey = "allMaintenanceWait";
        }else if(who == COMPANY_RELEASE_WEIBAO_DOING){
            rateKey = "allMaintenanceDoing";
        }else if(who == COMPANY_RELEASE_WEIBAO_DONE){
            rateKey = "allMaintenanceFinish";
        }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
            rateKey = "allMutualWait";
        }else if(who == COMPANY_RELEASE_HUZHU_DONE){
            rateKey = "allMutualClose";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myProjectWait(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 待确认
     */
    public Observable<OrderSkillRes> mySkillWait(int page, int who){
        String rateKey = "myPublishOddWait";
        if(who == SKILL_RELEASE_WEIBAO){
            rateKey = "allMaintenanceWait";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().mySkillWait(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我接的 零工 待确认
     */
    public Observable<OrderSkillRecieveRes> myAcceptOddWait(int page, int who){
        String rateKey = "myAcceptOddWait";
        if (who == SKILL_RECIEVE_PROJECT){
            rateKey = "myAcceptProjectWait";
        }else if(who == SKILL_RECIEVE_WEIBAO){
            rateKey = "myAcceptMaintenanceWait";
        }else if(who == SKILL_RECIEVE_JIANLI){
            rateKey = "myAcceptSupervisorWait";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddWait(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我接的 零工 取消申请
     */
    public Observable<Object> myAcceptOddCancel(int odd_id, int who){
        String rateKey = "myAcceptOddCancel";
        if (who == SKILL_RECIEVE_PROJECT){
            rateKey = "myAcceptProjectCancel";
        }else if(who == SKILL_RECIEVE_WEIBAO){
            rateKey = "myAcceptMaintenanceCancel";
        }else if(who == SKILL_RECIEVE_JIANLI){
            rateKey = "myAcceptSupervisorCancel";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(odd_id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        if (who == SKILL_RECIEVE_PROJECT){
            req.setProject_id(odd_id);
        }else if(who == SKILL_RECIEVE_LINGGONG){
            req.setOdd_id(odd_id);
        }else if(who == SKILL_RECIEVE_WEIBAO){
            req.setMaintenance_id(odd_id);
        }else if(who == SKILL_RECIEVE_JIANLI){
            req.setSupervisor_id(odd_id);
        }

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddCancel(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 进行中
     */
    public Observable<OrderSkillDoingRes> mySkillDoing(int page, int who){
        String rateKey = "myPublishOddDoing";
        if(who == SKILL_RELEASE_WEIBAO){
            rateKey = "allMaintenanceDoing";
        }
        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().mySkillDoing(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我接的 零工 进行中
     */
    public Observable<OrderSkillDoingRes> myAcceptOddDoing(int page, int who){
        String rateKey = "myAcceptOddDoing";

        if (who == SKILL_RECIEVE_PROJECT){
            rateKey = "myAcceptProjectDoing";
        }else if(who == SKILL_RECIEVE_WEIBAO){
            rateKey = "myAcceptMaintenanceDoing";
        }else if(who == SKILL_RECIEVE_JIANLI){
            rateKey = "myAcceptSupervisorDoing";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddDoing(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 已完成
     */
    public Observable<OrderSkillFinishRes> mySkillFinish(int page, int who){
        String rateKey = "myPublishOddFinish";
        if(who == SKILL_RELEASE_WEIBAO){
            rateKey = "allMaintenanceFinish";
        }
        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().mySkillFinish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我接的 零工 已完成
     */
    public Observable<OrderSkillFinishRecieveRes> myAcceptOddFinish(int page, int who){
        String rateKey = "myAcceptOddFinish";
        if (who == Constants.SKILL_RECIEVE_PROJECT){
            rateKey = "myAcceptProjectFinish";
        }else if(who == Constants.SKILL_RECIEVE_WEIBAO){
            rateKey = "myAcceptMaintenanceFinish";
        }else if(who == SKILL_RECIEVE_JIANLI){
            rateKey = "myAcceptSupervisorFinish";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(page).append(10).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddFinish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 进行中 通过
     */
    public Observable<Object> myPublishOddSuccess(int id){
        String rateKey = "myPublishOddSuccess";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append(id)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setWork_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myPublishOddSuccess(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<Object> myPublishMaintenanceSuccess(int id, int who){
        String rateKey = "myPublishMaintenanceSuccess";
        if(who == COMPANY_RELEASE_JIANLI_DOING){
            rateKey = "mySupervisorSuccess";
        }
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append(id)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setWork_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myPublishOddSuccess(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 技术 我发布的 零工 进行中 付款
     */
    public Observable<Object> myPublishOddFail(MyPublicOddFailReq req){
        String rateKey = "myPublishOddFail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getReason())
                .append(type).append(userid).append(req.getWork_id())
                .append("security");


        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myPublishOddFail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 技术 我发布的 零工 进行中 进度
     */
    public Observable<MyPublishOddWorkRes> myPublishOddWorkProgress(int id){
        String rateKey = "myPublishOddWork";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myPublishOddWorkProgress(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发接的 零工 进行中 进度
     */
    public Observable<MyPublishOddWorkRes> myAcceptOddWork(int id){
        String rateKey = "myAcceptOddWork";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<Object> myAcceptOddSubmit(MyAcceptOddSubmitReq req){
        String rateKey = "myAcceptOddSubmit";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getApply_type()).append(req.getImages()).append(req.getLocation())
                .append(req.getOdd_id()).append(req.getSign_time())
                .append(req.getRemark()).append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptOddSubmit(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 申请列表
     */
    public Observable<ApplyProjRes> getApplyList(int id, int projType){
        String rateKey = "applyList";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(projType).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(id);
        req.setProject_type(projType);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getApplyList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 申请列表
     */
    public Observable<ApplyDetailRes> getApplyDetail(int id){
        String rateKey = "applyInfo";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setTechnician_id(id+"");
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getApplyDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<ApplyDetailRes> maintenanceInfo(GetMoneyReq req){
        String rateKey = "maintenanceInfo";

        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getId()).append(req.getPage())
                .append(req.getPageSize()).append(req.getType())
                .append(req.getUserid())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getApplyDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<Object> getApplyPass(ApplyPassReq req){
        String rateKey = "applyPass";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getProject_id())
                .append(req.getProject_type()).append(req.getTechnician_id())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));


        return config.getRetrofitService().getApplyPass(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    public Observable<SkillRecieveProjDetailRes> myAcceptProjectDetail(int id){
        String rateKey = "myAcceptProjectDetail";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setProject_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  我接的 项目 进度
     */
    public Observable<MyAcceptProjectWorkRes> myAcceptProjectWork(int id, int who){
        String rateKey = "myAcceptProjectWork";

        if (who == COMPANY_RELEASE_PROJECT_DOING){
            rateKey = "myProjectWork";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  我接的 项目 进度 列表
     */
    public Observable<SignRes> myAcceptProjectSignList(int id, int who){
        String rateKey = "myAcceptProjectSignList";
        if (who == COMPANY_RELEASE_PROJECT_DOING || who == COMPANY_RELEASE_PROJECT_DONE){
            rateKey = "myProjectWorkSign";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id)
                .append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(id);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectSignList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  我接的 维保 进度 列表
     */
    public Observable<WeiBaoProgRes> myAcceptMaintenanceWork(int id, int who){
        String rateKey = "myAcceptMaintenanceWork";
        if(who == SKILL_RECIEVE_JIANLI){
            rateKey = "myAcceptSupervisorWork";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id)
                .append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        if(who == SKILL_RECIEVE_JIANLI){
            req.setSupervisor_id(id);
        }else{
            req.setMaintenance_id(id);
        }

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptMaintenanceWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  我接的 项目 进度 拍照 列表
     */
    public Observable<SkillProjProgPhotoRes> myAcceptProjectWorkList(int projId, int worksId, int who){
        String rateKey = "myAcceptProjectWorkList";

        if (who == COMPANY_RELEASE_PROJECT_DONE || who == COMPANY_RELEASE_PROJECT_DOING){
            rateKey = "myProjectWorkList";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(projId)
                .append(type).append(userid).append(worksId)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(projId);
        req.setWorks_id(worksId);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectWorkList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  我接的 项目 进度 拍照 列表
     */
    public Observable<MyPublishOddWorkRes> myPublishMaintenanceWork(int projId, int who){
        String rateKey = "myPublishMaintenanceWork";
        if(who == COMPANY_RELEASE_JIANLI_DOING ||
                who == COMPANY_RELEASE_JIANLI_DONE){
            rateKey = "mySupervisorWork";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(projId)
                .append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        if(who == COMPANY_RELEASE_JIANLI_DOING ||
                who == COMPANY_RELEASE_JIANLI_DONE){
            req.setSupervisor_id(projId);
        }else{
            req.setMaintenance_id(projId);
        }

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myPublishOddWorkProgress(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  我接的 项目 进度 拍照 提交
     */
    public Observable<Object> myAcceptProjectWorkSub(GetMoneyReq req){
        String rateKey = "myAcceptProjectWorkSub";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectWorkSub(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     *  我接的 项目 进度 签到
     */
    public Observable<Object> myAcceptProjectSign(GetMoneyReq req){
        String rateKey = "myAcceptProjectSign";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectSign(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  我接的 维保 进度 签到
     */
    public Observable<Object> myAcceptMaintenanceSubmit(GetMoneyReq req, int who){
        String rateKey = "myAcceptMaintenanceSubmit";
        if(who == SKILL_RECIEVE_JIANLI){
            rateKey="myAcceptSupervisorSubmit";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        if(who == SKILL_RECIEVE_JIANLI){
            sb.append(time+"").append(req.getImages()).append(req.getLocation())
                    .append(req.getSupervisor_id())
                    .append(type).append(userid)
                    .append("security");
        }else{
            sb.append(time+"").append(req.getImages()).append(req.getLocation())
                    .append(req.getMaintenance_id())
                    .append(type).append(userid)
                    .append("security");
        }


        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myAcceptProjectSign(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  审核不通过
     */
    public Observable<Object> myProjectWorkFail(GetMoneyReq req, int who){
        String rateKey = "myAcceptProjectSign";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder();
        if (who == SKILL_RELEASE_LINGGONG_NO_PASS){
            rateKey = "myPublishOddFail";
            sb.append(rateKey);
            sb.append(time+"").append(req.getImages()).append(req.getReason())
                    .append(type).append(userid).append(req.getWork_id())
                    .append("security");

        }else if(who == NO_PASS){
            sb.append(rateKey);
            sb.append(time+"").append(req.getImages()).append(req.getLocation())
                    .append(req.getProject_id())
                    .append(type).append(userid)
                    .append("security");
        }else if(who == COMPANY_PROJECT_NO_PASS){
            rateKey = "myProjectWorkFail";
            sb.append(rateKey);
            sb.append(time+"").append(req.getImages()).append(req.getProject_id())
                    .append(req.getReason())
                    .append(req.getStage())
                    .append(type).append(userid).append(req.getWork_id())
                    .append(req.getWorks_id())
                    .append("security");
        }else if(who == JIA_TYPE){
            //甲方申诉
            rateKey = "myPublishAppeal";
            sb.append(rateKey);
            sb.append(time+"").append(req.getAppeal_operate()).append(req.getContent())
                    .append(req.getImages())
                    .append(req.getProject_id())
                    .append(req.getProject_type())
                    .append(type).append(userid)
                    .append("security");
        }else if(who == YI_TYPE){
            //甲方申诉
            rateKey = "myAcceptAppeal";
            sb.append(rateKey);
            sb.append(time+"").append(req.getAppeal_operate()).append(req.getContent())
                    .append(req.getImages())
                    .append(req.getProject_id())
                    .append(req.getProject_type())
                    .append(type).append(userid)
                    .append("security");
        }else if(who == COMPANY_RELEASE_WEIBAO){
            rateKey = "myPublishMaintenanceFail";
            sb.append(rateKey);
            sb.append(time+"").append(req.getImages()).append(req.getProject_id())
                    .append(req.getReason())
                    .append(type).append(userid).append(req.getWork_id())
                    .append("security");
        }else if(who == COMPANY_RELEASE_JIANLI){
            rateKey = "mySupervisorFail";
            sb.append(rateKey);
            sb.append(time+"").append(req.getImages()).append(req.getProject_id())
                    .append(req.getReason())
                    .append(type).append(userid).append(req.getWork_id())
                    .append("security");
        }

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myProjectWorkFail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 审核不通过
     * @param req
     * @return
     */
    public Observable<Object> myProjectWorkPass(GetMoneyReq req){
        String rateKey = "myProjectWorkPass";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().myProjectWorkPass(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * ids 换项目
     * @param
     * @return
     */
    public Observable<ExpensesInfoRes> typeExpenses(String s){
        String rateKey = "typeExpenses";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(s)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setType_ids(s);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().typeExpenses(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 服务费用
     * @param
     * @return
     */
    public Observable<GetPercentRes> getPercent(){
        String rateKey = "getPercent";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setProject_type(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getPercent(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 服务费用
     * @param
     * @return
     */
    public Observable<GetPercentRes> publishSupervisorPrice(){
        String rateKey = "publishSupervisorPrice";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getPercent(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 监理费用
     * @param
     * @return
     */
    public Observable<GetPercentRes> countSupervisorExpenses(String supervisor_fee){
        String rateKey = "countSupervisorExpenses";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(supervisor_fee)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setSupervisor_fee(supervisor_fee);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getPercent(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 维保服务费用
     * @param
     * @return
     */
    public Observable<GetPercentRes> countMaintenanceExpenses(String door_fee){
        String rateKey = "countMaintenanceExpenses";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(door_fee)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setDoor_fee(door_fee);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getPercent(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 服务费用
     * @param
     * @return
     */
    public Observable<GetPercentRes> publishOddPrice(){
        String rateKey = "publishOddPrice";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setType(type);
        req.setUserid(userid);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishOddPrice(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 项目类型
     * @param
     * @return
     */
    public Observable<ProjectTypeRes> publishOddType(){
        String rateKey = "publishOddType";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setType(type);
        req.setUserid(userid);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishOddType(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 发布项目
     * @param
     * @return
     */
    public Observable<ReleaseProjRes> publishProject(ReleaseProjReq req, boolean jianli){
        String rateKey = "publishProject";
        if(jianli){
            rateKey = "publishSupervisor";
        }

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);

        if(jianli){
            sb.append(time+"").append(req.getAddress())
                    .append(req.getCity()).append(req.getContact())
                    .append(req.getContact_mobile()).append(req.getDescribe())
                    .append(req.getDistrict()).append(req.getImages())
                    .append(req.getProject_class()).append(req.getProject_fee())
                    .append(req.getProject_type()).append(req.getProvince())
                    .append(req.getService_cost()).append(req.getSupervisor_fee())
                    .append(req.getSupervisor_time()).append(req.getTitle())
                    .append(req.getTotal_price()).append(type)
                    .append(userid)
                    .append("security");

        }else{
            sb.append(time+"").append(req.getAddress()).append(req.getBuild_time())
                    .append(req.getCity()).append(req.getContact())
                    .append(req.getContact_mobile()).append(req.getDescribe())
                    .append(req.getDistrict()).append(req.getImages())
                    .append(req.getProject_class()).append(req.getProject_fee())
                    .append(req.getProject_type()).append(req.getProvince())
                    .append(req.getService_cost()).append(req.getTitle())
                    .append(req.getTotal_price()).append(type)
                    .append(userid)
                    .append("security");
        }


        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 发布维保
     * @param
     * @return
     */
    public Observable<ReleaseProjRes> publishMaintenance(ReleaseHelpReq req){
        String rateKey = "publishMaintenance";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getAddress())
                .append(req.getCity()).append(req.getContact())
                .append(req.getContact_mobile()).append(req.getDescribe())
                .append(req.getDistrict()).append(req.getDoor_fee()).append(req.getDoor_time()).append(req.getImages())
                .append(req.getProject_brand())
                .append(req.getProject_class())
                .append(req.getProject_type()).append(req.getProvince()).append(req.getService_fee())
                .append(req.getTitle()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 支付项目
     * @param req
     * @return
     */
    public Observable<BalancePayRes> balancePay(PayFeeReq req){
        String rateKey = "balancePay";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getIs_combination())
                .append(req.getOrder_no())
                .append(req.getPay_fee()).append(req.getProject_type())
                .append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().balancePay(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 支付项目 成功回调
     * @param req
     * @return
     */
    public Observable<Object> paySuccess(PayFeeReq req){
        String rateKey = "paySuccess";

        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getOrder_no())
                .append(req.getProject_type())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().balancePay(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 支付项目 成功回调
     * @param req
     * @return
     */
    public Observable<PayRes> aliPay(PayFeeReq req){
        String rateKey = "aliPay";

        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getBalance()).append(req.getOrder_no())
                .append(req.getPrice())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().aliPay(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    public Observable<PayRes> wxPay(PayFeeReq req){
        String rateKey = "wxPay";

        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getBalance()).append(req.getIp())
                .append(req.getOrder_no()).append(req.getPrice())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().aliPay(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 取消支付
     * @param req
     * @return
     */
    public Observable<Object> cancelPublish(PayFeeReq req){
        String rateKey = "cancelPublish";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getOrder_no())
                .append(req.getProject_type()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().cancelPublish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 取消支付
     * @param req
     * @return
     */
    public Observable<Object> destoryOrder(PayFeeReq req){
        String rateKey = "destoryOrder";

        long time = System.currentTimeMillis()/1000;
        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getOrder_no())
                .append(req.getProject_type())
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().cancelPublish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 发布零工
     * @param
     * @return
     */
    public Observable<ReleaseProjRes> publishOdd(ReleaseSkillReq req){
        String rateKey = "publishOdd";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getAddress()).append(req.getBuild_time())
                .append(req.getCity()).append(req.getContact())
                .append(req.getContact_mobile()).append(req.getDaily_wage()).append(req.getDescribe())
                .append(req.getDistrict()).append(req.getImages()).append(req.getOdd_fee())
                .append(req.getPeople_numbers())
                .append(req.getProject_type()).append(req.getProvince())
                .append(req.getService_fee()).append(req.getTitle())
                .append(req.getTotal_day()).append(req.getTotal_fee()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 维保最低单价
     * @param
     * @return
     */
    public Observable<GetPercentRes> publishMaintenancePrice(){
        String rateKey = "publishMaintenancePrice";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishOddPrice(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }




    /**
     * 发布互助
     * @param
     * @return
     */
    public Observable<ReleaseProjRes> publishMutual(ReleaseSkillReq req){
        String rateKey = "publishMutual";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getAddress())
                .append(req.getCity()).append(req.getContact())
                .append(req.getContact_mobile()).append(req.getDescribe())
                .append(req.getDistrict()).append(req.getImages())
                .append(req.getProvince())
                .append(req.getTitle())
                .append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().publishProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 完善信息
     * @param
     * @return
     */
    public Observable<CheckFinishRes> checkFinish(){
        String rateKey = "checkFinish";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().checkFinish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 区县选择
     * @return
     */
    public Observable<ArrayList<Province>> getAddressData(){
        return Observable.create(new Observable.OnSubscribe<ArrayList<Province>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Province>> subscriber) {
                ArrayList<Province> data = new ArrayList<>();
                List<CityBean.CityItemBean> sourceData = getCities();
                for(CityBean.CityItemBean bean : sourceData){
                    switch (Integer.valueOf(bean.getRegion_type())){
                        case 1:
                            //省份
                            Province province = new Province(String.valueOf(bean.getId()), bean.getName());
                            data.add(province);
                            break;

                        case 2:
                            //市区
                            City city = new City(String.valueOf(bean.getId()), bean.getName());
                            city.setProvinceId(String.valueOf(bean.getParent_id()));
                            for(Province p : data){
                                if (TextUtils.equals(bean.getParent_id(), p.getAreaId())){
                                    p.getCities().add(city);
                                }
                            }
                            break;

                        case 3:
                            //县
                            County county = new County(String.valueOf(bean.getId()), bean.getName());
                            county.setCityId(String.valueOf(bean.getParent_id()));
                            for(Province p: data){
                                for(City city1: p.getCities()){
                                    if (TextUtils.equals(bean.getParent_id(),city1.getAreaId())){
                                        city1.getCounties().add(county);
                                    }
                                }
                            }
                            break;
                    }

                }

                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    private List<CityBean.CityItemBean> getCities() {
        List<CityBean.CityItemBean> resourceList = new ArrayList<>();
        try {
            InputStream is = App.getContext().getAssets().open("city.json");
            String text = readTextFromSDcard(is);
            Gson gson = new Gson();
            resourceList = gson.fromJson(text, new TypeToken<List<CityBean.CityItemBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return resourceList;
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    public Observable<Integer> getCityId(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                List<CityBean.CityItemBean> list = getCities();
                String cityString="";
                if (city.contains("市")) {
                    cityString = city.substring(0, city.length() - 1);
                }
                for(CityBean.CityItemBean item: list){
                    if (TextUtils.equals(item.getName(), cityString)){
                        subscriber.onNext(Integer.valueOf(item.getId()));

                        break;
                    }
                }

                subscriber.onCompleted();
            }
        });
    }


    public Observable<HomeSearchRes> indexSearch(FindProjReq req){
        String rateKey = "indexSearch";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(city).append(req.getKeyword())
                .append(req.getLat()).append(req.getLng())
                .append(req.getNearby()).append(req.getPage()).append(req.getPageSize())
                .append(req.getProject_type()).append(req.getTime_type())
                .append(req.getType())
                .append(userid)
                .append("security");

        req.setCity(city);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().indexSearch(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());

    }


    /**
     * url 转 base64
     * @return
     */
    public Observable<List<String>> urlToBase64(List<String> images){
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> result = new ArrayList<>();
                for(String url: images){
                    result.add(url2StrByBase64(url));
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    private  String url2StrByBase64(@NonNull String imgURL){
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            data = new byte[inStream.available()];
            inStream.read(data);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return "data:image/png;base64,"+ Base64.encodeToString(data, Base64.DEFAULT);
    }


    /**
     * 更新项目
     * @param
     * @return
     */
    public Observable<Object> updateProject(ReleaseProjReq req){
        String rateKey = "updateProject";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContact()).append(req.getContact_mobile()).append(req.getProject_id())
                .append(req.getTitle()) .append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().updateProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 更新零工
     * @param
     * @return
     */
    public Observable<Object> updateOdd(ReleaseSkillReq req){
        String rateKey = "updateOdd";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContact()).append(req.getContact_mobile())
                .append(req.getOdd_id())
                .append(req.getTitle()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().updateProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 更新维保
     * @param
     * @return
     */
    public Observable<Object> updateMaintenance(ReleaseHelpReq req){
        String rateKey = "updateMaintenance";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContact()).append(req.getContact_mobile())
                .append(req.getMaintenance_id())
                .append(req.getTitle()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().updateProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    public Observable<SkillRes> goodSkills(int action){
        String rateKey = "goodSkills";
        if(action == 1){
            //评论标签
            rateKey = "evaluateTag";
        }else if(action == 2){
            //申诉
            rateKey = "appealTag";
        }else{

        }

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().goodSkills(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 计算零工费用
     * @param
     * @return
     */
    public Observable<OddFeeRes> countOddExpenses(ReleaseSkillReq req){
        String rateKey = "countOddExpenses";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getDaily_wage()).append(req.getPeople_numbers())
                .append(req.getTotal_day())
                .append("security");

        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().countOddExpenses(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    //甲方申诉
    public Observable<Object> myPublishAppeal(ReleaseSkillReq req){
        String rateKey = "myPublishAppeal";

        int userid;         if(TextUtils.isEmpty(User.getInstance().getUserId())){             userid = 0;         }else{             userid = Integer.valueOf(User.getInstance().getUserId());         }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContact()).append(req.getContact_mobile())
                .append(req.getOdd_id())
                .append(req.getTitle()).append(type)
                .append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().updateProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    //甲方申诉
    public Observable<MaintenanceTypeRes> maintenanceType(){
        String rateKey = "maintenanceType";

        int userid;
        if(TextUtils.isEmpty(User.getInstance().getUserId())){
            userid = 0;
        }else{
            userid = Integer.valueOf(User.getInstance().getUserId());
        }
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().maintenanceType(setBody(rateKey, time, req))
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
     *  检查更新
     */
    public Observable<List<UpdateVersionBean.DataBean>> updateVersion(){
        return config.getRetrofitService().updateVersion("caiji")
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
