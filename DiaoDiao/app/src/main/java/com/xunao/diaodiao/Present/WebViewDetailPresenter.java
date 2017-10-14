package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
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
    public void myProjectCancel(Context context, int id){
        mCompositeSubscription.add(model.myProjectCancel(id)
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
    public void getFindProjDetail(Context context, int req){
        mCompositeSubscription.add(model.getFindProjDetail(req)
                .subscribe(new RxSubUtils<FindProjDetailRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(FindProjDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
