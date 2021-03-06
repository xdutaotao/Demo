package com.yankon.smart;

import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yankon.smart.activities.LightInfoActivity;
import com.yankon.smart.activities.SwipeBackActivity;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;

/**
 * Created by Evan on 15/3/9.
 */
public class BaseActivity extends ActionBarActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    protected CursorAdapter mAdapter = null;
    private int mDownX, mDownY;
    private VelocityTracker mVelocityTracker;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (LightInfoActivity.FLAG)
            return super.dispatchTouchEvent(event);

        mVelocityTracker.addMovement(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(100);
                float xVelocity = mVelocityTracker.getXVelocity();
                if ((event.getX() - mDownX > Global.X_DISTANCE)
                        && (Math.abs(event.getY() - mDownY) < Global.Y_DISTANCE)
                        && (Math.abs(xVelocity) > 50) )
                    finish();
                break;

        }
        return super.dispatchTouchEvent(event);
    }


    public void initActivityUI() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long newTime = System.currentTimeMillis();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_NORMAL;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_LIGHTS);
        Global.oldTime = newTime;
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Global.oldTime = System.currentTimeMillis();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_BACKGROUND;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mAdapter != null) {
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVelocityTracker.recycle();
    }

    public void showToolbarBack(Toolbar toolBar, TextView title, String txt){
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(txt);
    }
}
