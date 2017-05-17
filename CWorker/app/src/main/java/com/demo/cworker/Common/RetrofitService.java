package com.demo.cworker.Common;


import com.demo.cworker.Bean.BaseBean;
import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.Bean.ResponseBean;
import com.demo.cworker.Model.UserInfo;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
}