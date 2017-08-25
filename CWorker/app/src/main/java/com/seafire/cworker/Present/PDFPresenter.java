package com.seafire.cworker.Present;

import android.text.TextUtils;

import com.seafire.cworker.Model.PDFModel;
import com.seafire.cworker.Utils.RxSubUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.PDFView;

import java.io.File;

import javax.inject.Inject;

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

                    @Override
                    protected void _onError(String msg) {
                        if (!TextUtils.equals(msg, "网络错误")){
                            msg = "下载失败, 请稍后重试";
                        }
                        ToastUtil.show(msg);
                        getView().onFailure();
                    }
                }));
    }
}
