package com.bz.fitness.Common;

import com.bz.fitness.Bean.BaseBean;
import com.bz.fitness.Bean.BaseResponseBean;
import com.bz.fitness.Bean.HomeResponseBean;
import com.bz.fitness.Bean.NumberBean;
import com.bz.fitness.Bean.PackageBean;
import com.bz.fitness.Bean.ResponseBean;
import com.bz.fitness.Bean.SearchResponseBean;
import com.bz.fitness.Bean.UpdateVersionBean;
import com.bz.fitness.Model.UserInfo;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @FormUrlEncoded
    @POST(ApiConstants.USER_LOGIN)
    Observable<ResponseBean> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("personalCenter/personalData")
    Observable<BaseBean<UserInfo>> getUserInfo(@Field("token") String token);

    @GET("user/checkMobile")
    Observable<BaseBean<String>> checkPhone(@Query("mobile") String mobile);

    @GET("user/checkEmail")
    Observable<BaseBean<String>> checkEmail(@Query("email") String email);

    @GET("user/changePassword/checkMobile")
    Observable<BaseBean<String>> checkExistPhone(@Query("mobile") String mobile);

    @GET("user/changeThePassword")
    Observable<BaseBean<String>> changePwd(@Query("mobile") String mobile, @Query("newPassword") String newPassword);

    @GET("home/hotKeys")
    Observable<BaseBean<List<String>>> getHotWord();

    @FormUrlEncoded
    @POST("data/packagingForm")
    Observable<PackageBean> packagingForm(@Field("project") String project);

    @FormUrlEncoded
    @POST("user/checkToken")
    Observable<BaseResponseBean> checkToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("user/logout")
    Observable<BaseBean<String>> logout(@Field("token") String token);

    @GET("home/getFirstPage")
    Observable<HomeResponseBean> getFirstPage();

    @FormUrlEncoded
    @POST("home/search")
    Observable<SearchResponseBean> getSearchResult(@FieldMap Map<String, String> map, @FieldMap Map<String, Integer> intMap);

    @FormUrlEncoded
    @POST("personalCenter/addVipDuration")
    Observable<BaseBean<UserInfo.PersonBean>> addVipDuration(@Field("token") String token, @Field("vipDateline") long vipDateline, @Field("gold") int gold);

    @FormUrlEncoded
    @POST("personalCenter/addTheAddress")
    Observable<BaseResponseBean> changeAddress(@FieldMap Map<String, String> map);

    //上传图片
    @Multipart
    @POST("personalCenter/updateAvatar")
    Observable<ResponseBody> changeHeadIcon(@PartMap Map<String, RequestBody> map);


    @FormUrlEncoded
    @POST("personalCenter/edit")
    Observable<BaseBean<String>> changeSex(@Field("sex") int sex, @Field("token") String token);

    @FormUrlEncoded
    @POST("version/getVersionsByPlatform")
    Observable<BaseBean<List<UpdateVersionBean.DataBean>>> updateVersion(@Field("platform") String platform);

    @FormUrlEncoded
    @POST("personalCenter/complaintsSuggestions")
    Observable<BaseBean<String>> submitSuggest(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("personalCenter/goldAndExp")
    Observable<BaseBean<UserInfo.PersonBean>> signToday(@Field("token") String token, @FieldMap Map<String, Integer> map);

    @FormUrlEncoded
    @POST("data/collect")
    Observable<BaseResponseBean> postCollectTxt(@FieldMap Map<String, String> map, @FieldMap Map<String, Double> doubleMap, @FieldMap Map<String, Integer> integerMap);

    //上传图片
    @Multipart
    @POST("data/uploadPublicPhoto")
    Observable<ResponseBody> postCollectImg(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("data/getPartInfoByCode")
    Observable<NumberBean> getPartInfoByCode(@FieldMap Map<String, String> map);

}