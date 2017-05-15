package com.demo.cworker.Common;


import com.demo.cworker.Bean.BaseBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
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

    @POST(ApiConstants.USER_REGISTER)
    Observable<BaseBean<String>> register(@Body RequestBody json);

    @POST(ApiConstants.USER_LOGIN)
    Observable<BaseBean<String>> login(@Body RequestBody json);

    @GET("user/checkMobile")
    Observable<BaseBean<String>> checkPhone(@Query("mobile") String mobile);

    @GET("user/checkEmail")
    Observable<BaseBean<String>> checkEmail(@Query("email") String email);
}