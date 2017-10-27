package com.xunao.diaodiao.Common;


import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.HasRateRes;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.HomeSearchRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Bean.MyAppealDetailRes;
import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Bean.NumberBean;
import com.xunao.diaodiao.Bean.OddFeeRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Bean.OrderSkillRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.Bean.PayRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.ProjectTypeRes;
import com.xunao.diaodiao.Bean.RateDetailRes;
import com.xunao.diaodiao.Bean.RegisterRespBean;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Bean.BaseBean;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.ResponseBean;
import com.xunao.diaodiao.Bean.SelectRespBean;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Bean.SkillProjRecieveDetailRes;
import com.xunao.diaodiao.Bean.SkillRecieveProjDetailRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Bean.UpdateInfo;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Bean.WeiXinRes;
import com.xunao.diaodiao.Model.UserInfo;
import com.xunao.diaodiao.Present.ProjectRes;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitService {

    @GET("{id}")
    Observable<String> getAppControl(@Path("id") String id);

    @FormUrlEncoded
    @POST(ApiConstants.USER_REGISTER)
    Observable<BaseBean<String>> register(@FieldMap Map<String, String> map);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<LoginResBean>> login(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<HomeResponseBean>> getFirstPage(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<UpdateInfo>> versionControl(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> checkPhone(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<UserInfo>> bindingWx(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<RegisterRespBean>> checkExistPhone(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SelectRespBean>> select(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<LoginResBean>> fillInfor(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> forgetPwd(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<GetMoneyRes>> getMoney(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyRateRes>> getRate(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyComplaintRes>> getMyComplaint(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyAppealDetailRes>> myAppealDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<HasRateRes>> getHasRate(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<RateDetailRes>> getRateDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyBean>> getUserInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyFavoriteRes>> getCollectList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> cancelCollect(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myProjectCancel(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<BankListRes>> getBankList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<CashRecordRes>> cashRecord(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> applyCash(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> bindingCard(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<AddBankRes>> bindingCardGetVerify(@Body RequestBody body);

    //上传图片
    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<HeadIconRes>> changeHeadIcon(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<PersonalRes>> getPersonalInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<List<DocRes>>> getDataBase(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<TypeInfoRes>> getTypeInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MessageListRes>> messageList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> readMessage(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ProjectRes>> getMyWork(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindProjectRes>> getProjectList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> projectDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindProjDetailRes>> getFindProjDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> postProject(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> collectWork(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindLingGongRes>> getFindLingGongDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<JoinDetailRes>> getCompanyInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<GetOddInfoRes>> getOddInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> submitSuggest(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderCompRes>> myProjectWait(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillRes>> mySkillWait(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillRecieveRes>> myAcceptOddWait(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myAcceptOddCancel(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillDoingRes>> mySkillDoing(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillDoingRes>> myAcceptOddDoing(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillFinishRes>> mySkillFinish(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillFinishRecieveRes>> myAcceptOddFinish(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyPublishOddWorkRes>> myPublishOddWorkProgress(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyPublishOddWorkRes>> myAcceptOddWork(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myPublishOddSuccess(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myAcceptOddSubmit(@Body RequestBody body);



    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myPublishOddFail(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SkillProjDetailRes>> mySkillProjDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SkillProjRecieveDetailRes>> mySkillProjRecieveDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ApplyProjRes>> getApplyList(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ApplyDetailRes>> getApplyDetail(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> getApplyPass(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SkillRecieveProjDetailRes>> myAcceptProjectDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyAcceptProjectWorkRes>> myAcceptProjectWork(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> myAcceptProjectSign(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SignRes>> myAcceptProjectSignList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<SkillProjProgPhotoRes>> myAcceptProjectWorkList(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> myAcceptProjectWorkSub(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myProjectWorkFail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> myProjectWorkPass(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ExpensesInfoRes>> typeExpenses(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<GetPercentRes>> getPercent(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<GetPercentRes>> publishOddPrice(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ProjectTypeRes>> publishOddType(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ReleaseProjRes>> publishProject(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> balancePay(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<PayRes>> aliPay(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<CheckFinishRes>> checkFinish(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<Object>> updateProject(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OddFeeRes>> countOddExpenses(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> getRegionId(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<HomeSearchRes>> indexSearch(@Body RequestBody body);

    @FormUrlEncoded
    @POST("personalCenter/personalData")
    Observable<BaseBean<UserInfo>> getUserInfo(@Field("token") String token);

    @GET("user/checkEmail")
    Observable<BaseBean<String>> checkEmail(@Query("email") String email);



    @GET("user/changeThePassword")
    Observable<BaseBean<String>> changePwd(@Query("mobile") String mobile, @Query("newPassword") String newPassword);

    @GET("home/hotKeys")
    Observable<BaseBean<List<String>>> getHotWord();

    @FormUrlEncoded
    @POST("user/checkToken")
    Observable<BaseResponseBean> checkToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("user/logout")
    Observable<BaseBean<String>> logout(@Field("token") String token);


    @FormUrlEncoded
    @POST("home/search")
    Observable<SearchResponseBean> getSearchResult(@FieldMap Map<String, String> map, @FieldMap Map<String, Integer> intMap);

    @FormUrlEncoded
    @POST("personalCenter/addTheAddress")
    Observable<BaseResponseBean> changeAddress(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("personalCenter/edit")
    Observable<BaseBean<String>> changeSex(@Field("sex") int sex, @Field("token") String token);

    @FormUrlEncoded
    @POST("version/getVersionsByPlatform")
    Observable<BaseBean<List<UpdateVersionBean.DataBean>>> updateVersion(@Field("platform") String platform);




    @FormUrlEncoded
    @POST("data/collect")
    Observable<BaseResponseBean> postCollectTxt(@FieldMap Map<String, String> map, @FieldMap Map<String, Float> doubleMap, @FieldMap Map<String, Integer> integerMap);

    //上传图片
    @Multipart
    @POST("data/uploadPublicPhoto")
    Observable<ResponseBody> postCollectImg(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("data/getPartInfoByCode")
    Observable<NumberBean> getPartInfoByCode(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user/getUsersByProject")
    Observable<BaseBean<List<FreindBean>>> getFriends(@Field("token") String token);

}