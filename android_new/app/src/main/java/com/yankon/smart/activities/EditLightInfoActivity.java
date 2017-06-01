package com.yankon.smart.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.DelayDialogFragment;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guzhenfu on 2015/10/19.
 */
public class EditLightInfoActivity extends BaseActivity implements DelayDialogFragment.DelayDialogInterface, CompoundButton.OnCheckedChangeListener {
    private String name, mac, mNewSsid, mNewPwd, newName;
    private Boolean remote;
    private int ip, lightID;
    private EditText etName, etMac, etIP, etSSID,  etRemote;
    private TextView tvPwd;
    private int isDisplaySSID;
    private ToggleButton tbEnableSSID;
    boolean apState;
    String apSsid;
    String apPass;
    boolean tbChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_light_info);
        ButterKnife.bind(this);
        initActivityUI();

        name = getIntent().getStringExtra(LightInfoActivity.EXTRA_NAME);
//        mac = getIntent().getStringExtra(LightInfoActivity.MAC);
//        ip = getIntent().getIntExtra(LightInfoActivity.IP, -1);
//        mOldSsid = Utils.getSSID(this, mac);
//        mOldPwd = Utils.getSSIDPWD(this, mac);
//        remote = getIntent().getBooleanExtra(LightInfoActivity.REMOTE, false);
        lightID = getIntent().getIntExtra(LightInfoActivity.EXTRA_LIGHT_ID, -1);
//        apState = getIntent().getBooleanExtra(LightInfoActivity.AP_STATE, false);
//        apSsid = getIntent().getStringExtra(LightInfoActivity.AP_SSID);
//        apPass = getIntent().getStringExtra(LightInfoActivity.AP_PASS);

        etName = (EditText) findViewById(R.id.et_light_name);
        etMac = (EditText) findViewById(R.id.et_light_mac);
        etIP = (EditText) findViewById(R.id.et_light_ip);
        etSSID = (EditText) findViewById(R.id.et_light_ssid);
        tvPwd = (TextView) findViewById(R.id.et_light_pwd);
        etRemote = (EditText) findViewById(R.id.et_light_remote);
        tbEnableSSID = (ToggleButton) findViewById(R.id.tb_enable_ssid);
//        oldSSIDState = Utils.getSSIDState(this, mac);
        tbChecked = apState;
        tbEnableSSID.setChecked(apState);
        if (!apState) {
            etSSID.setEnabled(false);
//            etPwd.setEnabled(false);
        }
        tbEnableSSID.setOnCheckedChangeListener(this);

        if ("".equals(apSsid) || apSsid == null) {
            etSSID.setEnabled(false);
//            etPwd.setEnabled(false);
        }
        etName.setText(name);
        etMac.setText(Utils.formatMac(mac));
        etIP.setText(Utils.intToIp(ip));
        etSSID.setText(apSsid);
        tvPwd.setText(apPass);

        if (remote)
            etRemote.setText(getString(R.string.no_remote));
        else {
            etRemote.setText(getString(R.string.yes_remote));
            etSSID.setEnabled(false);
//            etPwd.setEnabled(false);
            etName.setEnabled(false);
            tbEnableSSID.setEnabled(false);
        }

        setTitle(name);
//        int flag = 0x08;
//        isDisplaySSID = getIntent().getIntExtra(LightInfoActivity.PLATFORM, -1);
//
//        if ((isDisplaySSID & flag) != 0){
//            etPwd.setEnabled(false);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_light_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_done:
                saveChange();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveChange() {
        newName = etName.getText().toString();
        mNewSsid = etSSID.getText().toString();
        mNewPwd = tvPwd.getText().toString();
        if ((newName == null) || (mNewSsid == null) || (mNewPwd == null) ||
                "".equals(newName)) {
            Toast.makeText(this, getString(R.string.input_dialog_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mNewSsid.equals(apSsid) && mNewPwd.equals(apPass) && (tbChecked == apState) && (TextUtils.equals(name, newName))) {
            finish();
            return false;
        }

        if (!mNewSsid.equals(apSsid) || !mNewPwd.equals(apPass) || !TextUtils.equals(name, newName)) {
            ContentValues values = new ContentValues();
            values.put("name", newName);
            values.put("AP_state", tbChecked);
            Utils.saveSSIDState(this, mac, tbEnableSSID.isChecked());
            values.put("AP_SSID", mNewSsid);
            Utils.saveSSID(App.getApp(), mac, mNewSsid);
            values.put("AP_Pass", mNewPwd);
            Utils.saveSSIDPWD(App.getApp(), mac, mNewPwd);
            getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "_id=" + lightID, null);

            if (remote && !"".equals(mNewSsid)) {
//                Command cmd = new Command(Command.CommandType.CommandTypeAP, 7);
//                Utils.controlLight(this, lightID, cmd, true);
                DelayDialogFragment fragment = DelayDialogFragment.newInstance(getString(R.string.please_wait), 5);
                fragment.setDelayDialogInterface(this);
                fragment.show(getFragmentManager(), "dialog");
            } else {
                delayDialogDone();
            }
        } else if (remote){

//            Command cmd = new Command(Command.CommandType.CommandTypeAPState, tbEnableSSID.isChecked() ? 1 : 0);
//            Utils.controlLight(EditLightInfoActivity.this, lightID, cmd, true);
            DelayDialogFragment fragment = DelayDialogFragment.newInstance(getString(R.string.please_wait), 5);
            fragment.setDelayDialogInterface(this);
            fragment.show(getFragmentManager(), "dialog");
        }else{

        }

        return true;
    }

    @Override
    public void delayDialogDone() {
        Intent intent = new Intent();
        intent.putExtra(LightInfoActivity.EXTRA_NAME, etName.getText().toString());
//        intent.putExtra(LightInfoActivity.AP_STATE,tbEnableSSID.isChecked());
//        intent.putExtra(LightInfoActivity.AP_SSID,etSSID.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        tbChecked = isChecked;
        if (!isChecked) {
            etSSID.setEnabled(false);
//            etPwd.setEnabled(false);
        } else {
            etSSID.setEnabled(true);
//            etPwd.setEnabled(true);
        }
    }

}
