package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.DocView;

import java.util.List;

import javax.inject.Inject;

/**
 * Description:
 * Created by guzhenfu on 2017/9/14.
 */

public class DocPresenter extends BasePresenter<DocView> {

    @Inject
    LoginModel model;

    @Inject
    DocPresenter() {
    }

    public void getDataBase(Context context){
        mCompositeSubscription.add(model.getDataBase()
                .subscribe(new RxSubUtils<List<DocRes>>(mCompositeSubscription, context) {
                    @Override
                    protected void _onNext(List<DocRes> token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
