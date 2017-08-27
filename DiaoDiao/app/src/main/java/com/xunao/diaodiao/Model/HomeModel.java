package com.xunao.diaodiao.Model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xunao.diaodiao.Bean.HomeBean;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 16:44.
 */
@Singleton
public class HomeModel extends BaseModel {
    @Inject
    public HomeModel() {}

    public Observable<HomeResponseBean> getFirstPage(@NonNull String lat, @NonNull String lng){
        long time = System.currentTimeMillis()/1000;
        StringBuilder sb = new StringBuilder("index");
        sb.append(time+"").append(lat).append(lng)
                .append("security");

        HomeBean bean = new HomeBean();
        bean.setLat(lat);
        bean.setLng(lng);
        bean.setVerify(Utils.getMD5(sb.toString()));

        return config.getRetrofitService().getFirstPage(setBody("index",time, bean))
                .compose(RxUtils.handleResult());
    }


    public Observable<String> checkToken(){
        return config.getRetrofitService().checkToken(User.getInstance().getUserId())
                .map(baseResponseBean -> {
                    if (TextUtils.equals(baseResponseBean.getMsg(), "200")){
                        return "ok";
                    }else{
                        User.getInstance().clearUser();
                        return LOGIN_AGAIN;
                    }
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

}
