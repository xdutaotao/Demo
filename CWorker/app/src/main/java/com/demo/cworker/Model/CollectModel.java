package com.demo.cworker.Model;

import android.text.TextUtils;

import com.demo.cworker.Bean.BaseResponseBean;
import com.demo.cworker.Bean.CollectBean;
import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

/**
 * Created by
 */
@Singleton
public class CollectModel extends BaseModel {
    @Inject
    public CollectModel() {}

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
     * 上传采集信息
     */
    public Observable<String> postCollectTxt(CollectBean bean){
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
        map.put("documentCodes", bean.getDocumentCodes());

        Map<String, Double> doubleMap = new HashMap<>();
        doubleMap.put("packageLength", bean.getPackageLength());
        doubleMap.put("packageWidth", bean.getPackageWidth());
        doubleMap.put("packageHeight", bean.getPackageHeight());
        doubleMap.put("roughWeight", bean.getRoughWeight());
        doubleMap.put("partLength", bean.getPartLength());
        doubleMap.put("partWidth", bean.getPartWidth());
        doubleMap.put("partHeigth", bean.getPartHeigth());
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

        return config.getRetrofitService().postCollectTxt(map, doubleMap, intMap)
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

    /**
     * 上传采集信息
     */
    public Observable<String> postCollectImg(String path){
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("photo\"; filename=\""+file.getName(), body);
        map.put("token", RequestBody.create(MediaType.parse("application/json"), User.getInstance().getUserId()));

        return config.getRetrofitService().postCollectImg(map)
                .flatMap( responseBody -> {
                    try {
                        String s = responseBody.string();
                        BaseResponseBean bean = JsonUtils.getInstance().JsonToUpdateFile(s);
                        if (TextUtils.equals(bean.getMsg(), "200")){
                            return config.getRetrofitService().getUserInfo(User.getInstance().getUserId())
                                    .compose(RxUtils.handleResultNoThread());
                        }else if(TextUtils.equals(bean.getResult(), "请登录!")){
                            return Observable.error(new RxUtils.ServerException("请登录")) ;
                        }else if(TextUtils.equals(bean.getMsg(), "501")){
                            return Observable.error(new RxUtils.ServerException("上传文件太大")) ;
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


}
