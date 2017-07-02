package com.seafire.cworker.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.seafire.cworker.Utils.Dagger.Component.ActivityComponent;
import com.seafire.cworker.Utils.Dagger.Component.ActivityComponentFactory;
import com.seafire.cworker.Utils.RxBus;
import com.seafire.cworker.Utils.RxUtils;

import rx.Subscription;

import static com.seafire.cworker.Common.Constants.LOGIN_AGAIN;

/**
 * Description:
 * Created by guzhenfu on 2016/11/18 13:41.
 */

public class BaseFragment extends Fragment implements View.OnTouchListener {
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
        view.setOnTouchListener(this);
        sub = RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, LOGIN_AGAIN))
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(s -> {
                    updateData();
                });
    }

    /**
     * 触摸会影响
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    /**
     * 手动控制键盘隐藏
     * @param mEditText
     * @param mContext
     */
    public void closeSoftKeyboard(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 手动控制键盘弹出
     * @param view
     * @param mContext
     */
    public void showSoftKeyboard(View view, Context mContext) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

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
