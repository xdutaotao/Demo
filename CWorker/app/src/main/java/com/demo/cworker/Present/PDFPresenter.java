package com.demo.cworker.Present;

import com.demo.cworker.Model.PDFModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.View.PDFView;

import java.io.File;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class PDFPresenter extends BasePresenter<PDFView> {
    @Inject
    PDFModel model;

    @Inject
    PDFPresenter() {
    }

    /**
     * 下载APK
     * @param url
     * @param file
     */
    public void apkFileDownload(String url, File file){
        mCompositeSubscription.add(model.apkFileDownload(url, file)
                .subscribe(new RxSubUtils<Float>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(Float aFloat) {
                        getView().getProgress(aFloat);
                    }
                }));
    }
}
