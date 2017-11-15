package com.xunao.diaodiao.Present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.SkillRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.EditSkillModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.EditSkillView;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.Province;
import rx.Subscriber;

/**
 * Created by
 */
public class EditSkillPresenter extends BasePresenter<EditSkillView> {
    @Inject
    LoginModel model;

    @Inject
    EditSkillPresenter() {
    }

    public void fillSkillInfor(Context context, @NonNull FillSkillReq req){
        mCompositeSubscription.add(model.fillSkillInfor(req)
                .subscribe(new RxSubUtils<LoginResBean>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(LoginResBean token) {
                        getView().getData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        super._onError(msg);
                        getView().onFailure();
                    }
                }));
    }

    //省市区
    public void getAddressData(Context context){
        mCompositeSubscription.add(model.getAddressData()
                .subscribe(new RxSubUtils<ArrayList<Province>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(ArrayList<Province> token) {
                        getView().getAddressData(token);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtil.show(msg);
                    }
                }));
    }

    public void getTypeInfo(){
        mCompositeSubscription.add(model.getTypeInfo()
                .subscribe(new RxSubUtils<TypeInfoRes>(mCompositeSubscription) {
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

    /**
     * 审核
     */
    public void checkFinish(){
        mCompositeSubscription.add(model.checkFinish()
                .subscribe(new RxSubUtils<CheckFinishRes>(mCompositeSubscription) {
                    @Override
                    protected void _onNext(CheckFinishRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (!TextUtils.equals(msg, "网络错误"))
                            msg = "请求失败";
                        getView().onFailure();
                    }
                }));
    }

    public void goodSkills(){
        mCompositeSubscription.add(model.goodSkills(0)
                .subscribe(new RxSubUtils<SkillRes>(mCompositeSubscription) {
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
