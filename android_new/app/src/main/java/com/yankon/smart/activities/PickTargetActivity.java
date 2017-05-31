package com.yankon.smart.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.LightGroup;
import com.yankon.smart.model.Scene;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.widget.CardItemViewHolder;
import com.yankon.smart.widget.LightGroupItemHolder;
import com.yankon.smart.widget.LightItemViewHolder;

import java.util.ArrayList;


public class PickTargetActivity extends BaseActivity implements ExpandableListView.OnChildClickListener {

    ExpandableListView mList;
    ArrayList<Light> mLights = new ArrayList<>();
    ArrayList<LightGroup> mLightGroups = new ArrayList<>();
    ArrayList<Scene> mScenes = new ArrayList<>();
    TargetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_target);
        initAcitivityUI();

        mList = (ExpandableListView) findViewById(R.id.target_list);
        loadContents();
        mAdapter = new TargetAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnChildClickListener(this);
    }

    void loadContents() {
        Cursor c = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "deleted=0", null, "connected desc, ifnull(length(remote_pwd), 0) desc, owned_time asc");
        while (c.moveToNext()) {
            Light l = new Light();
            l.name = c.getString(c.getColumnIndex("name"));
            l.id = c.getInt(c.getColumnIndex("_id"));
            l.model = c.getString(c.getColumnIndex("model"));
            l.mac = c.getString(c.getColumnIndex("MAC"));
            mLights.add(l);
        }
        c.close();
        c = getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, null, "deleted=0", null, null);
        while (c.moveToNext()) {
            LightGroup group = new LightGroup();
            group.name = c.getString(c.getColumnIndex("name"));
            group.id = c.getInt(c.getColumnIndex("_id"));
            group.num = c.getInt(c.getColumnIndex("num"));
            group.objectID = c.getString(c.getColumnIndex("objectID"));
            mLightGroups.add(group);
        }
        c.close();
        c = getContentResolver().query(YanKonProvider.URI_SCENES, null, "deleted=0", null, null);
        while (c.moveToNext()) {
            Scene scene = new Scene();
            scene.name = c.getString(c.getColumnIndex("name"));
            scene.id = c.getInt(c.getColumnIndex("_id"));
            scene.objectID = c.getString(c.getColumnIndex("objectID"));
            mScenes.add(scene);
        }
        c.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent();
        intent.putExtra("type", groupPosition);
        switch (groupPosition) {
            case 0:
                intent.putExtra("id", mLights.get(childPosition).mac);
                break;
            case 1:
                intent.putExtra("id", mLightGroups.get(childPosition).objectID);
                break;
            case 2:
                intent.putExtra("id", mScenes.get(childPosition).objectID);
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    class TargetAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return 3;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case 0:
                    return mLights.size();
                case 1:
                    return mLightGroups.size();
                case 2:
                    return mScenes.size();
            }
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            if (view == null) {
                view = (TextView) getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }
            switch (groupPosition) {
                case 0:
                    view.setText(R.string.title_lights);
                    break;
                case 1:
                    view.setText(R.string.light_groups);
                    break;
                case 2:
                    view.setText(R.string.scene);
                    break;
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(PickTargetActivity.this).inflate(LightGroupItemHolder.layout_id, parent, false);
                LightGroupItemHolder holder = new LightGroupItemHolder(view);
                view.setTag(holder);
            }
            LightGroupItemHolder holder = (LightGroupItemHolder) view.getTag();
            String name = null, line2 = null;
            switch (groupPosition) {
                case 0: {
                    Light l = mLights.get(childPosition);
                    name = l.name;
                    holder.icon.setBackgroundResource(R.drawable.control_light);
                    line2 = getString(R.string.light_model_format, l.model);
                }
                break;
                case 1: {
                    LightGroup g = mLightGroups.get(childPosition);
                    name = g.name;
                    holder.icon.setBackgroundResource(R.drawable.menu_groups);
                    line2 = getString(R.string.group_num_format, g.num);
                }
                break;
                case 2: {
                    Scene s = mScenes.get(childPosition);
                    name = s.name;
                    holder.icon.setBackgroundResource(R.drawable.scene_icon);
                }
                break;
            }
            holder.textView1.setText(name);
            holder.textView2.setText(line2);
            holder.switchButton.setVisibility(View.GONE);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
