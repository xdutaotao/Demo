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
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yankon.smart.R;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.BaseListActivity;

import java.util.HashMap;

/**
 * Created by guzhenfu on 2015/8/19.
 */
public class LightGroupsActivity extends BaseListActivity {
    private static final int MENU_EDIT = 0;
    private static final int MENU_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initTitleView();
        mAdapter = new GroupsAdapter(this);
        initListView();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                cursor = (Cursor) mAdapter.getItem(position);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Intent intent = new Intent(LightGroupsActivity.this, LightInfoActivity.class);
                intent.putExtra(LightInfoActivity.EXTRA_GROUP_ID, (int) id);
                intent.putExtra(LightInfoActivity.EXTRA_NAME, name);
                startActivity(intent);
            }
        });
    }


    @Override
    public void initTitleView() {
        super.initTitleView();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.light_groups));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add:
                startActivity(new Intent(this, AddLightGroupsActivity.class));
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, YanKonProvider.URI_LIGHT_GROUPS, null, "deleted=0",
                null, "created_time asc");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case MENU_EDIT: {
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Intent intent = new Intent(this, AddLightGroupsActivity.class);
                intent.putExtra(AddLightGroupsActivity.EXTRA_GROUP_NAME, name);
                intent.putExtra(AddLightGroupsActivity.EXTRA_GROUP_ID, cid);
                startActivity(intent);
            }
            break;
            case MENU_DELETE: {
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                DataHelper.deleteLightGroupById(cid);
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
//        hashMap.put("value", getString(R.string.scene_edit));
//        contextMenuData.add(hashMap);
        array.put(arrayIndex++, getString(R.string.scene_edit));
//        HashMap<String, Object> hashMap5 = new HashMap<>();
//        hashMap5.put("value", getString(R.string.menu_delete));
//        contextMenuData.add(hashMap5);
        array.put(arrayIndex++, getString(R.string.menu_delete));
        showListViewDialog(title, array);
        return true;
    }

    class GroupsAdapter extends CursorAdapter {

        public GroupsAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.light_group_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(name);
            int num = cursor.getInt(cursor.getColumnIndex("num"));
            tv = (TextView) view.findViewById(android.R.id.text2);
            tv.setText(context.getString(R.string.group_num_format, num));
            View icon = view.findViewById(R.id.light_icon);
            icon.setBackgroundResource(R.drawable.menu_groups);
            int on_num = cursor.getInt(cursor.getColumnIndex("on_num"));
            final ToggleButton light_switch = (ToggleButton) view.findViewById(R.id.light_switch);
            final boolean state = (on_num == num);
            final int group_id = cursor.getInt(cursor.getColumnIndex("_id"));
            light_switch.setChecked(state);
            light_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = !state;
                    Command cmd = new Command(Command.CommandType.CommandTypeState, isChecked ? 1 : 0);
                    Utils.controlGroup(LightGroupsActivity.this, group_id, cmd, true);
                }
            });
        }
    }

}
