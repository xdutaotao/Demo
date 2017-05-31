package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yankon.smart.App;
import com.yankon.smart.R;
import com.yankon.smart.activities.AddLightsActivity;
import com.yankon.smart.activities.ChangePasswordActivity;
import com.yankon.smart.activities.LightInfoActivity;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.CardItemViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Evan on 14/11/26.
 */
public class LightsFragment extends BaseListFragment implements CompoundButton.OnCheckedChangeListener {

    private static boolean isFirstLaunch = true;

    private static final int REQUEST_EDIT_NAME = 0x2001;
    private static final int REQUEST_SET_PWD = 0x2002;
    private static final int REQUEST_TRANSER = 0x2003;

    private static final int SHOW_LOCAL = 0;
    private static final int SHOW_CONTROLLABLE = 1;  //local + remote pwd
    private static final int SHOW_ALL = 2;  // all even without pwd

    public static final int MENU_SET_REMOTE_PWD = 3;
    public static final int MENU_CHANGE_PWD = 4;
    public static final int MENU_RESET_NETWORK = 5;
    public static final int MENU_TRANSFER = 6;

    View headerView;
    ToggleButton multi_state_switch;

    boolean inMultipleMode = false;
    int mShowMode = SHOW_ALL;
    boolean needClearWhenSwitchMode = false;

    HashSet<String> mSelectedLights = new HashSet<>();
    String[] currLightIds = null;

