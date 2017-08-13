package com.xunao.diaodiao.Model;

import com.xunao.diaodiao.Common.RetrofitConfig;

import okhttp3.RequestBody;

public class BaseModel {

    protected RetrofitConfig config;
//    protected JsonUtils jsonUtils;
//    protected User user;
//    protected boolean isLogin = false;

    public BaseModel() {
        config = RetrofitConfig.getInstance();
//        jsonUtils = JsonUtils.getInstance();
//        user = User.getInstance();
    }

    public RequestBody getBody(String s) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json"), s);
    }

//    /**
//     * 返回最后请求的json字符串
//     *
//     * @param params
//     * @param methodName
//     * @param serviceName
//     * @return
//     */
//    public String paramsToRequest(@NonNull String params, @NonNull String methodName, @NonNull String serviceName) {
//        RequestBaseBean<String> requestBaseBean = new RequestBaseBean<>();
//        requestBaseBean.setMethodName(methodName);
//        requestBaseBean.setServiceName(serviceName);
//        requestBaseBean.setParams(params);
//        if (isLogin) {
//            requestBaseBean.setUserid(user.getDefualtUserId());
//        } else {
//            requestBaseBean.setUserid(user.getUserId());
//        }
//        RequestBean<RequestBaseBean<String>> requestBean = new RequestBean<>();
//        requestBean.setJson(requestBaseBean);
//        String s = jsonUtils.RequestToJson(requestBean);
//        return s;
//    }
}