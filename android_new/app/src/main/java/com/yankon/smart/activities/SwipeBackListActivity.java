package com.yankon.smart.activities;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yankon.smart.R;
import com.yankon.smart.widget.SwipeBackLayout;

/**
 * Created by sunup_002 on 2015/12/22.
 */
public class SwipeBackListActivity extends ListActivity {

    private SwipeBackLayout mSwipeBackLayout;

    private boolean mOverrideExitAniamtion = true;

    private boolean mIsFinishing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = new SwipeBackLayout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        return mSwipeBackLayout.findViewById(id);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnableGesture(enable);
    }

    /**
     *  slide from left
     */
    public void setEdgeFromLeft(){
        final int edgeFlag = SwipeBackLayout.EDGE_LEFT;
        mSwipeBackLayout.setEdgeTrackingEnabled(edgeFlag);
    }

    /**
     * Override Exit Animation
     *
     * @param override
     */
    public void setOverrideExitAniamtion(boolean override) {
        mOverrideExitAniamtion = override;
    }

    /**
     * Scroll out contentView and finish the activity
     */
    public void scrollToFinishActivity() {
        mSwipeBackLayout.scrollToFinishActivity();
    }

    @Override
    public void finish() {
        if (mOverrideExitAniamtion && !mIsFinishing) {
            //scrollToFinishActivity();
            mIsFinishing = true;
            return;
        }
        mIsFinishing = false;
        super.finish();
    }
}
