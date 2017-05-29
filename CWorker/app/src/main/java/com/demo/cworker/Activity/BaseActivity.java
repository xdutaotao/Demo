package com.demo.cworker.Activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.cworker.Utils.Dagger.Component.ActivityComponent;
import com.demo.cworker.Utils.Dagger.Component.ActivityComponentFactory;
import com.gzfgeh.swipeback.SwipeBackActivity;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/15 11:36.
 */

public class BaseActivity extends SwipeBackActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponentFactory.create(this);
        }
        return activityComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showToolbarBack(Toolbar toolBar, TextView title, String txt){
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(txt);
    }

    /**
     * 手动控制键盘隐藏
     * @param view
     * @param mContext
     */
    public void closeSoftKeyboard(View view, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

}
