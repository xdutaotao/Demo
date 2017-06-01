package com.yankon.smart.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.LightGroup;
import com.yankon.smart.model.StatusInfo;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Evan on 14/12/8.
 */
public class AddScenesActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener {

    public static final String EXTRA_SCENE_ID = "scene_id";
    public static final String EXTRA_SCENE_NAME = "scene_name";
    static final int REQUEST_SETTINGS = 0x1000;

    EditText mSceneNameEdit;
    ExpandableListView mList;
    int scene_id;
    HashSet<String> orgSelectedSet = new HashSet<>();
    HashSet<String> selectedSet = new HashSet<>();
    HashMap<String, StatusInfo> infoMap = new HashMap<>();
    StatusInfo currStatusInfo;
    SceneAdapter mAdapter;

    ArrayList<Light> mLights = new ArrayList<>();
    ArrayList<LightGroup> mGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenes);
        initActivityUI();
        mSceneNameEdit = (EditText) findViewById(R.id.scene_name);
        mList = (ExpandableListView) findViewById(R.id.scene_list);
        findViewById(R.id.scene_cancel).setOnClickListener(this);
        findViewById(R.id.scene_ok).setOnClickListener(this);

        scene_id = getIntent().getIntExtra(EXTRA_SCENE_ID, -1);
        mSceneNameEdit.setText(getIntent().getStringExtra(EXTRA_SCENE_NAME));
        loadContents();
        mAdapter = new SceneAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnChildClickListener(this);
        mList.expandGroup(0);
        mList.expandGroup(1);
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

    void addGroup(LightGroup group) {
    }

    void loadContents() {
        Cursor c = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "deleted=0", null, "connected desc, ifnull(length(remote_pwd), 0) desc, owned_time asc");
        while (c.moveToNext()) {
            Light l = new Light();
            l.name = c.getString(c.getColumnIndex("name"));
            l.id = c.getInt(c.getColumnIndex("_id"));
            l.model = c.getString(c.getColumnIndex("model"));
            l.connected = c.getInt((c.getColumnIndex("connected"))) > 0;
            l.remotePassword = c.getString(c.getColumnIndex("remote_pwd"));
            mLights.add(l);
        }
        c.close();
        c = getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, null, "deleted=0", null, null);
        while (c.moveToNext()) {
            LightGroup group = new LightGroup();
            group.name = c.getString(c.getColumnIndex("name"));
            group.id = c.getInt(c.getColumnIndex("_id"));
            group.num = c.getInt(c.getColumnIndex("num"));
            mGroups.add(group);
            addGroup(group);
        }
        c.close();
        if (scene_id >= 0) {
            c = getContentResolver().query(YanKonProvider.URI_SCENES_DETAIL, null, "scene_id=" + scene_id, null, null);
            while (c.moveToNext()) {
                StatusInfo info = new StatusInfo();
                info.brightness = c.getInt(c.getColumnIndex("brightness"));
                info.color = c.getInt(c.getColumnIndex("color"));
                info.state = c.getInt(c.getColumnIndex("state")) != 0;
                info.CT = c.getInt(c.getColumnIndex("CT"));
                info.mode = c.getInt(c.getColumnIndex("mode"));
                int light_id = c.getInt(c.getColumnIndex("light_id"));
                if (light_id >= 0) {
                    info.id = "l" + light_id;
                } else {
                    int group_id = c.getInt(c.getColumnIndex("group_id"));
                    info.id = "g" + group_id;
                }
                infoMap.put(info.id, info);
                orgSelectedSet.add(info.id);
            }
            c.close();
            selectedSet.addAll(orgSelectedSet);
            setTitle(R.string.edit_scenes);
        }else{
            setTitle(R.string.add_scenes);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scene_cancel:
                finish();
                break;
            case R.id.scene_ok:
                save();
                break;
            case R.id.light_checkbox: {
                String key = (String) v.getTag();
                if (selectedSet.contains(key)) {
                    selectedSet.remove(key);
                } else {
                    StatusInfo info = infoMap.get(key);
                    if (info == null) {
                        info = new StatusInfo();
                        info.id = key;
                        infoMap.put(info.id, info);
                    }
                    selectedSet.add(key);
                }
                mList.invalidateViews();
            }
            break;
        }
    }

    private void save() {
        String gName = mSceneNameEdit.getText().toString().trim();
        if (TextUtils.isEmpty(gName)) {
            Toast.makeText(this, getString(R.string.empty_scene_name), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", gName);
        values.put("synced",false);
        if (scene_id < 0) {
            values.put("objectID", UUID.randomUUID().toString());
            values.put("created_time", System.currentTimeMillis());
            Uri uri = cr.insert(YanKonProvider.URI_SCENES, values);
            scene_id = Integer.parseInt(uri.getLastPathSegment());
        } else {
            cr.update(YanKonProvider.URI_SCENES, values, "_id=" + scene_id, null);
        }
        String[] selArr = selectedSet.toArray(new String[selectedSet.size()]);
        for (int i = 0; i < selArr.length; i++) {
            String data = selArr[i];
            if (orgSelectedSet.contains(data)) {
                orgSelectedSet.remove(data);
                selectedSet.remove(data);
                loadInfoToContentValues(data, false);
            }
        }
        for (String data : orgSelectedSet) {
            if (data.length() <= 1)
                continue;
            String num = data.substring(1);
            int id = Integer.parseInt(num);
            if (data.charAt(0) == 'l') {
                cr.delete(YanKonProvider.URI_SCENES_DETAIL, "scene_id=" + scene_id + " AND light_id=" + id, null);
            } else {
                cr.delete(YanKonProvider.URI_SCENES_DETAIL, "scene_id=" + scene_id + " AND group_id=" + id, null);
            }
        }
        for (String data : selectedSet) {
            if (data.length() <= 1)
                continue;
            loadInfoToContentValues(data, true);
        }
        finish();
    }

    void loadInfoToContentValues(String key, boolean create) {
        String num = key.substring(1);
        int id = Integer.parseInt(num);
        ContentValues values = new ContentValues();
        int light_id = -1, group_id = -1;

        if (key.charAt(0) == 'l') {
            light_id = id;
        } else {
            group_id = id;
        }

        StatusInfo info = infoMap.get(key);
        if (info == null) {
            info = new StatusInfo();
            info.id = key;
            infoMap.put(info.id, info);
        }
        values.put("state", info.state);
        values.put("color", info.color);
        values.put("brightness", info.brightness);
        values.put("CT", info.CT);
        values.put("mode",info.mode);

        if (create) {
            values.put("scene_id", scene_id);
            values.put("light_id", light_id);
            values.put("group_id", group_id);
            getContentResolver().insert(YanKonProvider.URI_SCENES_DETAIL, values);
        } else {
            getContentResolver().update(YanKonProvider.URI_SCENES_DETAIL, values, "scene_id=(?) AND light_id=(?) AND group_id=(?)",
                    new String[]{String.valueOf(scene_id), String.valueOf(light_id), String.valueOf(group_id)});
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SETTINGS && currStatusInfo != null) {
                currStatusInfo.state = data.getBooleanExtra("state", true);
                currStatusInfo.color = data.getIntExtra("color", Constants.DEFAULT_COLOR);
                currStatusInfo.brightness = data.getIntExtra("brightness", Constants.DEFAULT_BRIGHTNESS);
                currStatusInfo.CT = data.getIntExtra("CT", Constants.DEFAULT_CT);
                currStatusInfo.mode = data.getIntExtra("mode", Constants.DEFAULT_MODE);
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String key;
        Intent intent = new Intent(this, LightInfoActivity.class);
        if (groupPosition == 0) {
            Light l = mLights.get(childPosition);
            intent.putExtra(LightInfoActivity.EXTRA_LIGHT_ID,l.id);
            key = "l" + l.id;
        } else {
            LightGroup g = mGroups.get(childPosition);
            intent.putExtra(LightInfoActivity.EXTRA_GROUP_ID,g.id);
            key = "g" + g.id;
        }
        if (selectedSet.contains(key)) {
            StatusInfo info = infoMap.get(key);
            if (info == null) {
                info = new StatusInfo();
                info.id = key;
                infoMap.put(info.id, info);
            }
            currStatusInfo = info;
            intent.putExtra("state", info.state);
            intent.putExtra("color", info.color);
            intent.putExtra("brightness", info.brightness);
            intent.putExtra("CT", info.CT);
            intent.putExtra("mode", info.mode);
            intent.putExtra(LightInfoActivity.EXTRA_RETURN, true);
            startActivityForResult(intent, REQUEST_SETTINGS);
        } else {
            Toast.makeText(this, getString(R.string.select_first), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    class SceneAdapter extends BaseExpandableListAdapter {

        public SceneAdapter() {
            super();
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) {
                return mLights.size();
            }
            return mGroups.size();
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
            view.setText(groupPosition == 0 ? R.string.Lights : R.string.light_groups);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.checkable_item, parent, false);
            }
            TextView tv;
            String name, line2;
            TextView sceneName = (TextView) view.findViewById(R.id.light_scene_name);
            String key;
            if (groupPosition == 0) {
                Light l = mLights.get(childPosition);
                name = l.name;
                sceneName.setText(name);
                boolean remoteEnable = l.remotePassword != null && l.remotePassword.length() == 4;
                if (l.connected) {
                    name = getString(R.string.light_name_local_1);
                } else if (remoteEnable) {
                    name = getString(R.string.light_name_remote_enabled_1);
                } else {
                    name = getString(R.string.light_name_remote_1);
                }
                //icon.setBackgroundResource(R.drawable.light_on);
                line2 = getString(R.string.light_model_format, l.model);
                key = "l" + l.id;
                tv = (TextView) view.findViewById(android.R.id.text2);
                tv.setText(name);
            } else {
                LightGroup g = mGroups.get(childPosition);
                name = g.name;
                sceneName.setText(name);
                //icon.setBackgroundResource(R.drawable.menu_groups);
                line2 = getString(R.string.group_num_format, g.num);
                key = "g" + g.id;
                tv = (TextView) view.findViewById(android.R.id.text2);
                tv.setText(line2);
            }
            //tv.setText(name);
            //tv.setVisibility(View.GONE);

            CheckBox cb = (CheckBox) view.findViewById(R.id.light_checkbox);
            cb.setOnClickListener(AddScenesActivity.this);
            cb.setChecked(selectedSet.contains(key));
            cb.setFocusable(false);
            cb.setFocusableInTouchMode(false);
            cb.setTag(key);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
