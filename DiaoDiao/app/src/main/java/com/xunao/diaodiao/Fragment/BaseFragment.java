package com.xunao.diaodiao.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xunao.diaodiao.Utils.Dagger.Component.ActivityComponent;
import com.xunao.diaodiao.Utils.Dagger.Component.ActivityComponentFactory;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.Utils;

import rx.Subscription;

import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
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

    public void hideToolbarBack(Toolbar toolBar, TextView title, String txt){
        toolBar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title.setText(txt);
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
        Utils.lastClickTime = 0L;
        if (sub != null && !sub.isUnsubscribed()){
            sub.unsubscribe();
            sub = null;
        }
    }
}
