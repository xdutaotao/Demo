package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SignDetailView;
import com.xunao.diaodiao.View.SkillProjProgressView;

import javax.inject.Inject;

/**
 * Created by
 */
public class SkillProjProgressPresenter extends BasePresenter<SkillProjProgressView> {
    @Inject
    LoginModel model;

    @Inject
    SkillProjProgressPresenter() {
    }

    //列表
    public void myAcceptProjectWorkList(Context context, int prjID, int worksID, int who){
        mCompositeSubscription.add(model.myAcceptProjectWorkList(prjID, worksID, who)
                .subscribe(new RxSubUtils<SkillProjProgPhotoRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SkillProjProgPhotoRes token) {
                        getView().getList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myAcceptProjectWorkSub(Context context, GetMoneyReq req){
        mCompositeSubscription.add(model.myAcceptProjectWorkSub(req)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myProjectWorkPass(Context context, GetMoneyReq req){
        mCompositeSubscription.add(model.myProjectWorkPass(req)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getPass(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

}
