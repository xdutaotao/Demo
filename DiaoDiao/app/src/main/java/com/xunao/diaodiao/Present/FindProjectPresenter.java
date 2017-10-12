package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
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

    public void getProjectList(Context context, FindProjReq req, int type){
        mCompositeSubscription.add(model.getFindProjectList(req, type)
                .subscribe(new RxSubUtils<FindProjectRes>(mCompositeSubscription, context) {
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

    //详情
    public void projectDetail(Context context, int id, int type){
        mCompositeSubscription.add(model.projectDetail(id, type)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getUrl(token);
                    }

                    @Override
                    public void _onError(String s) {
                        getView().onFailure();
                    }
                }));
    }

    //项目类型
    public void getTypeInfo(){
        mCompositeSubscription.add(model.getTypeInfo()
                .subscribe(new RxSubUtils<TypeInfoRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(TypeInfoRes token) {
                        getView().getProjType(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
