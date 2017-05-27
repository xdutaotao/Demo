package com.demo.cworker.Present;

import android.content.Context;

import com.demo.cworker.Bean.CollectBean;
import com.demo.cworker.Bean.NumberBean;
import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.Model.CollectModel;
import com.demo.cworker.Utils.RxSubUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.CollectView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class CollectPresenter extends BasePresenter<CollectView> {
    @Inject
    CollectModel model;

    @Inject
    CollectPresenter() {
    }

    public void packagingForm(Context context){
        mCompositeSubscription.add(model.packagingForm()
                .subscribe(new RxSubUtils<PackageBean.ResultBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(PackageBean.ResultBean token) {
                        getView().getData(token);
                    }
                }));
    }


    /**
     * 搜索文字信息
     * @param context
     */
    public void getPartInfoByCode(Context context, String code, String project){
        mCompositeSubscription.add(model.getPartInfoByCode(code, project)
                .subscribe(new RxSubUtils<NumberBean>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(NumberBean token) {
                        getView().getSearchData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }


    /**
     * 采集文字信息
     * @param context
     */
    public void postCollectData(Context context, CollectBean bean, List<String> imgList){
        mCompositeSubscription.add(model.postCollectTxt(bean, imgList)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getPostTxt(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }
}
