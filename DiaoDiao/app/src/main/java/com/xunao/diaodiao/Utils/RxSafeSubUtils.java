package com.xunao.diaodiao.Utils;

import android.content.Context;

import com.xunao.diaodiao.R;
import com.gzfgeh.iosdialog.IOSDialog;

import rx.Subscriber;
import rx.Subscription;
import rx.observers.SafeSubscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Description:
 * Created by guzhenfu on 2016/11/1 16:52.
 */

public class RxSafeSubUtils<T> extends SafeSubscriber<T> {
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private IOSDialog dialog;
    private Subscription subscription;

    public RxSafeSubUtils(Subscriber<? super T> actual) {
        super(actual);
        subscription = actual;
    }

    public RxSafeSubUtils(Subscriber<? super T> actual, CompositeSubscription compositeSubscription) {
        super(actual);
        this.mCompositeSubscription = compositeSubscription;
        subscription = actual;
    }

    public RxSafeSubUtils(Subscriber<? super T> actual, CompositeSubscription compositeSubscription, Context context) {
        this(actual, compositeSubscription);
        this.mContext = context;
        subscription = actual;
    }


    /**
     * 这个一定要有 Presenter的逻辑在这里处理
     * @param t
     */
    @Override
    public void onNext(T t) {
        ((RxSubUtils)subscription)._onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (dialog!= null){
            dialog.dismiss();
            dialog = null;
        }

        if (!NetWorkUtils.isNetworkAvailable()) {
            ((RxSubUtils)subscription)._onError("网络错误");
        }else if(e instanceof RxUtils.ServerException){
            ((RxSubUtils)subscription)._onError(((RxUtils.ServerException) e).getMsg());
        }else{
            ((RxSubUtils)subscription)._onError("");
        }
    }

    @Override
    public void onCompleted() {
        if (mCompositeSubscription != null)
            mCompositeSubscription.remove(this);

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mContext != null && dialog == null) {
            dialog = new IOSDialog(mContext).builder()
                    .setLoadingView(R.color.colorPrimary);
            dialog.show();
        }
    }



}
