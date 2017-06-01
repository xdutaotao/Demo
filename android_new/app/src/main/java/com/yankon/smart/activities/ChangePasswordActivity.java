package com.yankon.smart.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ChangePasswordActivity extends BaseActivity {

    public static final String EXTRA_LIGHTS = "lights";
    String[] lightIds;
    String[] lightMacs;
    HashMap<String, String> lightMacNameMap = new HashMap<>();

    private EditText etOldAdminPwd, etNewAdminPwd, etCNewAdminPwd, etNewRemotePwd, etCNewRemotePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initActivityUI();
        lightIds = getIntent().getStringArrayExtra(EXTRA_LIGHTS);
        if (lightIds == null || lightIds.length == 0) {
            finish();
            return;
        }
        ArrayList<String> macs = new ArrayList<>();
        Cursor cursor = getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"MAC", "name"},
                "_id in " + Utils.buildNumsInSQL(lightIds), null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(1);
                String mac = cursor.getString(0);
                macs.add(mac);
                lightMacNameMap.put(mac, name);
            }
            cursor.close();
        }
        lightMacs = macs.toArray(new String[macs.size()]);

        etOldAdminPwd = (EditText) findViewById(R.id.old_admin_pwd);
        etNewAdminPwd = (EditText) findViewById(R.id.new_admin_pwd);
        etCNewAdminPwd = (EditText) findViewById(R.id.confirm_new_admin_pwd);
        etNewRemotePwd = (EditText) findViewById(R.id.new_remote_pwd);
        etCNewRemotePwd = (EditText) findViewById(R.id.confirm_new_remote_pwd);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:
                save();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String oldAdminPwd = etOldAdminPwd.getText().toString();
        String newAdminPwd = etNewAdminPwd.getText().toString();
        String cNewAdminPwd = etCNewAdminPwd.getText().toString();
        String newRemotePwd = etNewRemotePwd.getText().toString();
        String cNewRemotePwd = etCNewRemotePwd.getText().toString();

        if (newAdminPwd.length() + newRemotePwd.length() == 0) {
            Toast.makeText(this, getString(R.string.chg_pwd_none), Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldAdminPwd.length() == 0) {
            Toast.makeText(this, getString(R.string.chg_pwd_need_adminpwd), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newAdminPwd.equals(cNewAdminPwd)) {
            Toast.makeText(this, getString(R.string.chg_pwd_admin_confirm), Toast.LENGTH_SHORT).show();
            return;
        }

        if (newRemotePwd.length() > 0 && (newRemotePwd.length() != 4 || !TextUtils.isDigitsOnly(newRemotePwd))) {
            Toast.makeText(this, getString(R.string.chg_pwd_remote_4), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newRemotePwd.equals(cNewRemotePwd)) {
            Toast.makeText(this, getString(R.string.chg_pwd_remote_confirm), Toast.LENGTH_SHORT).show();
            return;
        }

        new ChangePwdTask(lightMacs, oldAdminPwd, newAdminPwd, newRemotePwd).execute();
    }

    class ChangePwdTask extends AsyncTask<Void, Void, Void> {

        String oldPwd, adminPwd, remotePwd;
        String result;
        String[] lights;

        ProgressDialogFragment dialogFragment;

        public ChangePwdTask(String[] lights, String oldPwd, String adminPwd, String remotePwd) {
            super();
            this.oldPwd = oldPwd;
            this.adminPwd = adminPwd;
            this.remotePwd = remotePwd;
            this.lights = lights;
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = KiiSync.changePwd(lights, oldPwd, adminPwd, remotePwd);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.change_pwd_async_msg));
            dialogFragment.show(getFragmentManager(), "dialog");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogFragment.dismiss();
            if (TextUtils.isEmpty(result)) {
                Toast.makeText(ChangePasswordActivity.this, R.string.kii_extension_return_null, Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> succList = new ArrayList<>();
            ArrayList<String> failList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(result);
                JSONObject succ = root.getJSONObject("success");

                Iterator<String> it = succ.keys();
                while (it.hasNext()) {//遍历JSONObject
                    String mac = it.next();
                    boolean res = succ.getBoolean(mac);
                    if (res) {
                        succList.add(mac);
                    } else {
                        String name = lightMacNameMap.get(mac);
                        if (!TextUtils.isEmpty(name)) {
                            failList.add(name);
                        }
                    }
                }
            } catch (Exception e) {
                LogUtils.e(ChangePasswordActivity.class.getSimpleName(), Log.getStackTraceString(e));
            }
            if (succList.size() == lightMacs.length) {
                Toast.makeText(ChangePasswordActivity.this, getString(R.string.chg_pwd_succ), Toast.LENGTH_SHORT).show();
                finish();
            } else if (succList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (String s : failList) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(s);
                }
                String txt = succList.size() + getString(R.string.chg_pwd_part_succ) + sb.toString();
                Toast.makeText(ChangePasswordActivity.this, txt, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(ChangePasswordActivity.this, getString(R.string.chg_pwd_all_fail), Toast.LENGTH_LONG).show();
            }

        }
    }
}
