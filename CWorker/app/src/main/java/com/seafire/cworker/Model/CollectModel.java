package com.seafire.cworker.Model;

import android.text.TextUtils;

import com.seafire.cworker.App;
import com.seafire.cworker.Bean.CollectBean;
import com.seafire.cworker.Bean.NumberBean;
import com.seafire.cworker.Bean.PackageBean;
import com.seafire.cworker.Bean.UploadBean;
import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Utils.JsonUtils;
import com.seafire.cworker.Utils.RxUtils;
import com.seafire.cworker.Utils.ShareUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import top.zibin.luban.Luban;

/**
 * Created by
 */
@Singleton
public class CollectModel extends BaseModel {
    @Inject
    public CollectModel() {}

    /**
     * 扫描
     * @return
     */
    public Observable<PackageBean.ResultBean> packagingForm(){
        return config.getRetrofitService().packagingForm(User.getInstance().getUserInfo().getPerson().getProject())
                .flatMap(packageBean -> {
                    if (TextUtils.equals(packageBean.getMsg(), "200")){
                        return Observable.create(new Observable.OnSubscribe<PackageBean.ResultBean>(){

                            @Override
                            public void call(Subscriber<? super PackageBean.ResultBean> subscriber) {
                                subscriber.onNext(packageBean.getResult());
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new RxUtils.ServerException(""));
                    }
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 搜索
     * @param code
     * @param project
     * @return
     */
    public Observable<NumberBean> getPartInfoByCode(String code, String project){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("project", project);
        return config.getRetrofitService().getPartInfoByCode(map)
                .flatMap(numberBean -> {
                    if (TextUtils.equals(numberBean.getMsg(), "200")){
                        return Observable.create(new Observable.OnSubscribe<NumberBean>(){

                            @Override
                            public void call(Subscriber<? super NumberBean> subscriber) {
                                subscriber.onNext(numberBean);
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new RxUtils.ServerException(numberBean.getResult()));
                    }
                }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }




    /**
     * 上传采集信息
     */
    public Observable<String> postCollectTxt(CollectBean bean, List<String> list){
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getUserId());
        map.put("partCode", bean.getPartCode());
        map.put("partName", bean.getPartName());
        map.put("packageStypeName", bean.getPackageStypeName());
        map.put("partMaterialName", bean.getPartMaterialName());
        map.put("processRecommendation", bean.getProcessRecommendation());
        map.put("remark", bean.getRemark());
        map.put("systemResource", bean.getSystemResource());
        map.put("locations", bean.getLocations());
        map.put("auditType", bean.getAuditType());
        map.put("project", bean.getProject());

        Map<String, Float> doubleMap = new HashMap<>();
        doubleMap.put("packageLength", bean.getPackageLength());
        doubleMap.put("packageWidth", bean.getPackageWidth());
        doubleMap.put("packageHeight", bean.getPackageHeight());
        doubleMap.put("roughWeight", bean.getRoughWeight());
        doubleMap.put("partLength", bean.getPartLength());
        doubleMap.put("partWidth", bean.getPartWidth());
        doubleMap.put("partHeight", bean.getPartHeigth());
        doubleMap.put("netWeight", bean.getNetWeight());
        doubleMap.put("addedLength", bean.getAddedLength());
        doubleMap.put("addedWidth", bean.getAddedWidth());
        doubleMap.put("addedHeight", bean.getAddedHeight());
        doubleMap.put("singlePackageLength", bean.getSinglePackageLength());
        doubleMap.put("singlePackageWidth", bean.getSinglePackageWidth());
        doubleMap.put("singlePackageHeight", bean.getSinglePackageHeight());
        doubleMap.put("singlePackageWeight", bean.getSinglePackageWeight());

        Map<String, Integer> intMap = new HashMap<>();
        intMap.put("packageModelCount", bean.getPackageModelCount());
        intMap.put("isHistory", bean.getIsHistory());


//        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        requestBodyMap.put("token", RequestBody.create(MediaType.parse("application/json"), User.getInstance().getUserId()));
//
//        for(String path: list){
//            File file = new File(path);
//            RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
//            requestBodyMap.put("photo\"; filename=\""+file.getName(), body);
//        }

        return resizeFile(list)
                .flatMap(s -> {
                    map.put("documentCodes", s);
                    return config.getRetrofitService().postCollectTxt(map, doubleMap, intMap);
                })
                .flatMap( bean1 -> {
                    if (TextUtils.equals(bean1.getMsg(), "200")){
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                subscriber.onNext(bean1.getResult());
                                subscriber.onCompleted();
                            }
                        });
                    }else{
                        return Observable.error(new RxUtils.ServerException(bean1.getResult())) ;
                    }
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    private Observable<String> resizeFile(List<String> list){
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        return Observable.from(list)
                .flatMap(s -> {
                    if (s.contains("http")){
                        return Observable.create(subscriber -> {
                            subscriber.onNext(s);
                            subscriber.onCompleted();
                        });
                    }else{
                        return Luban.get(App.getContext())
                                .load(new File(s))                     //传人要压缩的图片
                                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                                .asObservable()
                                .map(file -> {
                                    RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
                                    requestBodyMap.put("photo\"; filename=\""+file.getName(), body);
                                    requestBodyMap.put("token", RequestBody.create(MediaType.parse("application/json"), User.getInstance().getUserId()));
                                    return requestBodyMap;
                                })
                                .flatMap(stringRequestBodyMap -> config.getRetrofitService().postCollectImg(stringRequestBodyMap))
                                .map(responseBody -> {
                                    try {
                                        String result = responseBody.string();
                                        UploadBean uploadBean = JsonUtils.getInstance().JsonToUploadBean(result);
                                        if (TextUtils.equals(uploadBean.getMsg(), "200")) {
                                            return uploadBean.getData();
                                        }else{
                                            return "上传图片失败";
                                        }
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    return "上传图片失败";
                                });
                    }
                })
                .toList()
                .map(s -> {
                    for(String temp: s)
                        sb.append(temp).append(",");
                    return sb.toString().substring(0, sb.toString().length()-1);
                });
    }

    /**
     * 上传采集信息
     */
    public Observable<String> postCollectImg(List<String> list){
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", RequestBody.create(MediaType.parse("application/json"), User.getInstance().getUserId()));

        for(String path: list){
            File file = new File(path);
            RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
            map.put("photo\"; filename=\""+file.getName(), body);
        }

        return config.getRetrofitService().postCollectImg(map)
                .flatMap( responseBody -> {
                    try {
                        String s = responseBody.string();
                        UploadBean bean = JsonUtils.getInstance().JsonToUploadBean(s);
                        if (TextUtils.equals(bean.getMsg(), "200")){
                            return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                                    .compose(RxUtils.handleResultNoThread());
                        }else{
                            return Observable.error(new RxUtils.ServerException("操作失败")) ;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Observable.error(new RxUtils.ServerException("操作失败")) ;
                })
                .map(userInfo -> {
                    String userInfoString = JsonUtils.getInstance().UserInfoToJson(userInfo);
                    User.getInstance().setUserInfo(userInfoString);
                    return "操作成功";
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    /**
     * 记录上次
     */
    public void recodeMark(CollectBean bean, boolean isSuccess){

        /**
         * 是否是历史上传 不是历史上传
         */
        if (bean.getIsHistory() == 0){
            if (ShareUtils.getValue(Constants.POST_COLLECT_TIME, 0) != 0){
                int time = ShareUtils.getValue(Constants.POST_COLLECT_TIME, 0);
                ++time;
                ShareUtils.putValue(Constants.POST_COLLECT_TIME, time);
            }else{
                ShareUtils.putValue(Constants.POST_COLLECT_TIME, 1);
            }
        }


        List<CollectBean> collectBeanList = new ArrayList<>();
        bean.setSuccess(isSuccess);

        if (bean.getIsHistory() == 1){
            List<CollectBean> list = JsonUtils.getInstance().JsonToCollectList(ShareUtils.getValue(Constants.COLLECT_LIST, null));
            if (list != null){
                for(CollectBean collectBean: list){
                    if (!TextUtils.equals(collectBean.getTime(), bean.getTime())){
                        collectBeanList.add(collectBean);
                    }else{
                        collectBeanList.add(bean);
                    }
                }
            }else{
                collectBeanList.add(bean);
            }

        }else{
            if (!isSuccess)
                bean.setIsHistory(1);

            if (ShareUtils.getValue(Constants.COLLECT_LIST, null) != null){
                collectBeanList.addAll(JsonUtils.getInstance().JsonToCollectList(ShareUtils.getValue(Constants.COLLECT_LIST, null)));
            }
            collectBeanList.add(bean);
        }


        String listJson = JsonUtils.getInstance().CollectListToJson(collectBeanList);
        ShareUtils.putValue(Constants.COLLECT_LIST, listJson);

    }


}
