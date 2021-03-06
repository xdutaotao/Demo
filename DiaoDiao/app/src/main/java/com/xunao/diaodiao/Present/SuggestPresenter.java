package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.EvaluateReq;
import com.xunao.diaodiao.Bean.SkillRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SuggestView;

import javax.inject.Inject;

/**
 * Created by
 */
public class SuggestPresenter extends BasePresenter<SuggestView> {
    @Inject
    LoginModel model;

    @Inject
    SuggestPresenter() {
    }

    public void submitSuggest(Context context, String content){
        mCompositeSubscription.add(model.submitSuggest(content)
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

    //评价
    public void toEvaluate(Context context, EvaluateReq content){
        mCompositeSubscription.add(model.toEvaluate(content)
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

    public void getTypeInfo(Context context){
        mCompositeSubscription.add(model.getTypeInfo()
                .subscribe(new RxSubUtils<TypeInfoRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(TypeInfoRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    //评论
    public void goodSkills(Context context){
        mCompositeSubscription.add(model.goodSkills(1)
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
