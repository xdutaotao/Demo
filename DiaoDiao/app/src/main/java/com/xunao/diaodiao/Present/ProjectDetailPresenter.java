package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ProjectDetailModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ProjectDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ProjectDetailPresenter extends BasePresenter<ProjectDetailView> {
    @Inject
    LoginModel model;

    @Inject
    ProjectDetailPresenter() {
    }

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


    public void getFindLingGongDetail(Context context, int req){
        mCompositeSubscription.add(model.getFindLingGongDetail(req)
                .subscribe(new RxSubUtils<FindLingGongRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(FindLingGongRes token) {
                        getView().getLingGongData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }


    //申请项目
    public void postProject(Context context, int id, int type){
        mCompositeSubscription.add(model.postProject(id, type)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().postProject(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    //收藏
    public void collectWork(Context context, int id, int type){
        mCompositeSubscription.add(model.collectWork(id, type)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().collectWork(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    //取消收藏
    public void cancelCollect(Context context, int id){
        mCompositeSubscription.add(model.cancelCollect(id)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().cancleCollect(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
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

}
