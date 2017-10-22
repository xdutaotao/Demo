package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Model.DocDetailModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.DocDetailView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by
 */
public class DocDetailPresenter extends BasePresenter<DocDetailView> {
    @Inject
    LoginModel model;

    @Inject
    DocDetailPresenter() {
    }

    public void database(Context context, int id, int page){
        mCompositeSubscription.add(model.database(id, page)
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
