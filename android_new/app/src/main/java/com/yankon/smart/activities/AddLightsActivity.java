package com.yankon.smart.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.DaemonHandler;
import com.yankon.smart.R;
import com.yankon.smart.model.BuildNetworkThread;
import com.yankon.smart.model.Command;
import com.yankon.smart.model.Light;
import com.yankon.smart.model.NewBuildNetworkThread;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.CardItemViewHolder;
import com.yankon.smart.widget.LightItemViewHolder;
import com.yankon.smart.widget.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AddLightsActivity extends BaseActivity {
    public static final String SSID = "SSID";
    public static final String PWD = "SSID_PWD";
    private static final int TIME = 1000;
    private String mPass, mSsid;
    private ImageView ImScan;
    private ImageView ImDian;
    private RotateAnimation animation;
    private RotateAnimation animation2;
    private TextView TxtDebug, tvLightNum;
    private BuildNetworkThread mBuildNetworkThread;
    private NewBuildNetworkThread mNetBuildNetworkThread;

    private List<Light> mLights = new ArrayList<>();
    private LightsAdapter mAdapter;
    private ListView mList;
    private View emptyView;
    private int num = 0, times = 1;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lights);
        initActivityUI();

        Global.gLightsMacMap.clear();
        tvLightNum = (TextView) findViewById(R.id.tv_light_num);
        tvLightNum.setText(String.format(getString(R.string.light_num), 0));

        TxtDebug = (TextView) findViewById(R.id.tvdebug);
        TxtDebug.setText("0");
        ImScan = (ImageView) findViewById(R.id.im_scan);
        ImDian = (ImageView) findViewById(R.id.im_dian);

        mSsid = getIntent().getStringExtra(SSID);
        mPass = getIntent().getStringExtra(PWD);
        mAdapter = new LightsAdapter(this);
        mList = (ListView) findViewById(android.R.id.list);
        emptyView = findViewById(android.R.id.empty);
        mList.setAdapter(mAdapter);
        mList.setEmptyView(emptyView);
        initAnim();
        IntentFilter filter = new IntentFilter(Constants.ACTION_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

        setTitle(getString(R.string.searching));
        handler.postDelayed(runnable, TIME); //每隔1s执行
        buildNetwork();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                TxtDebug.setText(Integer.toString(times++));
                if (times == 120){
                    //showWarningDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void showWarningDialog(){
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withMessage(R.string.all_light_stop)
                .withTitle(null)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withButton1Text(getString(android.R.string.ok))
                .withButton2Text(getString(android.R.string.cancel))
                .setButton1Click(v ->  {
                        dialogBuilder.dismiss();
                        finish();
                    })
                .setButton2Click(v1 -> {
                        dialogBuilder.dismiss();
                        Toast toast = Toast.makeText(App.getApp(), getString(R.string.build_net_again), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                   })
                .show();
    }

    private void buildNetwork() {
        if (mBuildNetworkThread == null)
            mBuildNetworkThread = new BuildNetworkThread(this, mPass);
        mBuildNetworkThread.start();

//        if (mNetBuildNetworkThread == null){
//            mNetBuildNetworkThread = new NewBuildNetworkThread(this, mPass);
//        }
//        mNetBuildNetworkThread.start();
        startAnim();
    }

    private void initAnim() {
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        animation2 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(2000);
        animation2.setRepeatCount(Animation.INFINITE);
    }

    private void stopAnim(){
        if(ImScan!=null)
            ImScan.clearAnimation();
        if(ImDian!=null)
        ImDian.clearAnimation();
    }
    private void startAnim(){

        ImScan.startAnimation(animation);
        ImDian.startAnimation(animation2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(this, getString(R.string.start_build_net), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Global.gScanLightsType = Constants.SCAN_LIGHTS_ADDLIGHTS;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_LIGHTS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBuildNetworkThread != null) {
            mBuildNetworkThread.interrupt();
            mBuildNetworkThread = null;
        }

//        if(mNetBuildNetworkThread != null) {
//            mNetBuildNetworkThread.interrupt();
//            mNetBuildNetworkThread = null;
//        }
        stopAnim();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        handler.removeCallbacks(runnable);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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
        tvLightNum.setText(String.format(getString(R.string.light_num), ++num));
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
//            holder.checkBox.setTag(light);
            holder.checkBox.setVisibility(View.GONE);
//            holder.checkBox.setChecked(light.selected || light.added);
//            holder.checkBox.setEnabled(!light.added);
//            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setOnCheckedChangeListener(AddLightsActivity.this);
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
