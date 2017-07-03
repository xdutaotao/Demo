package com.bz.fitness.Present;

import android.content.Context;

import com.bz.fitness.Bean.CollectBean;
import com.bz.fitness.Bean.NumberBean;
import com.bz.fitness.Bean.PackageBean;
import com.bz.fitness.Model.CollectModel;
import com.bz.fitness.Utils.RxSubUtils;
import com.bz.fitness.Utils.ToastUtil;
import com.bz.fitness.View.CollectView;

import java.util.List;

import javax.inject.Inject;

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
                        model.recodeMark(bean, true);
                        getView().getPostTxt(token);
                    }

                    @Override
                    protected void _onError() {
                        model.recodeMark(bean, false);
                        getView().onFailure();
                    }
                }));
    }
}
