package com.xunao.diaodiao.Model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.HomeBean;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.UpdateInfo;
import com.xunao.diaodiao.Common.ApiConstants;
import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.Utils;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;
import static com.xunao.diaodiao.Common.Constants.city;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:44.
 */
@Singleton
public class HomeModel extends BaseModel {
    @Inject
    public HomeModel() {}

    public Observable<HomeResponseBean> getFirstPage(@NonNull String lat, @NonNull String lng){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("index");
        sb.append(time+"").append(city).append(lat).append(lng)
                .append("security");

        HomeBean bean = new HomeBean();
        bean.setLat(lat);
        bean.setLng(lng);
        bean.setCity(city);
        bean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFirstPage(setBody("index",time, bean))
                .compose(RxUtils.handleResult());
    }

    /**
     * 得到要更新APK的版本信息
     * @return
     */
    public Observable<UpdateInfo> updateApp(){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("versionControl");
        sb.append(time+"").append(1)
                .append("security");

        HomeBean bean = new HomeBean();
        bean.setDevice_type(1);
        bean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().versionControl(setBody("versionControl",time, bean))
                .compose(RxUtils.handleResult());
    }

    /**
     * 下载APK文件
     * @param url
     * @return
     */
    public Observable<Float> apkFileDownload(String url, File file){
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                FileUtils.downloadApk(subscriber, url, file);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }

}
