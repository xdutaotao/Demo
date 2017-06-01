package com.yankon.smart.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.fragments.SwitchFragment;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.services.NetworkSenderService;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Utils;

/**
 * Created by James on 2015/8/3.
 */
public class SwitchInfoActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener
{
    public static final String EXTRA_LIGHT_ID = "light_id";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_LIGHTS = "lights";
    public static final String EXTRA_MODEL = "model";

    private ToggleButton mToggleBtn1, mToggleBtn2, mToggleBtn3;
    private boolean state;
    private int light_id = -1, switchNum = -1;
    private String name, model;
    private boolean key1, key2, key3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_info);
        initActivityUI();
        parseIntent();
        initView();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        light_id = intent.getIntExtra(SwitchInfoActivity.EXTRA_LIGHT_ID, -1);
        model = intent.getStringExtra(SwitchInfoActivity.EXTRA_MODEL);
        name = intent.getStringExtra(SwitchInfoActivity.EXTRA_NAME);
        if (!TextUtils.isEmpty(name)) {
            setTitle(name);
        }

        switchNum = Integer.valueOf(model.substring(9));
        Cursor c = null;
        if (!getIntent().hasExtra("state")) {
            if (light_id > 0) {
                c = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "_id=" + light_id, null, null);
            }
        }

        if (c != null) {
            if (c.moveToFirst()) {
                if (light_id > 0) {
                    state = c.getInt(c.getColumnIndex("state")) != 0;
                    key1 = c.getInt(c.getColumnIndex("key1")) != 0;
                    key2 = c.getInt(c.getColumnIndex("key2")) != 0;
                    key3 = c.getInt(c.getColumnIndex("key3")) != 0;
                }
            }
            c.close();
        }

    }

    private void initView() {
        mToggleBtn1 = (ToggleButton) findViewById(R.id.switch_button_1);
        mToggleBtn2 = (ToggleButton) findViewById(R.id.switch_button_2);
        mToggleBtn3 = (ToggleButton) findViewById(R.id.switch_button_3);
        mToggleBtn1.setOnCheckedChangeListener(this);
        mToggleBtn2.setOnCheckedChangeListener(this);
        mToggleBtn3.setOnCheckedChangeListener(this);
        mToggleBtn1.setChecked(key1);
        mToggleBtn2.setChecked(key2);
        mToggleBtn3.setChecked(key3);

        switch (switchNum){
            case 1:
                mToggleBtn2.setVisibility(View.GONE);
                mToggleBtn3.setVisibility(View.GONE);
                break;

            case 2:
                mToggleBtn3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Command cmd;
        byte checked = (byte)(b ? 1 : 0);

        switch (compoundButton.getId()){
            case R.id.switch_button_1:
                //00 00 00 00 0B 00 01 01 00 00 00 0C 01 01 01 00 01 第一个开关 开
                //00 00 00 00 0B 00 01 01 00 00 00 0C 01 01 01 00 00 第一个开关 关

                //byte[] send_data1 = {00, 00, 00, 00, 0x0B, 00, 01, 01, 00, 00, 00, 0x0C, 01, 01, 01, 00, checked};
                //NetworkSenderService.sendCmd(this, ip, send_data1);
                cmd = new Command(Command.CommandSwitchsType.CommandTypeKey1, checked);
                Utils.controlSwitch(this, light_id, cmd, true);
                break;

            case R.id.switch_button_2:
                //00 00 00 00 0B 00 01 01 00 00 00 0C 02 01 01 00 01 第一个开关 开
                //00 00 00 00 0B 00 01 01 00 00 00 0C 02 01 01 00 00 第一个开关 关

                cmd = new Command(Command.CommandSwitchsType.CommandTypeKey2, checked);
                Utils.controlSwitch(this, light_id, cmd, true);
                break;

            case R.id.switch_button_3:
                //00 00 00 00 0B 00 01 01 00 00 00 0C 03 01 01 00 01 第一个开关 开
                //00 00 00 00 0B 00 01 01 00 00 00 0C 03 01 01 00 00 第一个开关 关

                cmd = new Command(Command.CommandSwitchsType.CommandTypeKey3, checked);
                Utils.controlSwitch(this, light_id, cmd, true);
                break;

            default:
                break;
        }
    }
}
