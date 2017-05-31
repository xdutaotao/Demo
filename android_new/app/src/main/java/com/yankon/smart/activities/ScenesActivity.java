package com.yankon.smart.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yankon.smart.R;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.BaseListActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by guzhenfu on 2015/8/19.
 */
public class ScenesActivity extends BaseListActivity {
    private static final int MENU_EDIT = 0;
    private static final int MENU_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initTitleView();
        mAdapter = new ScenesAdapter(this);
        initListView();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                cursor = (Cursor) mAdapter.getItem(position);
                int sid = cursor.getInt(cursor.getColumnIndex("_id"));
                Utils.controlScene(ScenesActivity.this, sid, false, true);
            }
        });
    }

    @Override
    public void initTitleView() {
        super.initTitleView();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.scene));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add:
                startActivity(new Intent(this, AddScenesActivity.class));
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, YanKonProvider.URI_SCENES, null, "deleted=0", null, "created_time desc");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case MENU_EDIT: {
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Intent intent = new Intent(this, AddScenesActivity.class);
                intent.putExtra(AddScenesActivity.EXTRA_SCENE_NAME, name);
                intent.putExtra(AddScenesActivity.EXTRA_SCENE_ID, cid);
                startActivity(intent);
            }
            break;
            case MENU_DELETE: {
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                DataHelper.deleteSceneById(cid);
            }
            break;
        }
        super.onItemClick(adapterView, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        cursor = (Cursor) mAdapter.getItem(position);
        String title = cursor.getString(cursor.getColumnIndex("name"));
        contextMenuData.clear();
        cursorDataPos = position;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("value", getString(R.string.scene_edit));
        contextMenuData.add(hashMap);
        HashMap<String, Object> hashMap5 = new HashMap<>();
        hashMap5.put("value", getString(R.string.menu_delete));
        contextMenuData.add(hashMap5);
        showListViewDialog(title, contextMenuData);
        return true;
    }

    class ScenesAdapter extends CursorAdapter {
        DateFormat dateFormat;

        public ScenesAdapter(Context context) {
            super(context, null, 0);
            dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.scene_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            TextView tvSceneName = (TextView) view.findViewById(R.id.scene_name_tv);
            tvSceneName.setText(name);

            TextView tv = (TextView) view.findViewById(android.R.id.text2);
            long last_used_time = cursor.getLong(cursor.getColumnIndex("last_used_time"));
            if (last_used_time == 0) {
                tv.setText(context.getString(R.string.never_used));
            } else {
                Date date = new Date(last_used_time);
                tv.setText(getString(R.string.scene_last_used, dateFormat.format(date)));
            }
            final int sid = cursor.getInt(cursor.getColumnIndex("_id"));
            View icon = view.findViewById(R.id.light_icon);
            icon.setBackgroundResource(R.drawable.scene_icon);
            //Button applyBtn = (Button) view.findViewById(R.id.light_apply);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.controlScene(ScenesActivity.this, sid, v.getId() == R.id.light_close, true);
                }
            };
            //applyBtn.setOnClickListener(listener);
            Button closeBtn = (Button) view.findViewById(R.id.light_close);
            closeBtn.setOnClickListener(listener);
        }
    }

}