    public static LightsFragment newInstance(int sectionNumber) {
        LightsFragment fragment = new LightsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.lights, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_select_all).setVisible(inMultipleMode);
        menu.findItem(R.id.action_multiple).setTitle(inMultipleMode ?
                R.string.action_single : R.string.action_multiple);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_mode: {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(R.string.action_show_mode);
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.setSingleChoiceItems(R.array.lights_show_modes, mShowMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mode = SHOW_ALL;
                        switch (which) {
                            case 0:
                                mode = SHOW_LOCAL;
                                break;
                            case 1:
                                mode = SHOW_CONTROLLABLE;
                                break;
                            case 2:
                                mode = SHOW_ALL;
                                break;
                        }
                        if (mode != mShowMode) {
                            mShowMode = mode;
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("default_show_mode", String.valueOf(mShowMode));
                            editor.commit();
                            needClearWhenSwitchMode = true;
                            getLoaderManager().restartLoader(LightsFragment.class.hashCode(), null, LightsFragment.this);/* Update UI */
                        }
                        dialog.dismiss();
                    }
                });
                ab.show();
            }
            break;
            case R.id.action_multiple: {
                inMultipleMode = !inMultipleMode;
                switchMode();
                if (inMultipleMode)
                    selectAll(false);
            }
            return true;
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddLightsActivity.class));
                return true;
            case R.id.action_select_all:
                selectAll(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void clearSelectionWhenSwitchMode() {
        HashSet<String> tmpSet = new HashSet<>();
        tmpSet.addAll(mSelectedLights);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            Cursor cursor = (Cursor) mAdapter.getItem(i);
            String lid = cursor.getString(cursor.getColumnIndex("_id"));
            if (tmpSet.contains(lid)) {
                tmpSet.remove(lid);
            }
        }
        mSelectedLights.removeAll(tmpSet);
        updateHeaderView();
    }

    void selectAll(boolean autoClear) {
        boolean isAllSelected = true;
        HashSet<String> tmpSet = new HashSet<>();
        tmpSet.addAll(mSelectedLights);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            Cursor cursor = (Cursor) mAdapter.getItem(i);
            String lid = cursor.getString(cursor.getColumnIndex("_id"));
            if (!mSelectedLights.contains(lid)) {
                isAllSelected = false;
                mSelectedLights.add(lid);
            } else {
                tmpSet.remove(lid);
            }
        }
        if (isAllSelected && autoClear) {
            mSelectedLights.clear();
        } else {
            if (tmpSet.size() > 0) {
                mSelectedLights.removeAll(tmpSet);
            }
        }
        updateHeaderView();
        getListView().invalidateViews();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderView();
    }

    void initHeaderView() {
        ListView lv = getListView();
        final View headerViewContainer = View.inflate(getActivity(), R.layout.lights_header, null);
        lv.addHeaderView(headerViewContainer);
        headerView = headerViewContainer.findViewById(R.id.header_view);
        View select = headerView.findViewById(R.id.icon);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll(true);
            }
        });
        multi_state_switch = (ToggleButton) headerView.findViewById(R.id.switch_button);
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
        info.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        headerViewContainer.showContextMenu();
                                    }
                                }

        );

        updateHeaderView();

        switchMode();
    }

    void updateHeaderView() {
        TextView tv = (TextView) headerView.findViewById(R.id.title);
        tv.setText(getString(R.string.header_selected_lights, mSelectedLights.size()));
    }

    void switchMode() {
        headerView.setVisibility(inMultipleMode ? View.VISIBLE : View.GONE);
        getActivity().invalidateOptionsMenu();
        getListView().invalidateViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mShowMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString("default_show_mode", "0"));
        } catch (Exception e) {
            mShowMode = SHOW_ALL;
        }

        mAdapter = new LightsAdapter(getActivity());
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(getClass().hashCode(), null, this);       /* Init LoaderManager */
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //"connected OR is_mine"
        String where = "deleted=0";
        switch (mShowMode) {
            case SHOW_LOCAL:
                where = "connected AND deleted=0";
                break;
            case SHOW_CONTROLLABLE:
                where = "deleted=0 AND (connected OR ifnull(length(remote_pwd), 0) = 4)";
                break;
            default:
                break;
        }
        return new CursorLoader(getActivity(), YanKonProvider.URI_LIGHTS, null, where, null,
                "connected desc, owned_time asc");
    }

    /*
    *   后台线程执行游标查询，不会影响UI
    */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (isFirstLaunch) {
            isFirstLaunch = false;
            int num = 0;
            Cursor c = getActivity().getContentResolver()
                    .query(YanKonProvider.URI_LIGHTS, new String[]{"_id"}, null, null, null);
            if (c != null) {
                num = c.getCount();
                c.close();
            }
            if (num == 0) {
                startActivity(new Intent(getActivity(), AddLightsActivity.class));
            }
        }
        if (needClearWhenSwitchMode) {
            needClearWhenSwitchMode = false;
            if (inMultipleMode)
                clearSelectionWhenSwitchMode();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String name = null;
        if (info.position == 0) {
            if (mSelectedLights.size() == 0) {
                Toast.makeText(getActivity(), R.string.lights_multiple_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSelectedLights.size() == 1) {
                name = getString(R.string.lights_multiple_title_1light);
            } else {
                name = getString(R.string.lights_multiple_title, mSelectedLights.size());
            }
            menu.add(0, MENU_SET_REMOTE_PWD, 0, R.string.set_remote_pwd);
        } else {
            Cursor cursor = (Cursor) mAdapter.getItem(info.position - 1);
            name = cursor.getString(cursor.getColumnIndex("name"));
            menu.add(0, MENU_EDIT, 0, R.string.menu_edit_name);
            if (TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remote")))) {
                menu.add(0, MENU_SET_REMOTE_PWD, 0, R.string.set_remote_pwd);
            } else {
                boolean connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
                menu.add(0, MENU_TRANSFER, 0, R.string.transfer_server).setEnabled(connected);
            }
        }
        menu.setHeaderTitle(name);
        menu.add(0, MENU_CHANGE_PWD, 0, R.string.change_password);
        menu.add(0, MENU_RESET_NETWORK, 0, R.string.action_addlights_unnetwork);
        menu.add(0, MENU_DELETE, 0, R.string.menu_delete);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateMutliState();
    }

    void showEditAction(String text) {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getActivity().getString(R.string.edit_light_name),
                text,
                null, null, 0);
        newFragment.setTargetFragment(this, REQUEST_EDIT_NAME);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void showSetPassword() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getString(R.string.input_remotepwd_title),
                null,
                getString(R.string.input_remotepwd_hint), null, InputDialogFragment.TYPE_REMOTE_PWD);
        newFragment.setTargetFragment(this, REQUEST_SET_PWD);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void showTransfer() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getString(R.string.transfer_title),
                null,
                getString(R.string.transfer_hint), null, 0);
        newFragment.setTargetFragment(this, REQUEST_TRANSER);
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int pos = info.position - 1;
        switch (item.getItemId()) {
            case MENU_EDIT: {
                Cursor cursor = (Cursor) mAdapter.getItem(pos);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                currentEditId = cursor.getInt(cursor.getColumnIndex("_id"));
                showEditAction(name);
            }
            break;
            case MENU_DELETE: {
                if (info.position == 0) {
                    for (String lid : mSelectedLights) {
                        DataHelper.deleteLightById(Integer.parseInt(lid));
                    }
                    mSelectedLights.clear();
                } else {
                    Cursor cursor = (Cursor) mAdapter.getItem(pos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    String mac = cursor.getString(cursor.getColumnIndex("MAC"));
                    DataHelper.deleteLightById(cid);
                    mSelectedLights.remove(mac);
                }
                updateHeaderView();
            }
            break;
            case MENU_SET_REMOTE_PWD:
                if (info.position == 0) {
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                } else {
                    Cursor cursor = (Cursor) mAdapter.getItem(pos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    currLightIds = new String[]{String.valueOf(cid)};
                }
                showSetPassword();
                break;
            case MENU_TRANSFER: {
                Cursor cursor = (Cursor) mAdapter.getItem(pos);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                currLightIds = new String[]{String.valueOf(cid)};
                showTransfer();
            }
            break;
            case MENU_CHANGE_PWD: {
                if (info.position == 0) {
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                } else {
                    Cursor cursor = (Cursor) mAdapter.getItem(pos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    currLightIds = new String[]{String.valueOf(cid)};
                }
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                intent.putExtra(ChangePasswordActivity.EXTRA_LIGHTS, currLightIds);
                startActivity(intent);
            }
            break;
            case MENU_RESET_NETWORK: {
                if (info.position == 0) {
                    currLightIds = mSelectedLights.toArray(new String[mSelectedLights.size()]);
                } else {
                    Cursor cursor = (Cursor) mAdapter.getItem(pos);
                    int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                    currLightIds = new String[]{String.valueOf(cid)};
                }
                Command cmd = new Command(Command.CommandType.CommandTypeResetWifi, 0);
                Utils.controlLightsById(App.getApp(), currLightIds, cmd, true);
                needClearWhenSwitchMode = true;
            }
            break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_EDIT_NAME: {
                String name = data.getStringExtra(InputDialogFragment.ARG_TEXT);
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("synced", false);
                if (currentEditId > -1) {
                    getActivity().getContentResolver()
                            .update(YanKonProvider.URI_LIGHTS, values, "_id=" + currentEditId,
                                    null);
                }
            }
            break;
            case REQUEST_SET_PWD: {
                String pwd = data.getStringExtra(InputDialogFragment.ARG_TEXT);
                if (pwd != null && pwd.length() == 4 && TextUtils.isDigitsOnly(pwd)) {
                    new SetRemotePwdTask(pwd).execute();
                } else {
                    Toast.makeText(getActivity(), R.string.remotepwd_prompt, Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case REQUEST_TRANSER: {
                String pwd = data.getStringExtra(InputDialogFragment.ARG_TEXT);
                new TransferTask(pwd).execute();
            }
            break;
        }
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
                Toast.makeText(getActivity(), R.string.kii_extension_return_null, Toast.LENGTH_SHORT).show();
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
                getActivity().getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "MAC in " + Utils.buildStringsInSQL(succMacs), null);
            }
            if (failList.size() > 0) {
                String[] failMacs = failList.toArray(new String[failList.size()]);
                ContentValues values = new ContentValues();
                values.put("remote_pwd", "");
                values.put("synced", false);
                getActivity().getContentResolver().update(YanKonProvider.URI_LIGHTS, values, "MAC in " + Utils.buildStringsInSQL(failMacs), null);
            }
            if (succList.size() > 0 && failList.size() == 0) {
                Toast.makeText(getActivity(), R.string.enable_remote_succ, Toast.LENGTH_SHORT).show();
            } else if (succList.size() == 0 && failList.size() > 0) {
                Toast.makeText(getActivity(), R.string.enable_remote_fail, Toast.LENGTH_SHORT).show();
            } else if (succList.size() > 0 && failList.size() > 0) {
                Toast.makeText(getActivity(), R.string.enable_remote_part_succ, Toast.LENGTH_SHORT).show();
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
            Cursor c = getActivity().getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"MAC"}, "_id=" + currLightIds[0], null, null);
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
                Toast.makeText(getActivity(), R.string.kii_extension_return_null, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), R.string.transfer_succ, Toast.LENGTH_SHORT).show();
                KiiSync.asyncFullSync();
            } else {
                Toast.makeText(getActivity(), R.string.transfer_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }


    void updateMutliState() {
        if (!inMultipleMode)
            return;
        if (mSelectedLights.size() == 0) {
            multi_state_switch.setChecked(false);
            return;
        }
        String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);

        Cursor c = getActivity().getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"sum(state)"},
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String name = null;
        Intent intent = new Intent(getActivity(), LightInfoActivity.class);
        if (position == 0) {
            if (mSelectedLights.size() == 0) {
                Toast.makeText(getActivity(), R.string.lights_multiple_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);
            if (lights.length == 1) {
                Cursor cursor = getActivity().getContentResolver().query(YanKonProvider.URI_LIGHTS,
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
                Cursor c = getActivity().getContentResolver().query(YanKonProvider.URI_LIGHTS, null,
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
                        }
                    }
                    c.close();
                }
            }
        } else {
            Cursor cursor = (Cursor) mAdapter.getItem(position - 1);
            name = cursor.getString(cursor.getColumnIndex("name"));
            intent.putExtra(LightInfoActivity.EXTRA_LIGHT_ID, (int) id);
        }
        intent.putExtra(LightInfoActivity.EXTRA_NAME, name);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String lid = (String) buttonView.getTag();
        if (buttonView.getId() == R.id.checkbox) {
            if (isChecked) {
                mSelectedLights.add(lid);
            } else {
                mSelectedLights.remove(lid);
            }
            updateHeaderView();
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
//            String modelName = cursor.getString(cursor.getColumnIndex("model"));
//            CardItemViewHolder holder = (CardItemViewHolder) view.getTag();
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            holder.title.setText(name);
//            String ver = cursor.getString(cursor.getColumnIndex("firmware_version"));
//            holder.label1.setText(R.string.model);
//            holder.textView1.setText(modelName + "\t[" + ver + "]");
//            boolean connected = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
//            String remotePwd = cursor.getString(cursor.getColumnIndex("remote_pwd"));
//            String mac = cursor.getString(cursor.getColumnIndex("MAC"));
//            boolean roaming = !TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remote")));
//
//            if (connected) {
//                holder.updateStatus(CardItemViewHolder.STATUS_LOCAL, roaming, remotePwd != null && remotePwd.length() == 4);
//                holder.label2.setText("IP");
//                int ip = cursor.getInt(cursor.getColumnIndex("IP"));
//                int subIP = cursor.getInt(cursor.getColumnIndex("subIP"));
//                String ipStr = Utils.intToIp(ip);
//                if (subIP == 0) {
//                    holder.textView2.setText(ipStr);
//                } else {
//                    holder.textView2.setText(ipStr + ", " + Utils.intToIp(subIP));
//                }
//            } else {
//                holder.updateStatus(CardItemViewHolder.STATUS_REMOTE, roaming, remotePwd != null && remotePwd.length() == 4);
//                holder.label2.setText("MAC");
//                holder.textView2.setText(Utils.formatMac(mac));
//            }
//
//            final boolean state = cursor.getInt(cursor.getColumnIndex("state")) > 0;
//            final int light_id = cursor.getInt(cursor.getColumnIndex("_id"));
//            String lid = String.valueOf(light_id);
//            if (inMultipleMode) {
//                holder.checkBox.setVisibility(View.VISIBLE);
//                holder.checkBox.setTag(lid);
//                holder.checkBox.setOnCheckedChangeListener(null);
//                holder.checkBox.setChecked(mSelectedLights.contains(lid));
//                holder.checkBox.setOnCheckedChangeListener(LightsFragment.this);
//            } else {
//                holder.checkBox.setVisibility(View.GONE);
//            }
//            holder.switchButton.setChecked(state);
//            holder.switchButton.setTag(lid);
//            holder.switchButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean isChecked = !state;
//                    Command cmd = new Command(Command.CommandType.CommandTypeState, isChecked ? 1 : 0);
//                    Utils.controlLight(getActivity(), light_id, cmd, true);
//                }
//            });
//            holder.infoButton.setVisibility(View.VISIBLE);
//            holder.infoButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    view.showContextMenu();
//                }
//            });
        }

    }
}
