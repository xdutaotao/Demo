package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.WeiBaoDetailRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.WebViewDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.WebViewDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class WebViewDetailPresenter extends BasePresenter<WebViewDetailView> {
    @Inject
    LoginModel model;

    @Inject
    WebViewDetailPresenter() {
    }

    //取消已经发布的项目
    public void myProjectCancel(Context context, int id, int who){
        mCompositeSubscription.add(model.myProjectCancel(id, who)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().myProjectCancel(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    /**
     * 项目详情
     * @param context
     * @param req
     */
    public void getFindProjDetail(Context context, int req, int who){
        mCompositeSubscription.add(model.getFindProjDetail(req, who)
                .subscribe(new RxSubUtils<FindProjDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(FindProjDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if(!TextUtils.isEmpty(s))
                            ToastUtil.show(s);
                    }
                }));
    }


    /**
     * 维保详情
     * @param context
     * @param req
     */
    public void getFindWBDetail(Context context, int req, int who){
        mCompositeSubscription.add(model.getFindWBDetail(req, who)
                .subscribe(new RxSubUtils<WeiBaoDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(WeiBaoDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        if(!TextUtils.isEmpty(s))
                            ToastUtil.show(s);
                    }
                }));
    }


}
