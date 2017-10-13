package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.UpdateInfo;
import com.xunao.diaodiao.Model.HomeModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.HomeView;

import java.io.File;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:45.
 */

public class HomePresenter extends BasePresenter<HomeView> {
    @Inject
    HomeModel model;

    @Inject
    HomePresenter() {}

    public void getFirstPage(String lat, String lng){
        mCompositeSubscription.add(model.getFirstPage(lat, lng)
                .subscribe(new RxSubUtils<HomeResponseBean>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(HomeResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "请求失败";
                        ToastUtil.show(s);
                        getView().onFailure();
                    }
                }));
    }

    public void getFirstPage(Context context, String lat, String lng){
        mCompositeSubscription.add(model.getFirstPage(lat, lng)
                .subscribe(new RxSubUtils<HomeResponseBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(HomeResponseBean token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if (!TextUtils.equals(s, "网络错误"))
                            s = "请求失败";
                        ToastUtil.show(s);
                        getView().onFailure();
                    }
                }));
    }



    /**
     * 每次APP启动检查版本
     */
    public void getUpdateVersion(){
        mCompositeSubscription.add(model.updateApp()
                .subscribe(new RxSubUtils<UpdateInfo>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(UpdateInfo info) {
                        getView().getData(info);
                    }
                }));
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
