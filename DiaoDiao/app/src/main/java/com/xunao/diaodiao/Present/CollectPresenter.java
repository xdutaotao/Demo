package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.CollectBean;
import com.xunao.diaodiao.Bean.NumberBean;
import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.Model.CollectModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.CollectView;

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

    public void packagingForm(){
//        mCompositeSubscription.add(model.packagingForm()
//                .subscribe(new RxSubUtils<PackageBean.ResultBean>(mCompositeSubscription) {
//                    @Override
//                    protected void _onNext(PackageBean.ResultBean token) {
//                        getView().getData(token);
//                    }
//                }));
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
                        if (TextUtils.equals(msg, "网络错误"))
                            ToastUtil.show(msg);
                        getView().onFailure();
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
                    protected void _onError(String msg) {
                        model.recodeMark(bean, false);
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "采集失败";
                        ToastUtil.show(msg);
                        getView().postTxtError();
                    }
                }));
    }
}
