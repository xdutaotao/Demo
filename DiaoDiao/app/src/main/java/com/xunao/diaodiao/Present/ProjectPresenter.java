package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ProjectView;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class ProjectPresenter extends BasePresenter<ProjectView> {
    @Inject
    LoginModel model;

    @Inject
    ProjectPresenter() {
    }


    public void getMyWork(Context context){
        mCompositeSubscription.add(model.getMyWork()
                .subscribe(new RxSubUtils<ProjectRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ProjectRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
