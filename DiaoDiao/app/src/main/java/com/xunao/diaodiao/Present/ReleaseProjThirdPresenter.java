package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseProjThirdModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseProjThirdView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseProjThirdPresenter extends BasePresenter<ReleaseProjThirdView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseProjThirdPresenter() {
    }


    public void publishProject(Context context, ReleaseProjReq address, boolean jianli){
        mCompositeSubscription.add(model.publishProject(address, jianli)
                .subscribe(new RxSubUtils<ReleaseProjRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ReleaseProjRes token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

    /**
     *  url 转 base64
     * @param context
     * @param address
     */
    public void countSupervisorExpenses(Context context, String address){
        mCompositeSubscription.add(model.countSupervisorExpenses(address)
                .subscribe(new RxSubUtils<GetPercentRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(GetPercentRes token) {
                        getView().getBase64List(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }

    /**
     * 更新项目
     * @param context
     * @param
     */
    public void updateProject(Context context, ReleaseProjReq req){
        mCompositeSubscription.add(model.updateProject(req)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().updateProject(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        getView().onFailure();
                    }
                }));
    }


}
