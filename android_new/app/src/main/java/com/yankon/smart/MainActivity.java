package com.yankon.smart;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.LoginCallBack;
import com.yankon.smart.activities.ChooseSiteActivity;
import com.yankon.smart.activities.LightActivity;
import com.yankon.smart.activities.LightGroupsActivity;
import com.yankon.smart.activities.LoginActivity;
import com.yankon.smart.activities.NetworkBuildActivity;
import com.yankon.smart.activities.ProfileActivity;
import com.yankon.smart.activities.ScenesActivity;
import com.yankon.smart.activities.ScheduleActivity;
import com.yankon.smart.activities.SettingsActivity;
import com.yankon.smart.fragments.AlertDialogFragment;
import com.yankon.smart.fragments.AlertDialogFragment.AlertDialogListener;
import com.yankon.smart.fragments.LogInFragment;
import com.yankon.smart.services.NetworkReceiverService;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.Settings;
import com.yankon.smart.widget.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements LoginCallBack, AlertDialogListener, AdapterView.OnItemClickListener {

    private int[] icons = {-1, R.drawable.control_light, -1, R.drawable.build_net, R.drawable.schedule, R.drawable.lights,
            R.drawable.scene, R.drawable.login, R.drawable.setting};

    private int[] text = {R.string.space, R.string.control_light, R.string.space, R.string.action_addlights_network, R.string.title_schedule, R.string.light_groups, R.string.scene,
            R.string.log_in, R.string.action_settings};

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_function);
        initView();

        IntentFilter filter = new IntentFilter(Constants.INTENT_LOGGED_IN);
        filter.addAction(Constants.INTENT_LOGGED_OUT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

        Global.isWifiConnected = false;
        Global.gDaemonHandler.sendEmptyMessage(DaemonHandler.MSG_SCAN_LIGHTS);

        if (!Global.kiiInited) {
            startActivity(new Intent(this, ChooseSiteActivity.class));
            finish();
            return;
        }

        if (!KiiUser.isLoggedIn()) {
            loginKii();
        }
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.grid_view);
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        SimpleAdapter adapter = new SimpleAdapter(this, getDatas(), R.layout.choose_function_item,
                from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getDatas(){
        List<Map<String, Object>> list = new ArrayList<>();
        int length = icons.length;
        for (int i=0; i<length; i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", icons[i]);
            map.put("text", getResources().getString(text[i]));
            list.add(map);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                break;

            case 1:
                startActivity(new Intent(MainActivity.this, LightActivity.class));
                break;

            case 2:
                break;

            case 3:
                startActivity(new Intent(MainActivity.this, NetworkBuildActivity.class));
                break;

            case 4:
                if (Settings.isLoggedIn()){
                    startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
                }else{
                    DialogFragment dialogFragment = AlertDialogFragment.newInstance(0);
                    dialogFragment.show(getFragmentManager(), "dialog");
                }
                break;

            case 5:
                startActivity(new Intent(MainActivity.this, LightGroupsActivity.class));
                break;

            case 6:
                startActivity(new Intent(MainActivity.this, ScenesActivity.class));
                break;

            case 7:
                if (Settings.isLoggedIn()){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;

            case 8:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        AlertDialog.Builder ab = new AlertDialog.Builder(this);
//        ab.setMessage(R.string.exit_prompt);
//        ab.setNegativeButton(android.R.string.cancel, null);
//        ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        ab.show();

        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withMessage(R.string.exit_prompt)
                .withTitle(null)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withButton1Text(getString(android.R.string.ok))
                .withButton2Text(getString(android.R.string.cancel))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                        finish();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        stopService(new Intent(this, NetworkReceiverService.class));
        super.onDestroy();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_LOGGED_IN) || action.equals(Constants.INTENT_LOGGED_OUT)) {
                if (Settings.isLoggedIn()){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        }
    };

    protected void loginKii() {
        String token = Settings.getToken();
        if (!KiiUser.isLoggedIn() && !TextUtils.isEmpty(token)) {
            KiiUser.loginWithToken(this, token, Settings.getExp());
        }
    }

    @Override
    public void onLoginCompleted(KiiUser kiiUser, Exception e) {
        if (kiiUser != null) {
            Toast.makeText(this, "Kii logged in", Toast.LENGTH_SHORT).show();
            KiiSync.asyncFullSync();
        } else {
            Toast.makeText(this, "Kii login failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOk(int type) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onCancel(int type) {
    }
}
