package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.FindProjectModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.FindProjectView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class FindProjectPresenter extends BasePresenter<FindProjectView> {
    @Inject
    LoginModel model;

    @Inject
    FindProjectPresenter() {
    }

    public void getProjectList(FindProjReq req, int type){
        mCompositeSubscription.add(model.getFindProjectList(req, type)
                .subscribe(new RxSubUtils<FindProjectRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(FindProjectRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().getNoMore(s);
                    }
                }));
    }

}
