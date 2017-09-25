package com.xunao.diaodiao.Model;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BaseRequestBean;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Bean.DocReq;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.EvaluateReq;
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
import com.xunao.diaodiao.Bean.HasRateRes;
import com.xunao.diaodiao.Bean.HeadIconReq;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Bean.LoginBaseReq;
import com.xunao.diaodiao.Bean.LoginBean;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.MyAcceptOddSubmitReq;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Bean.MyAppealDetailRes;
import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.RateDetailReq;
import com.xunao.diaodiao.Bean.RateDetailRes;
import com.xunao.diaodiao.Bean.RateReq;
import com.xunao.diaodiao.Bean.RegisterBean;
import com.xunao.diaodiao.Bean.RegisterRespBean;
import com.xunao.diaodiao.Bean.SelectBean;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Bean.SkillProjRecieveDetailRes;
import com.xunao.diaodiao.Bean.SkillRecieveProjDetailRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillRecievePresenter;
import com.xunao.diaodiao.Present.ProjectRes;
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

import static android.R.attr.id;
import static com.xunao.diaodiao.Common.Constants.CACHE_DIR;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
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
        String useid = User.getInstance().getUserId();
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        StringBuilder sb = new StringBuilder("completeInfo");
        sb.append(time+"").append(req.getAddress()).append(req.getCard_back())
                .append(req.getCard_front()).append(req.getCity())
                .append(req.getContact())
                .append(req.getContact_card()).append(req.getContact_mobile())
                .append(req.getDistrict()).append(req.getName())
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
    public Observable<String> cancelCollect(int id){
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
    public Observable<String> applyCash(GetCashRes req){
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
    public Observable<String> bindingCard(BindBankReq req){
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
        .append(req.getMobile()).append(req.getName());
        if(!isCode){
            sb.append(req.getTrade_no());
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
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(type)
                .append(User.getInstance().getUserId())
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
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
    public Observable<TypeInfoRes> getTypeInfo(){
        String rateKey = "typeInfo";

        long time = System.currentTimeMillis()/1000;
        int userid = Integer.valueOf(User.getInstance().getUserId());
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
     *  拿到资料库文件
     */
    public Observable<ProjectRes> getMyWork(){
        String rateKey = "myWork";

        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        }
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getLat())
                .append(req.getLng()).append(req.getNearby())
                .append(req.getPage())
                .append(req.getPageSize()).append(req.getTime_type());
        if (!TextUtils.isEmpty(req.getType())){
            sb.append(req.getType());
        }
        sb.append("security");

        req.setVerify(sb.toString());


        return config.getRetrofitService().getProjectList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     *  拿到资料库文件
     */
    public Observable<FindProjDetailRes> getFindProjDetail(int id){
        String rateKey = "projectDetail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(userid)
            .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().getFindProjDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //申请项目
    public Observable<String> postProject(int id, int types){
        String rateKey = "applyProject";
        if (types == 0){

        }else{
            rateKey = "applyOdd";
        }
        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().postProject(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //收藏
    public Observable<String> collectWork(int id, int types){
        String rateKey = "collectWork";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().collectWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<FindLingGongRes> getFindLingGongDetail(int id){
        String rateKey = "oddDetail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().getFindLingGongDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<JoinDetailRes> getCompanyInfo(int id, int page){
        String rateKey = "companyInfo";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(page).append(10).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setPage(page);
        req.setPageSize(10);
        req.setVerify(sb.toString());

        return config.getRetrofitService().getCompanyInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    public Observable<GetOddInfoRes> getOddInfo(int id, int page){
        String rateKey = "oddInfo";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(page).append(10).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setId(id);
        req.setPageSize(10);
        req.setPage(page);
        req.setVerify(sb.toString());

        return config.getRetrofitService().getOddInfo(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //我发布的  详情
    public Observable<SkillProjDetailRes> mySkillProjDetail(int id){
        String rateKey = "myPublishOddDetail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().mySkillProjDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    //我接的 零工 详情
    public Observable<SkillProjRecieveDetailRes> mySkillProjRecieveDetail(int id){
        String rateKey = "myAcceptOddDetail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue(TYPE_KEY, 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().mySkillProjRecieveDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 提交意见
     */
    public Observable<String> submitSuggest(String content){
        String rateKey = "feedBack";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(content).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setContent(content);
        req.setVerify(sb.toString());

        return config.getRetrofitService().submitSuggest(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 评价
     */
    public Observable<String> toEvaluate(EvaluateReq req){
        String rateKey = "toEvaluate";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getContent()).append(req.getImages())
                .append(req.getPoint()).append(req.getProject_id()).append(req.getProject_type())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

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
        }

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myProjectWait(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 待确认
     */
    public Observable<OrderSkillRes> mySkillWait(int page){
        String rateKey = "myPublishOddWait";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

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
        }

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddWait(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我接的 零工 取消申请
     */
    public Observable<String> myAcceptOddCancel(int odd_id, int who){
        String rateKey = "myAcceptOddCancel";
        if (who == SKILL_RECIEVE_PROJECT){
            rateKey = "myAcceptProjectCancel";
        }

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        }

        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddCancel(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 进行中
     */
    public Observable<OrderSkillDoingRes> mySkillDoing(int page){
        String rateKey = "myPublishOddDoing";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

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
        }

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddDoing(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 已完成
     */
    public Observable<OrderSkillFinishRes> mySkillFinish(int page){
        String rateKey = "myPublishOddFinish";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

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
        }

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddFinish(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发布的 零工 进行中 通过
     */
    public Observable<String> myPublishOddSuccess(int id){
        String rateKey = "myPublishOddSuccess";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myPublishOddSuccess(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 技术 我发布的 零工 进行中 付款
     */
    public Observable<String> myPublishOddFail(MyPublicOddFailReq req){
        String rateKey = "myPublishOddFail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getReason())
                .append(type).append(userid).append(req.getWork_id())
                .append("security");


        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myPublishOddFail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    /**
     * 技术 我发布的 零工 进行中 进度
     */
    public Observable<MyPublishOddWorkRes> myPublishOddWorkProgress(int id){
        String rateKey = "myPublishOddWork";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myPublishOddWorkProgress(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 技术 我发接的 零工 进行中 进度
     */
    public Observable<MyPublishOddWorkRes> myAcceptOddWork(int id){
        String rateKey = "myAcceptOddWork";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setOdd_id(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddWork(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<String> myAcceptOddSubmit(MyAcceptOddSubmitReq req){
        String rateKey = "myAcceptOddSubmit";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getOdd_id()).append(req.getSign_time())
                .append(req.getRemark()).append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptOddSubmit(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     * 申请列表
     */
    public Observable<ApplyProjRes> getApplyList(int id, int projType){
        String rateKey = "applyList";

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().getApplyList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 申请列表
     */
    public Observable<ApplyDetailRes> getApplyDetail(int id){
        String rateKey = "applyList";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(id);
        req.setVerify(sb.toString());

        return config.getRetrofitService().getApplyDetail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }


    public Observable<String> getApplyPass(ApplyPassReq req){
        String rateKey = "applyList";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        int type = ShareUtils.getValue(TYPE_KEY, 0);

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");


        return config.getRetrofitService().getApplyPass(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    public Observable<SkillRecieveProjDetailRes> myAcceptProjectDetail(int id){
        String rateKey = "myAcceptProjectDetail";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setProject_id(id);
        req.setVerify(sb.toString());

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

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(id).append(type).append(userid)
                .append("security");

        GetMoneyReq req = new GetMoneyReq();
        req.setUserid(userid);
        req.setType(type);
        req.setProject_id(id);
        req.setVerify(sb.toString());

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

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptProjectSignList(setBody(rateKey, time, req))
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

        int userid = Integer.valueOf(User.getInstance().getUserId());
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
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptProjectWorkList(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  我接的 项目 进度 拍照 提交
     */
    public Observable<String> myAcceptProjectWorkSub(GetMoneyReq req){
        String rateKey = "myAcceptProjectWorkSub";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptProjectWorkSub(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }



    /**
     *  我接的 项目 进度 签到
     */
    public Observable<String> myAcceptProjectSign(GetMoneyReq req){
        String rateKey = "myAcceptProjectSign";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myAcceptProjectSign(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     *  审核不通过
     */
    public Observable<String> myProjectWorkFail(GetMoneyReq req, int who){
        String rateKey = "myAcceptProjectSign";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myProjectWorkFail(setBody(rateKey, time, req))
                .compose(RxUtils.handleResult());
    }

    /**
     * 审核不通过
     * @param req
     * @return
     */
    public Observable<String> myProjectWorkPass(GetMoneyReq req){
        String rateKey = "myProjectWorkPass";

        int userid = Integer.valueOf(User.getInstance().getUserId());
        int type = ShareUtils.getValue("TYPE", 0);
        long time = System.currentTimeMillis()/1000;

        StringBuilder sb = new StringBuilder(rateKey);
        sb.append(time+"").append(req.getImages()).append(req.getLocation())
                .append(req.getProject_id())
                .append(type).append(userid)
                .append("security");

        req.setUserid(userid);
        req.setType(type);
        req.setVerify(sb.toString());

        return config.getRetrofitService().myProjectWorkPass(setBody(rateKey, time, req))
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
     *  更改个人地址
     */
    public Observable<String> changeAddress(String address){

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers());

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
