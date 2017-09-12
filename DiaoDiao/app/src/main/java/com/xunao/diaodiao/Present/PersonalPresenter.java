package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.PersonalRes;
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
                        ToastUtil.show(s);
                    }
                }));
    }


}
