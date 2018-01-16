package com.xunao.diaodiao.Model;

import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.RxUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by
 */
@Singleton
public class PDFModel extends BaseModel {
    @Inject
    public PDFModel() {
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
        })
        .sample(1000, TimeUnit.MILLISECONDS)
        .compose(RxUtils.applyIOToMainThreadSchedulers());
    }
}
