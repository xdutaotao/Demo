package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.WeiBaoProgModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.View.WeiBaoProgView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class WeiBaoProgPresenter extends BasePresenter<WeiBaoProgView> {
    @Inject
    LoginModel model;

    @Inject
    WeiBaoProgPresenter() {
    }

    //列表
    public void myPublishMaintenanceWork(Context context, int prjID){
        mCompositeSubscription.add(model.myPublishMaintenanceWork(prjID)
                .subscribe(new RxSubUtils<MyPublishOddWorkRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(MyPublishOddWorkRes token) {
                        getView().getList(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    public void myPublishMaintenanceSuccess(Context context, int page){
        mCompositeSubscription.add(model.myPublishMaintenanceSuccess(page)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().passData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

}
