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
import android.text.TextUtils;
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
import com.yankon.smart.model.Switchs;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.CardItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzhenfu on 2015/8/7.
 */
public class AddSwitchsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private List<Switchs> mSwitchs = new ArrayList<>();
    private SwitchsAdapter mAdapter;
    private ListView mList;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_switchs);
        initAcitivityUI();

        Global.gSwitchsMacMap.clear();

        mAdapter = new SwitchsAdapter(this);

        mList = (ListView) findViewById(android.R.id.list);
        emptyView = findViewById(android.R.id.empty);
        mList.setAdapter(mAdapter);
        mList.setEmptyView(emptyView);

        IntentFilter filter = new IntentFilter(Constants.ACTION_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    private void querySwitchs(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwitchs.size() == 0) {  // && !AddLightsActivity.this.isDestroyed()
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
        Global.gScanSwitchsType = Constants.SCAN_SWITCHS_ADDSWITCHS;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_SWITCHS);
        querySwitchs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.SEND_TYPE);
            if (!TextUtils.isEmpty(type) && TextUtils.equals(type, "switch")){
                for (Switchs switchs : Global.gSwitchsMacMap.values()) {
                    addSwitchToList(switchs);
                }
            }

        }
    };

    private void addSwitchToList(Switchs switchs) {
        if (mSwitchs.contains(switchs)) {
            return;
        }

        Cursor cursor = getContentResolver().query(YanKonProvider.URI_SWITCHS, null, "MAC=(?) AND deleted=0", new String[]{switchs.mac}, null);
        if (cursor.moveToFirst()) {
            switchs.name = cursor.getString(cursor.getColumnIndex("name"));
            switchs.model = cursor.getString(cursor.getColumnIndex("model"));
            switchs.added = true;
        }

        cursor.close();
        mSwitchs.add(switchs);
        mAdapter.notifyDataSetChanged();
    }

    /* 只在这里把接受的数据 添加到数据库 */
    void addSwitchsToDB(Switchs switchs) {
        ContentValues values = new ContentValues();
        values.put("MAC", switchs.mac);
        values.put("model", switchs.model);
        values.put("connected", true);
        values.put("state", switchs.state);
        values.put("IP", switchs.ip);
        values.put("subIP", switchs.subIP);
        values.put("mode", 0);
        values.put("name", switchs.name);
        values.put("owned_time", System.currentTimeMillis());
        values.put("deleted", false);
        values.put("firmware_version", switchs.firmwareVersion);
        values.put("key1", switchs.key1);
        values.put("key2", switchs.key2);
        values.put("key3", switchs.key3);

        getContentResolver().insert(YanKonProvider.URI_SWITCHS, values);
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
                for (Switchs s : mSwitchs) {
                    if (!s.added) {
                        if (!s.selected) {
                            s.selected = true;
                            isAllSelected = false;
                        }
                    }
                }
                if (isAllSelected) {
                    for (Switchs s : mSwitchs) {
                        if (!s.added) {
                            s.selected = false;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            break;
            case R.id.action_done: {
                for (Switchs s : mSwitchs) {
                    if (!s.added && s.selected) {
                        addSwitchsToDB(s);
                    }
                }
                finish();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    void resetAllWifi() {
        Command cmd = new Command(Command.CommandType.CommandTypeResetWifi, 0);
        for (Switchs switchs : mSwitchs) {
            if (switchs.subIP > 0)
                Utils.sendCmdToLocalSwitch(this, switchs, cmd);
        }
        for (Switchs switchs : mSwitchs) {
            if (switchs.subIP == 0)
                Utils.sendCmdToLocalSwitch(this, switchs, cmd);
        }
        Global.gSwitchsMacMap.clear();
        Utils.setAllSwitchsOffline(this);
        mSwitchs.clear();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Switchs s = (Switchs) buttonView.getTag();
        if (s != null) {
            s.selected = isChecked;
        }
    }

    class SwitchsAdapter extends BaseAdapter {
//        Context mContext = null;
//
//        SwitchsAdapter(Context context) {
//            super();
//            mContext = context;
//        }
//
//        @Override
//        public int getCount() {
//            return mSwitchs.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mSwitchs.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = convertView;
//            if (view == null) {
//                view = LayoutInflater.from(mContext).inflate(SwitchItemViewHolder.layout_id, parent, false);
//                CardItemViewHolder holder = new CardItemViewHolder(view);
//                view.setTag(holder);
//            }
//            final Switchs s = (Switchs) getItem(position);
//            SwitchItemViewHolder holder = (SwitchItemViewHolder) view.getTag();
//            holder.title.setText(s.name);
//            return view;
//        }
Context mContext = null;

        SwitchsAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return mSwitchs.size();
        }

        @Override
        public Object getItem(int position) {
            return mSwitchs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(CardItemViewHolder.layout_id, parent, false);
                CardItemViewHolder holder = new CardItemViewHolder(view);
                view.setTag(holder);
            }
            final Switchs light = (Switchs) getItem(position);
            CardItemViewHolder holder = (CardItemViewHolder) view.getTag();
            holder.title.setText(light.name);
            holder.switchButton.setOnCheckedChangeListener(null);
            holder.switchButton.setChecked(light.state);
            return view;
        }
    }

}
