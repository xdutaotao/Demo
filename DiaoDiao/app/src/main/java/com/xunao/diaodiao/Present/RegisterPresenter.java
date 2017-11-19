package com.xunao.diaodiao.Present;

import android.content.Context;
import android.text.TextUtils;

import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.RegisterView;

import javax.inject.Inject;

/**
 * Created by
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
    @Inject
    LoginModel model;

    @Inject
    RegisterPresenter() {
    }

    public void checkPhone(Context context, String name){
        mCompositeSubscription.add(model.checkPhone(name)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(Object token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
//                        if (!TextUtils.equals(s, "网络错误"))
//                            s = "手机号已经注册";
                        if(TextUtils.isEmpty(s)){
                            s = "修改失败";
                        }
                        ToastUtil.show(s);
                    }
                }));
    }

    /**
     * 注册
     */
    public void register(Context context, String phone, String pwd, String code, int type){
        mCompositeSubscription.add(model.checkExistPhone(phone, pwd, code, type)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

    /**
     * 修改密码 检查手机号是否存在
     */
    public void forgetPwd(Context context, String phone, String pwd, String code, int type){
        mCompositeSubscription.add(model.forgetPwd(phone, pwd, code, type)
                .subscribe(new RxSubUtils<Object>(mCompositeSubscription,context) {
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



    /**
     * 选择角色
     */
    public void select(Context context, int type){
        mCompositeSubscription.add(model.select(type)
                .subscribe(new RxSubUtils<String>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(String token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }


}
