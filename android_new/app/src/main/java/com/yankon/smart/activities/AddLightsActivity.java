package com.yankon.smart.activities;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.DaemonHandler;
import com.yankon.smart.R;
import com.yankon.smart.fragments.AlertDialogFragment;
import com.yankon.smart.model.Command;
import com.yankon.smart.model.Light;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.CardItemViewHolder;
import com.yankon.smart.widget.LightItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AddLightsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, AlertDialogFragment.AlertDialogListener {

    private List<Light> mLights = new ArrayList<>();
    private LightsAdapter mAdapter;
    private ListView mList;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lights);
        initAcitivityUI();

        Global.gLightsMacMap.clear();

        mAdapter = new LightsAdapter(this);
        mList = (ListView) findViewById(android.R.id.list);
        emptyView = findViewById(android.R.id.empty);
        mList.setAdapter(mAdapter);
        mList.setEmptyView(emptyView);

        IntentFilter filter = new IntentFilter(Constants.ACTION_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

        queryLight();
    }

    private void queryLight(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLights.size() == 0) {  // && !AddLightsActivity.this.isDestroyed()
                    try {
                        DialogFragment dialogFragment = AlertDialogFragment.newInstance(AlertDialogFragment.TYPE_BUILD_NETWORK);
                        dialogFragment.show(getFragmentManager(), "dialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 5000L);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_ADDLIGHTS;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_LIGHTS);
        //queryLight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String type = intent.getStringExtra(Constants.SEND_TYPE);
//            if ( !TextUtils.isEmpty(type) && TextUtils.equals(type, "light")){
//                for (Light light : Global.gLightsMacMap.values()) {
//                    addLightToList(light);
//                }
//            }
            for (Light light : Global.gLightsMacMap.values()) {
                addLightToList(light);
            }

        }
    };

    void addLightToList(Light light) {
        if (mLights.contains(light)) {
            return;
        }

        Cursor cursor = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "MAC=(?) AND deleted=0", new String[]{light.mac}, null);
        if (cursor.moveToFirst()) {
            light.name = cursor.getString(cursor.getColumnIndex("name"));
            light.model = cursor.getString(cursor.getColumnIndex("model"));
            light.added = true;
        }
        cursor.close();
        mLights.add(light);
        mAdapter.notifyDataSetChanged();
    }

    /* 只在这里把接受的数据 添加到数据库 */
    void addLightToDB(Light light) {
        ContentValues values = new ContentValues();
        values.put("MAC", light.mac);
        values.put("model", light.model);
        values.put("connected", true);
        values.put("state", light.state);
        values.put("IP", light.ip);
        values.put("subIP", light.subIP);
        values.put("color", light.color);
        values.put("brightness", Math.max(light.brightness, Constants.MIN_BRIGHTNESS));
        values.put("CT", Math.max(light.CT, Constants.MIN_CT));
        values.put("mode", 0);
        values.put("name", light.name);
        values.put("owned_time", System.currentTimeMillis());
        values.put("deleted", false);
        values.put("firmware_version", light.firmwareVersion);

        getContentResolver().insert(YanKonProvider.URI_LIGHTS, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_lights, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_select_all: {
                boolean isAllSelected = true;
                for (Light l : mLights) {
                    if (!l.added) {
                        if (!l.selected) {
                            l.selected = true;
                            isAllSelected = false;
                        }
                    }
                }
                if (isAllSelected) {
                    for (Light l : mLights) {
                        if (!l.added) {
                            l.selected = false;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            break;
            case R.id.action_done: {
                for (Light l : mLights) {
                    if (!l.added && l.selected) {
                        addLightToDB(l);
                    }
                }
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    void resetAllWifi() {
//                NetworkSenderService.sendCmd(this, Constants.BROADCAST_IP, Constants.RESET_ALL_WIFI);
        Command cmd = new Command(Command.CommandType.CommandTypeResetWifi, 0);
        for (Light light : mLights) {
            if (light.subIP > 0)
                Utils.sendCmdToLocalLight(this, light, cmd);
        }
        for (Light light : mLights) {
            if (light.subIP == 0)
                Utils.sendCmdToLocalLight(this, light, cmd);
        }
        Global.gLightsMacMap.clear();
        Utils.setAllLightsOffline(this);
        mLights.clear();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Light light = (Light) buttonView.getTag();
        if (light != null) {
            light.selected = isChecked;
        }
    }

    @Override
    public void onOk(int type) {
        switch (type) {
            case AlertDialogFragment.TYPE_BUILD_NETWORK:
                startActivity(new Intent(this, NetworkBuildActivity.class));
                break;
            case AlertDialogFragment.TYPE_RESET_NETWORK:
                resetAllWifi();
                break;
        }
    }

    @Override
    public void onCancel(int type) {
        //queryLight();
    }


    class LightsAdapter extends BaseAdapter {
        Context mContext = null;

        LightsAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return mLights.size();
        }

        @Override
        public Object getItem(int position) {
            return mLights.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(LightItemViewHolder.layout_id, parent, false);
                LightItemViewHolder holder = new LightItemViewHolder(view);
                view.setTag(holder);
            }
            final Light light = (Light) getItem(position);
            LightItemViewHolder holder = (LightItemViewHolder) view.getTag();
            holder.title.setText(light.name);
            holder.label1.setText(R.string.model);
            holder.textView1.setText(light.model);
            holder.label2.setText(R.string.mac_address);
            holder.textView2.setText(Utils.formatMac(light.mac));
            holder.switchButton.setOnCheckedChangeListener(null);
            holder.switchButton.setChecked(light.state);
            holder.checkBox.setTag(light);
            holder.checkBox.setChecked(light.selected || light.added);
            holder.checkBox.setEnabled(!light.added);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(AddLightsActivity.this);
            holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Command cmd = new Command(Command.CommandType.CommandTypeState, isChecked ? 1 : 0);
                    Utils.sendCmdToLocalLight(mContext, light, cmd);
                }
            });
            holder.updateStatus(CardItemViewHolder.STATUS_NONE, false, false);
            return view;
        }
    }
}
