package com.yankon.smart.activities;

import android.app.FragmentManager;
import android.content.ContentValues;
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
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yankon.smart.R;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.SyncUITask;
import com.yankon.smart.BaseListActivity;

import java.util.HashMap;

/**
 * Created by guzhenfu on 2015/8/21.
 */
public class ScheduleActivity extends BaseListActivity {
    private static final int MENU_DELETE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initTitleView();
        mAdapter = new ScheduleAdapter(this);
        initListView();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ScheduleActivity.this, AddScheduleActivity.class);
                intent.putExtra(AddScheduleActivity.EXTRA_SCHEDULE_ID, (int) id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initTitleView() {
        super.initTitleView();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_schedule));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add:
                startActivity(new Intent(this, AddScheduleActivity.class));
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, YanKonProvider.URI_SCHEDULE, null, "deleted=0", null, "created_time asc");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case MENU_DELETE: {
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                DataHelper.deleteScheduleById(cid);
            }
            break;
        }
        super.onItemClick(adapterView, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        cursor = (Cursor) mAdapter.getItem(position);
        String title = cursor.getString(cursor.getColumnIndex("name"));
//        contextMenuData.clear();
        arrayIndex = 0;
        cursorDataPos = position;
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("value", getString(R.string.menu_delete));
//        contextMenuData.add(hashMap);
        array.put(arrayIndex++, getString(R.string.menu_delete));
        showListViewDialog(title, array);
        return true;
    }

    class ScheduleAdapter extends CursorAdapter {
        public ScheduleAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.light_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            TextView tv = (TextView) view.findViewById(R.id.title);
            tv.setText(name);
            View viewIcon = view.findViewById(R.id.view_icon);
            viewIcon.setVisibility(View.GONE);

            View icon = view.findViewById(R.id.light_icon);
            icon.setBackgroundResource(R.drawable.menu_schdules);
            final ToggleButton light_switch = (ToggleButton) view.findViewById(R.id.toggle_btn);
            View tvIcon = view.findViewById(R.id.light_info);
            tvIcon.setVisibility(View.GONE);
            final boolean state = cursor.getInt(cursor.getColumnIndex("enabled")) > 0;
            final int schedule_id = cursor.getInt(cursor.getColumnIndex("_id"));
            light_switch.setChecked(state);
            light_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    light_switch.setChecked(!state);
                    ContentValues values = new ContentValues();
                    values.put("enabled", !state);
                    values.put("synced", false);
                    getContentResolver().update(YanKonProvider.URI_SCHEDULE, values, "_id=" + schedule_id, null);
                    new SyncTask(getFragmentManager(), getString(R.string.saving)).execute();
                }
            });
        }
    }

    class SyncTask extends SyncUITask {
        SyncTask(FragmentManager fm, String msg) {
            super(fm, msg);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!checkIfSyncSucc()) {
                Toast.makeText(ScheduleActivity.this, R.string.sync_schedule_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

}
