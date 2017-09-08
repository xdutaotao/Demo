package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Bean.RateDetailRes;
import com.xunao.diaodiao.Model.FeedBackDetailModel;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.FeedBackDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class FeedBackDetailPresenter extends BasePresenter<FeedBackDetailView> {
    @Inject
    LoginModel model;

    @Inject
    FeedBackDetailPresenter() {
    }

    /**
     * 查看评论
     * @param context
     * @param type
     */
    public void getRatingDetail(Context context, int type){
        mCompositeSubscription.add(model.getRatingDetail(type)
                .subscribe(new RxSubUtils<RateDetailRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(RateDetailRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }
}
