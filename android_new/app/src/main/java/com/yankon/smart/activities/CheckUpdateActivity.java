package com.yankon.smart.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.model.Light;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckUpdateActivity extends BaseActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private List<Light> mLights = new ArrayList<>();

    private UpdateAdapter mAdapter;

    private List<KiiObject> mObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);
        initAcitivityUI();
        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new UpdateAdapter();
        listView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(listView);
        new CheckUpdateTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_select_all:
                if (isAllSelected()) {
                    mSelectedSet.clear();
                } else {
                    for (Light light : mLights) {
                        mSelectedSet.add(light.id);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.action_done:
                doUpgrade();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doUpgrade() {
        new UpgradeTask().execute();
    }

    private boolean isAllSelected() {
        for (Light light : mLights) {
            if (!mSelectedSet.contains(light.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Light light = (Light) buttonView.getTag();
        if (mSelectedSet.contains(light.id)) {
            mSelectedSet.remove(light.id);
        } else {
            mSelectedSet.add(light.id);
        }
        mAdapter.notifyDataSetChanged();
    }

    class CheckUpdateTask extends AsyncTask<Void, Void, Void> {

        ProgressDialogFragment dialogFragment;

        @Override
        protected Void doInBackground(Void... params) {
            String where = "connected AND deleted=0";
            Cursor cursor = getContentResolver()
                    .query(YanKonProvider.URI_LIGHTS, null, where, null, null);
            List<String> models = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String model = cursor.getString(cursor.getColumnIndex("model"));
                    if (!models.contains(model)) {
                        models.add(model);
                    }
                }
                cursor.close();
            }
            if (models.isEmpty()) {
                return null;
            }
            KiiBucket bucket = Kii.bucket("Firmwares");
            try {
                for (String model : models) {
                    KiiQuery query = new KiiQuery(
                            KiiClause.equals("MODEL_" + model + "_linked", true));
                    query.setLimit(1);
                    query.sortByDesc("versionCode");
                    KiiQueryResult<KiiObject> result = bucket.query(query);
                    List<KiiObject> objList = result.getResult();
                    if (objList != null && !objList.isEmpty()) {
                        mObjects.add(objList.get(0));
                    }
                }
                if (mObjects.isEmpty()) {
                    return null;
                }
                //DEBUG ONLY
                for (KiiObject object : mObjects) {
                    LogUtils.d("CheckUpdateActivity", "object is " + object);
                }
                cursor = getContentResolver()
                        .query(YanKonProvider.URI_LIGHTS, null, where, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String model = cursor.getString(cursor.getColumnIndex("model"));
                        String remote = cursor.getString(cursor.getColumnIndex("remote"));
                        if (TextUtils.isEmpty(model)) {
                            continue;
                        }
                        if (!TextUtils.isEmpty(remote)) {
                            continue;
                        }
                        String firmwareVersion = cursor
                                .getString(cursor.getColumnIndex("firmware_version"));
                        for (KiiObject object : mObjects) {
                            if (object.getBoolean("MODEL_" + model + "_linked", false)
                                    && !object.getString("versionName", "")
                                    .equals(firmwareVersion)) {
                                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                                String mac = cursor.getString(cursor.getColumnIndex("MAC"));
                                Light light = new Light();
                                light.name = name;
                                light.id = id;
                                light.firmwareVersion = firmwareVersion;
                                light.model = model;
                                light.mac = mac;
                                if (!mLights.contains(light))
                                    mLights.add(light);
                                break;
                            }
                        }
                    }
                    cursor.close();
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogFragment = ProgressDialogFragment
                    .newInstance(null, getString(R.string.checking_firmware_update));
            dialogFragment.show(getFragmentManager(), "dialog");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogFragment.dismiss();
            if (mObjects.isEmpty()) {
                Toast.makeText(CheckUpdateActivity.this, R.string.no_upgrade_available, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    class UpdateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLights.size();
        }

        @Override
        public Light getItem(int position) {
            return mLights.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.update_item, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            Light light = getItem(position);
            holder.text1.setText(light.name);
            holder.text2.setText(light.model);
            //holder.lightIcon.setBackgroundResource(R.drawable.light_icon);
            holder.lightCheckbox.setTag(light);
            holder.lightCheckbox.setOnCheckedChangeListener(null);
            holder.lightCheckbox.setChecked(mSelectedSet.contains(light.id));
            holder.lightCheckbox.setOnCheckedChangeListener(CheckUpdateActivity.this);
            return view;
        }

    }

    private Set<Integer> mSelectedSet = new HashSet<>();

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file
     * 'checkable_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers
     *         (http://github.com/avast)
     */
    static class ViewHolder {

        @BindView(R.id.light_icon)
        ImageView lightIcon;

        @BindView(R.id.light_checkbox)
        CheckBox lightCheckbox;

        @BindView(android.R.id.text1)
        TextView text1;

        @BindView(android.R.id.text2)
        TextView text2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class UpgradeTask extends AsyncTask<Void, Void, Void> {

        ProgressDialogFragment dialogFragment;
        boolean succ = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogFragment = ProgressDialogFragment
                    .newInstance(null, getString(R.string.upgrading));
            dialogFragment.show(getFragmentManager(), "dialog");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogFragment.dismiss();
            if (succ) {
                Toast.makeText(CheckUpdateActivity.this, R.string.start_upgrade_fw, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CheckUpdateActivity.this, R.string.upgrade_fw_fail, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < mLights.size(); i++) {
                Light light = mLights.get(i);
                if (!mSelectedSet.contains(light.id)) {
                    continue;
                }
                try {
                    JSONObject lightObject = new JSONObject();
                    lightObject.put(light.mac, "");
                    KiiObject object = null;
                    for (KiiObject a : mObjects) {
                        if (a.getBoolean("MODEL_" + light.model + "_linked")) {
                            object = a;
                            break;
                        }
                    }
                    if (object == null) {
                        continue;
                    }
                    String result = KiiSync.upgradeLamp(lightObject, object.getString("bodyUrl"),
                            object.getString("versionName"));
                    if (result == null || !result.contains("success")) {
                        succ = false;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        if (listAdapter.getCount() < 6)
            return;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height/2;
        listView.setLayoutParams(params);
    }
}
