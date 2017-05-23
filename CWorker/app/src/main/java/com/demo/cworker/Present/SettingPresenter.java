package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.UpdateVersionBean;
import com.demo.cworker.Model.LoginModel;
import com.demo.cworker.Model.PDFModel;
import com.demo.cworker.Model.SettingModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.SettingView;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SettingPresenter extends BasePresenter<SettingView> {
    @Inject
    LoginModel model;

    @Inject
    PDFModel pdfModel;

    @Inject
    SettingPresenter() {
    }

    public void updateVersion(Context context){
        mCompositeSubscription.add(model.updateVersion()
                .subscribe(new RxSubUtils<List<UpdateVersionBean.DataBean>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(List<UpdateVersionBean.DataBean> token) {
                        getView().getUpdateVersion(token.get(0));
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    /**
     * 下载APK
     * @param url
     * @param file
     */
    public void apkFileDownload(String url, File file){
        mCompositeSubscription.add(pdfModel.apkFileDownload(url, file)
                .subscribe(new RxSubUtils<Float>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(Float aFloat) {
                        getView().getProgress(aFloat);
                    }
                }));
    }

    public void clearCache(Context context){
        mCompositeSubscription.add(model.clearCache()
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }
                }));
    }


}
