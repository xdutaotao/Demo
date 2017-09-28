package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ProjectTypeRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Model.ReleaseSkillModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseSkillView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillPresenter extends BasePresenter<ReleaseSkillView> {
    @Inject
    LoginModel model;

    @Inject
    ReleaseSkillPresenter() {
    }

    //服务费
    public void getPercent(Context context){
        mCompositeSubscription.add(model.getPercent()
                .subscribe(new RxSubUtils<GetPercentRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(GetPercentRes token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }

    //项目类型
    public void publishOddType(){
        mCompositeSubscription.add(model.publishOddType()
                .subscribe(new RxSubUtils<ProjectTypeRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(ProjectTypeRes token) {
                        getView().getProjectType(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }


}
