package com.demo.cworker.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.demo.cworker.Utils.Dagger.Component.ActivityComponent;
import com.demo.cworker.Utils.Dagger.Component.ActivityComponentFactory;

import rx.Subscription;

/**
 * Description:
 * Created by guzhenfu on 2016/11/18 13:41.
 */

public class BaseFragment extends Fragment {
    private ActivityComponent activityComponent;
    public boolean isVisible;

    protected Subscription sub;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponentFactory.create(getActivity());
        }
        return activityComponent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
        }else{
            isVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.setOnTouchListener(this);
//        sub = RxBus.getInstance().toObservable(String.class)
//                .filter(s -> TextUtils.equals(s, Constants.UPDATE_UI))
//                .compose(RxUtils.applyIOToMainThreadSchedulers())
//                .subscribe(s -> {
//                    updateData();
//                });
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        return true;
//    }

    /**
     * token过期，需要刷新数据
     */
    public void updateData(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sub != null && !sub.isUnsubscribed()){
            sub.unsubscribe();
            sub = null;
        }
    }
}
