package com.demo.cworker.Common;


import com.demo.cworker.Bean.BuyVipBean;
import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.Bean.BaseBean;
import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.Bean.ResponseBean;
import com.demo.cworker.Bean.UpdateVersionBean;
import com.demo.cworker.Model.UserInfo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<BaseBean<UserInfo>> getUserInfo(@FieldMap Map<String, String> map);

    @GET("user/checkMobile")
    Observable<BaseBean<String>> checkPhone(@Query("mobile") String mobile);

    @GET("user/checkEmail")
    Observable<BaseBean<String>> checkEmail(@Query("email") String email);

    @GET("user/changePassword/checkMobile")
    Observable<BaseBean<String>> checkExistPhone(@Query("mobile") String mobile);

    @GET("user/changeThePassword")
    Observable<BaseBean<String>> changePwd(@Query("mobile") String mobile, @Query("newPassword") String newPassword);

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
    Observable<BaseBean<UserInfo.PersonBean>> addVipDuration(@Field("token") String token, @FieldMap Map<String, Long> intMap);

    @FormUrlEncoded
    @POST("http://363600.cicp.net:8080")
    Observable<BaseBean<String>> changeAddress(@FieldMap Map<String, String> map);

    //上传图片
    @Multipart
    @POST("personalCenter/updateAvatar")
    Observable<ResponseBody> changeHeadIcon(@Part MultipartBody.Part photo, @Field("token") String token);


    @FormUrlEncoded
    @POST("personalCenter/edit")
    Observable<BaseBean<String>> changeSex(@Field("sex") int sex, @Field("token") String token);

    @FormUrlEncoded
    @POST("version/getVersionsByPlatform")
    Observable<BaseBean<UpdateVersionBean.DataBean>> updateVersion(@Field("platform") String platform);

    @FormUrlEncoded
    @POST("personalCenter/complaintsSuggestions")
    Observable<BaseBean<String>> submitSuggest(@FieldMap Map<String, String> map);
}