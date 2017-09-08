package com.xunao.diaodiao.Present;

import android.content.Context;

import com.xunao.diaodiao.Bean.HasRateRes;
import com.xunao.diaodiao.Bean.MyRateRes;
import com.xunao.diaodiao.Model.LoginModel;
import com.xunao.diaodiao.Utils.RxSubUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.HasRatingView;
import com.xunao.diaodiao.View.MyRatingView;

import javax.inject.Inject;

/**
 * Created by
 */
public class HasRatingPresenter extends BasePresenter<HasRatingView> {
    @Inject
    LoginModel model;

    @Inject
    HasRatingPresenter() {
    }

    /**
     * 评论列表
     * @param context
     */
    public void getHasRating(Context context){
        mCompositeSubscription.add(model.getHasRating()
                .subscribe(new RxSubUtils<HasRateRes>(mCompositeSubscription,context) {
                    @Override
                    protected void _onNext(HasRateRes token) {
                        getView().getData(token);
                    }

                    @Override
                    public void _onError(String s) {
                        ToastUtil.show(s);
                    }
                }));
    }

}
