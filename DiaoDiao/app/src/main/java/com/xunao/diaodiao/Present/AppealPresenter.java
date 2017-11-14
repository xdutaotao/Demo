package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Bean.SkillRes;
import com.xunao.diaodiao.Model.AppealModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AppealView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class AppealPresenter extends BasePresenter<AppealView> {
    @Inject
    LoginModel model;

    @Inject
    AppealPresenter() {
    }

    public void myProjectWorkFail(Context context, GetMoneyReq req, int who){
        mCompositeSubscription.add(model.myProjectWorkFail(req, who)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    //申诉
    public void goodSkills(Context context){
        mCompositeSubscription.add(model.goodSkills(2)
                .subscribe(new RxSubUtils<SkillRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(SkillRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
