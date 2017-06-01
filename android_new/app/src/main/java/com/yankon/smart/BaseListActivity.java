package com.yankon.smart;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.yankon.smart.R;
import com.yankon.smart.activities.LightInfoActivity;
import com.yankon.smart.activities.SwipeBackListActivity;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.widget.DialogArrayItemAdapter;
import com.yankon.smart.widget.DialogItemsAdapter;
import com.yankon.smart.widget.Effectstype;
import com.yankon.smart.widget.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guzhenfu on 2015/8/20.
 */
public class BaseListActivity extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener {
    public NiftyDialogBuilder dialogBuilder;
    public CursorAdapter mAdapter;
    public List<Map<String, Object>> contextMenuData = new ArrayList<>();
    public int cursorDataPos = -1;
    private View backLayout;
    public Cursor cursor = null;
    private int mDownX, mDownY;
    public SparseArray<String> array = new SparseArray<>();
    public int arrayIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(getClass().hashCode(), null, this);       /* Init LoaderManager */
    }

    public void initListView(){
        setListAdapter(mAdapter);
        if (mAdapter != null && cursor == null)
            cursor = mAdapter.getCursor();
        getListView().setOnItemLongClickListener(this);
    }

    public void initTitleView() {
        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);
        ImageView ivBack = (ImageView) findViewById(R.id.back);
        ivBack.setOnClickListener(this);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.control_light));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
            case R.id.back_layout:
                //backLayout.setBackgroundColor(getResources().getColor(R.color.myAccentColor));
                finish();
                break;

            case R.id.add:
                break;
        }
    }

    public void showListViewDialog(String title, List<Map<String, Object>> data){
        ListView listView= new ListView(this);
        DialogItemsAdapter adapter = new DialogItemsAdapter(this, data);
        listView.setAdapter(adapter);

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        Effectstype effect= Effectstype.Fadein;

        dialogBuilder
                .withMessage(null)
                .withTitle(title)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withEffect(effect)                                 //def gone
                .setCustomView(listView, this)         //.setCustomView(View or ResId,context)
                .setButton1Visibility(View.GONE)
                .setButton2Visibility(View.GONE)
                .show();

        listView.setOnItemClickListener(this);
    }

    public void showListViewDialog(String title, SparseArray data){
        ListView listView= new ListView(this);
//        DialogItemsAdapter adapter = new DialogItemsAdapter(this, data);
        DialogArrayItemAdapter adapter = new DialogArrayItemAdapter(this, data);
        listView.setAdapter(adapter);

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        Effectstype effect= Effectstype.Fadein;

        dialogBuilder
                .withMessage(null)
                .withTitle(title)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withEffect(effect)                                 //def gone
                .setCustomView(listView, this)         //.setCustomView(View or ResId,context)
                .setButton1Visibility(View.GONE)
                .setButton2Visibility(View.GONE)
                .show();

        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long newTime = System.currentTimeMillis();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_NORMAL;
        Global.oldTime = newTime;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Global.oldTime = System.currentTimeMillis();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_BACKGROUND;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null && cursor != null) {
            cursor.close();
            mAdapter = null;
        }

        RefWatcher refWatcher = App.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialogBuilder.dismiss();
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
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                LogUtils.i("MOVE", mDownX + "----" + event.getX() + "MOVE---------" + mDownY + "----" + event.getY());
                if (event.getX() - mDownX > Global.X_DISTANCE && Math.abs(event.getY() - mDownY) < Global.Y_DISTANCE)
                    finish();
                break;

        }
        return super.dispatchTouchEvent(event);
    }
}
