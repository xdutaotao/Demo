package com.yankon.smart.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yankon.smart.App;
import com.yankon.smart.R;
import com.yankon.smart.fragments.InputDialogFragment;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.BaseListActivity;
import com.yankon.smart.widget.CardItemViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by guzhenfu on 2015/8/17.
 */
public class LightActivity extends BaseListActivity implements InputDialogFragment.InputDialogInterface {
    public static final int MENU_EDIT = 0;
    public static final int MENU_DELETE = 4;
    public static final int MENU_SET_REMOTE_PWD = 1;
    public static final int MENU_CHANGE_PWD = 2;
    public static final int MENU_RESET_NETWORK = 3;
    public static final int MENU_TRANSFER =5;

    private static boolean isFirstLaunch = false;
    private View headerView;
    private TextView tv;
    private ToggleButton multi_state_switch;
    HashSet<String> mSelectedLights = new HashSet<>();

    String[] currLightIds = null;
    public int currentEditId = -1;
    private int menuType = -1;
    private boolean transfer = false;
    private boolean headState = true;
    private boolean connected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initTitleView();
        mAdapter = new LightsAdapter(this);
        initHeaderView();
        initListView();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = null;
                Intent intent = new Intent(LightActivity.this, LightInfoActivity.class);
                if (position == 0) {
                    if (mSelectedLights.size() == 0) {
                        Toast.makeText(LightActivity.this, R.string.lights_multiple_empty, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                    if (lights.length == 1) {
                        Cursor cursor = LightActivity.this.getContentResolver().query(YanKonProvider.URI_LIGHTS,
                                new String[]{"name"},
                                "_id=(?)", new String[]{lights[0]},
                                null);
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                name = cursor.getString(0);
                                intent.putExtra(LightInfoActivity.EXTRA_LIGHT_ID, Integer.parseInt(lights[0]));
                            }
                            cursor.close();
                        }
                    } else {
                        name = getString(R.string.lights_multiple_title, mSelectedLights.size());
                        intent.putExtra(LightInfoActivity.EXTRA_LIGHTS, lights);
                        Cursor c = LightActivity.this.getContentResolver().query(YanKonProvider.URI_LIGHTS, null,
                                "_id in " + Utils.buildNumsInSQL(lights), null, null);
                        if (c != null) {
                            boolean isFirst = true;
                            int state = 0, color = 0, brightness = 0, CT = 0, mode = 0;
                            while (c.moveToNext()) {
                                if (isFirst) {
                                    isFirst = false;
                                    state = c.getInt(c.getColumnIndex("state"));
                                    color = c.getInt(c.getColumnIndex("color"));
                                    brightness = c.getInt(c.getColumnIndex("brightness"));
                                    CT = c.getInt(c.getColumnIndex("CT"));
                                    mode = c.getInt(c.getColumnIndex("mode"));
                                    intent.putExtra("state", state > 0);
                                    intent.putExtra("color", color);
                                    intent.putExtra("brightness", brightness);
                                    intent.putExtra("CT", CT);
                                    intent.putExtra("mode", mode);
                                    intent.putExtra("type", c.getInt(c.getColumnIndex("type")));
                                    intent.putExtra("number", c.getInt(c.getColumnIndex("number")));
                                    intent.putExtra("sens", c.getInt(c.getColumnIndex("sens")));
                                    intent.putExtra("lux", c.getInt(c.getColumnIndex("lux")));
                                } else {
                                    int tmp = c.getInt(c.getColumnIndex("state"));
                                    if (tmp != state)
                                        intent.putExtra("state", false);
                                    tmp = c.getInt(c.getColumnIndex("color"));
                                    if (tmp != color)
                                        intent.putExtra("color", Constants.DEFAULT_COLOR);
                                    tmp = c.getInt(c.getColumnIndex("brightness"));
                                    if (tmp != brightness)
                                        intent.putExtra("brightness", Constants.DEFAULT_BRIGHTNESS);
                                    tmp = c.getInt(c.getColumnIndex("CT"));
                                    if (tmp != CT)
                                        intent.putExtra("CT", Constants.DEFAULT_CT);
                                    tmp = c.getInt(c.getColumnIndex("mode"));
                                    if (tmp != mode)
                                        intent.putExtra("mode", Constants.DEFAULT_MODE);
                                    tmp = c.getInt(c.getColumnIndex("type"));
                                    if (tmp != mode)
                                        intent.putExtra("type", Constants.DEFAULT_MODE);

                                    tmp = c.getInt(c.getColumnIndex("number"));
                                    if (tmp != mode)
                                        intent.putExtra("number", Constants.DEFAULT_MODE);

                                    tmp = c.getInt(c.getColumnIndex("sens"));
                                    if (tmp != mode)
                                        intent.putExtra("sens", Constants.DEFAULT_MODE);

                                    tmp = c.getInt(c.getColumnIndex("lux"));
                                    if (tmp != mode)
                                        intent.putExtra("lux", Constants.DEFAULT_MODE);
                                }
                            }
                            c.close();
                        }
                    }
                } else {
                    cursor = (Cursor) mAdapter.getItem(position - 1);
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    intent.putExtra(LightInfoActivity.EXTRA_LIGHT_ID, (int) id);
                }
                intent.putExtra(LightInfoActivity.EXTRA_NAME, name);
                startActivity(intent);
            }
        });
        isFirstLaunch = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateMutliState();
    }

    void updateMutliState() {
        if (mSelectedLights.size() == 0) {
            multi_state_switch.setChecked(false);
            return;
        }
        String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);

        Cursor c = this.getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"sum(state)"},
                "_id in " + Utils.buildNumsInSQL(lights), null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                int sum = c.getInt(0);
                if (sum == lights.length) {
                    multi_state_switch.setChecked(true);
                } else {
                    multi_state_switch.setChecked(false);
                }
            }
            c.close();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String where = "deleted=0";
        return new CursorLoader(this, YanKonProvider.URI_LIGHTS, null, where, null,
                "connected desc, owned_time asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (isFirstLaunch){
            isFirstLaunch = false;
            int num = 0;
            Cursor c = getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"_id"}, null, null, null);
            if (c != null) {
                num = c.getCount();
                c.close();
            }
            if (num == 0) {
                startActivity(new Intent(this, AddLightsActivity.class));
            }
        }
        selectAll();
    }

    void updateHeaderView() {
        tv.setText(getString(R.string.header_selected_lights, mSelectedLights.size()));
    }

    void initHeaderView() {
        ListView lv = getListView();
        final View headerViewContainer = View.inflate(this, R.layout.lights_header, null);
        lv.addHeaderView(headerViewContainer);
        headerView = headerViewContainer.findViewById(R.id.header_view);
        tv = (TextView) headerView.findViewById(R.id.title);
        multi_state_switch = (ToggleButton) headerView.findViewById(R.id.switch_button);
        multi_state_switch.setChecked(headState);
        multi_state_switch.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      ToggleButton cb = (ToggleButton) v;
                                                      String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                                                      Command cmd = new Command(Command.CommandType.CommandTypeState, cb.isChecked() ? 1 : 0);
                                                      Utils.controlLightsById(App.getApp(), lights, cmd, true);
                                                  }
                                              }

        );
        View info = headerView.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        headerViewContainer.showContextMenu();
                                    }
                                }

        );
        updateHeaderView();
    }

    void selectAll() {
        HashSet<String> tmpSet = new HashSet<>();
        tmpSet.addAll(mSelectedLights);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            cursor = (Cursor) mAdapter.getItem(i);
            String lid = cursor.getString(cursor.getColumnIndex("_id"));
            if (!mSelectedLights.contains(lid)) {
                mSelectedLights.add(lid);
            } else {
                tmpSet.remove(lid);
            }
        }
        updateHeaderView();
        updateMutliState();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.add:
                startActivity(new Intent(this, AddLightsActivity.class));
                break;
        }
    }

    void showEditAction(String text) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(getString(R.string.edit_light_name),
                text,
                null, null, 0);
        newFragment.setInputDialogInterface(this);
        newFragment.show(getFragmentManager(), "dialog");

    }

    void showSetPassword() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getString(R.string.input_remotepwd_title),
                null,
                getString(R.string.input_remotepwd_hint), null, InputDialogFragment.TYPE_REMOTE_PWD);
        newFragment.setInputDialogInterface(this);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void showTransfer() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getString(R.string.transfer_title),
                null,
                getString(R.string.transfer_hint), null, 0);
        newFragment.setInputDialogInterface(this);
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (cursorDataPos == -1){
            position += 1;
        }else if (!connected && position == 3){
            position ++;
        }

        switch (position) {
            case MENU_EDIT: {
                if (cursorDataPos == -1)
                    break;
                cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                currentEditId = cursor.getInt(cursor.getColumnIndex("_id"));
                menuType = MENU_EDIT;
                showEditAction(name);
            }
            break;
            case MENU_DELETE: {
                if (cursorDataPos == -1){
                    for (String lid : mSelectedLights) {
                        DataHelper.deleteLightById(Integer.parseInt(lid));
                    }
                    mSelectedLights.clear();
                }else{
                    cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    String lid = cursor.getString(cursor.getColumnIndex("_id"));
                    DataHelper.deleteLightById(cid);
                    mSelectedLights.remove(lid);
                }
                updateHeaderView();
            }
            break;
            case MENU_SET_REMOTE_PWD: {
                if (cursorDataPos == -1){
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                }else{
                    if (transfer){
                        cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                        int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                        currLightIds = new String[]{String.valueOf(cid)};
                        menuType = MENU_TRANSFER;
                        showTransfer();
                        break;
                    }else{
                        cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                        int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                        currLightIds = new String[]{String.valueOf(cid)};
                    }
                }
                menuType = MENU_SET_REMOTE_PWD;
                showSetPassword();
            }
            break;

            case MENU_CHANGE_PWD: {
                if (cursorDataPos == -1){
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                }else{
                    cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    currLightIds = new String[]{String.valueOf(cid)};
                }
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra(ChangePasswordActivity.EXTRA_LIGHTS, currLightIds);
                startActivity(intent);
            }
            break;
            case MENU_RESET_NETWORK: {
                if (cursorDataPos == -1){
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                    Command cmd = new Command(Command.CommandType.CommandTypeResetWifi, 0);
                    Utils.controlLightsById(App.getApp(), currLightIds, cmd, true);
                    cursor = this.getContentResolver().query(YanKonProvider.URI_LIGHTS, null,
                            "_id in " + Utils.buildNumsInSQL(currLightIds), null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            boolean connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
                            if (connected){
                                String lid = cursor.getString(cursor.getColumnIndex("_id"));
                                DataHelper.deleteLightById(Integer.parseInt(lid));
                                mSelectedLights.remove(lid);
                            }
                        }
                        cursor.close();
                    }
                }else{
                    cursor = (Cursor) mAdapter.getItem(cursorDataPos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    currLightIds = new String[]{String.valueOf(cid)};
                    Command cmd = new Command(Command.CommandType.CommandTypeResetWifi, 0);
                    Utils.controlLightsById(App.getApp(), currLightIds, cmd, true);

                    boolean connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
                    if (connected){
                        DataHelper.deleteLightById(cid);
                        mSelectedLights.remove(String.valueOf(cid));
                    }
                }
            }
            break;
        }
        super.onItemClick(adapterView, view, position, id);
    }

    @Override
    public void onInputDialogTextDone(String text) {
        if (!TextUtils.isEmpty(text)){
            switch (menuType){
                case MENU_EDIT:{
                    ContentValues values = new ContentValues();
                    values.put("name", text);
                    values.put("synced", false);
                    if (currentEditId > -1) {
                        getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "_id=" + currentEditId, null);
                    }
                }
                break;

                case MENU_SET_REMOTE_PWD:{
                    String pwd = text;
                    if (pwd != null && pwd.length() == 4 && TextUtils.isDigitsOnly(pwd)) {
                        new SetRemotePwdTask(pwd).execute();
                    } else {
                        Toast.makeText(this, R.string.remotepwd_prompt, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

                case MENU_TRANSFER:{
                    new TransferTask(text).execute();
                }
                break;
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        contextMenuData.clear();
        String name = null;
        if (position == 0){
            if (mSelectedLights.size() == 0) {
                Toast.makeText(this, R.string.lights_multiple_empty, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (mSelectedLights.size() == 1) {
                name = getString(R.string.lights_multiple_title_1light);
            } else {
                name = getString(R.string.lights_multiple_title, mSelectedLights.size());
            }
            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("value", getString(R.string.set_remote_pwd));
            contextMenuData.add(hashMap1);
            cursorDataPos = -1;
            connected = true;
        }else{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("value", getString(R.string.menu_edit));
            contextMenuData.add(hashMap);
            position -= 1;
            cursorDataPos = position;
            cursor = (Cursor) mAdapter.getItem(position);
            name = cursor.getString(cursor.getColumnIndex("name"));
            connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
            if (TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remote")))) {
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("value", getString(R.string.set_remote_pwd));
                contextMenuData.add(hashMap1);
                transfer = false;
            } else {
                HashMap<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("value", getString(R.string.transfer_server));
                contextMenuData.add(hashMap2);
                transfer = true;
            }
        }

        HashMap<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("value", getString(R.string.change_password));
        contextMenuData.add(hashMap3);
        if (connected){
            HashMap<String, Object> hashMap4 = new HashMap<>();
            hashMap4.put("value", getString(R.string.action_addlights_unnetwork));
            contextMenuData.add(hashMap4);
        }
        HashMap<String, Object> hashMap5 = new HashMap<>();
        hashMap5.put("value", getString(R.string.menu_delete));
        contextMenuData.add(hashMap5);
        showListViewDialog(name, contextMenuData);
        return true;
    }

    class SetRemotePwdTask extends AsyncTask<Void, Void, Void> {
        ProgressDialogFragment dialogFragment;
        String pwd;
        String result;

        public SetRemotePwdTask(String pwd) {
            super();
            this.pwd = pwd;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor c = App.getApp().getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"MAC"}, "_id in " + Utils.buildNumsInSQL(currLightIds), null, null);
            ArrayList<Pair<String, String>> lights = new ArrayList<>();
            if (c != null) {
                while (c.moveToNext()) {
                    lights.add(new Pair<>(c.getString(0), pwd));
                }
                c.close();
            }
            result = KiiSync.fireLamp(lights, true, null);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.set_pwd_async_msg));
            dialogFragment.show(getFragmentManager(), "dialog");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogFragment.dismiss();
            if (TextUtils.isEmpty(result)) {
                Toast.makeText(LightActivity.this, R.string.kii_extension_return_null, Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<String> succList = new ArrayList<>();
            ArrayList<String> failList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(result);
                JSONObject succ = root.getJSONObject("lights");

                Iterator<String> it = succ.keys();
                while (it.hasNext()) {//遍历JSONObject
                    String mac = it.next();
                    int res = succ.getInt(mac);
                    if (res == 0) {
                        succList.add(mac);
                    } else {
                        failList.add(mac);
                    }
                }
            } catch (Exception e) {
                LogUtils.e(ChangePasswordActivity.class.getSimpleName(), Log.getStackTraceString(e));
            }
            if (succList.size() > 0) {
                String[] succMacs = succList.toArray(new String[succList.size()]);
                ContentValues values = new ContentValues();
                values.put("remote_pwd", pwd);
                values.put("synced", false);
                LightActivity.this.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "MAC in " + Utils.buildStringsInSQL(succMacs), null);
            }
            if (failList.size() > 0) {
                String[] failMacs = failList.toArray(new String[failList.size()]);
                ContentValues values = new ContentValues();
                values.put("remote_pwd", "");
                values.put("synced", false);
                LightActivity.this.getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "MAC in " + Utils.buildStringsInSQL(failMacs), null);
            }
            if (succList.size() > 0 && failList.size() == 0) {
                Toast.makeText(LightActivity.this, R.string.enable_remote_succ, Toast.LENGTH_SHORT).show();
            } else if (succList.size() == 0 && failList.size() > 0) {
                Toast.makeText(LightActivity.this, R.string.enable_remote_fail, Toast.LENGTH_SHORT).show();
            } else if (succList.size() > 0 && failList.size() > 0) {
                Toast.makeText(LightActivity.this, R.string.enable_remote_part_succ, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class TransferTask extends AsyncTask<Void, Void, Void> {
        ProgressDialogFragment dialogFragment;
        String pwd;
        String result;

        public TransferTask(String pwd) {
            super();
            this.pwd = pwd;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor c = LightActivity.this.getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"MAC"}, "_id=" + currLightIds[0], null, null);
            String thingID = "";
            if (c.moveToNext()) {
                thingID = c.getString(0);
            }
            c.close();
            if (!TextUtils.isEmpty(thingID)) {
                result = KiiSync.migrateLamp(thingID, pwd);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.transfering_msg));
            dialogFragment.show(getFragmentManager(), "dialog");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogFragment.dismiss();
            if (TextUtils.isEmpty(result)) {
                Toast.makeText(LightActivity.this, R.string.kii_extension_return_null, Toast.LENGTH_SHORT).show();
                return;
            }
            boolean succ = false;
            try {
                JSONObject root = new JSONObject(result);
                succ = root.optBoolean("success", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (succ) {
                Toast.makeText(LightActivity.this, R.string.transfer_succ, Toast.LENGTH_SHORT).show();
                KiiSync.asyncFullSync();
            } else {
                Toast.makeText(LightActivity.this, R.string.transfer_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LightsAdapter extends CursorAdapter {

        public LightsAdapter(Context context) {
            super(context, null, 0);
        }

        /*
        *   数据变化的时候调用，但是重绘的时候不会
        */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(CardItemViewHolder.layout_id, parent, false);
            CardItemViewHolder holder = new CardItemViewHolder(view);
            view.setTag(holder);
            return view;
        }

        /*
        *   在重绘的时候会调用
        */
        @Override
        public void bindView(final View view, Context context, Cursor cursor) {
            CardItemViewHolder holder = (CardItemViewHolder) view.getTag();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            if (!TextUtils.equals(holder.title.getText(), name)){
                holder.setIconColor();
            }
            holder.title.setText(name);
            boolean connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
            String remotePwd = cursor.getString(cursor.getColumnIndex("remote_pwd"));
            if (connected) {
                holder.updateStatus(CardItemViewHolder.STATUS_LOCAL, remotePwd != null && remotePwd.length() == 4);
            }else{
                holder.updateStatus(CardItemViewHolder.STATUS_REMOTE,remotePwd != null && remotePwd.length() == 4);
            }

            final boolean state = cursor.getInt(cursor.getColumnIndex("state")) > 0;
            final int light_id = cursor.getInt(cursor.getColumnIndex("_id"));
            String lid = String.valueOf(light_id);
            holder.switchButton.setChecked(state);
            holder.switchButton.setTag(lid);
            holder.switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = !state;
                    Command cmd = new Command(Command.CommandType.CommandTypeState, isChecked ? 1 : 0);
                    Utils.controlLight(LightActivity.this, light_id, cmd, true);
                }
            });
            holder.infoButton.setVisibility(View.VISIBLE);
            holder.infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.showContextMenu();
                }
            });
        }

    }

}
