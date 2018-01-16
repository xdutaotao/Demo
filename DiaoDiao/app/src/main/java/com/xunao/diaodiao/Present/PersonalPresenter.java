package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.PersonalView;

import javax.inject.Inject;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/23 17:24.
 */

public class PersonalPresenter extends BasePresenter<PersonalView> {
    @Inject
    LoginModel model;

    @Inject
    PersonalPresenter() {
    }

    public void changeHeadIcon(Context context, String path){
        mCompositeSubscription.add(model.changeHeadIcon(path)
                .subscribe(new RxSubUtils<HeadIconRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(HeadIconRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    public void getPersonalInfo(Context context){
        mCompositeSubscription.add(model.getPersonalInfo()
                .subscribe(new RxSubUtils<PersonalRes>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(PersonalRes token) {
                        getView().getPersonalData(token);
                    }

                    @Override
                    public void _onError(String s) {
                       getView().onFailure();
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


}
