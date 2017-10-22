package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.PDFModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.PDFView;

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
                        super._onError(msg);
                        getView().onFailure();
                    }
                }));
    }
}
