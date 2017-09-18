package com.xunao.diaodiao.Common;


import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BaseResponseBean;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.FreindBean;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Bean.HasRateRes;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.MyBean;
import com.xunao.diaodiao.Bean.MyComplaintRes;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Bean.NumberBean;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.RateDetailRes;
import com.xunao.diaodiao.Bean.RegisterRespBean;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Bean.BaseBean;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.ResponseBean;
import com.xunao.diaodiao.Bean.SelectRespBean;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
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
import rx.Observable;


public interface RetrofitService {
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("data/{type}/20/{page}")
    Observable<BaseBean<String>> getNewsList(@Path("type") String type, @Path("page") int page);

    @FormUrlEncoded
    @POST(ApiConstants.USER_REGISTER)
    Observable<BaseBean<String>> register(@FieldMap Map<String, String> map);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<LoginResBean>> login(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<HomeResponseBean>> getFirstPage(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> checkPhone(@Body RequestBody body);

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
    Observable<BaseBean<HasRateRes>> getHasRate(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<RateDetailRes>> getRateDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyBean>> getUserInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<MyFavoriteRes>> getCollectList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<BankListRes>> getBankList(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> applyCash(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> bindingCard(@Body RequestBody body);

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
    Observable<BaseBean<ProjectRes>> getMyWork(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindProjectRes>> getProjectList(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindProjDetailRes>> getFindProjDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> postProject(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<FindLingGongRes>> getFindLingGongDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<JoinDetailRes>> getCompanyInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<GetOddInfoRes>> getOddInfo(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> submitSuggest(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderCompRes>> myProjectWait(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillRes>> mySkillWait(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillDoingRes>> mySkillDoing(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<OrderSkillFinishRes>> mySkillFinish(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> mySkillProjDetail(@Body RequestBody body);

    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ApplyProjRes>> getApplyList(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<ApplyDetailRes>> getApplyDetail(@Body RequestBody body);


    @POST(ApiConstants.BASE_URL_INDEX)
    Observable<BaseBean<String>> getApplyPass(@Body RequestBody body);

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