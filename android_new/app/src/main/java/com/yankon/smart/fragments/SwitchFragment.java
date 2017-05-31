package com.yankon.smart.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yankon.smart.R;
import com.yankon.smart.activities.AddSwitchsActivity;
import com.yankon.smart.activities.SwitchInfoActivity;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.SwitchItemViewHolder;

import java.util.HashSet;

/**
 * Created by guzhenfu on 2015/8/3.
 */
public class SwitchFragment extends BaseListFragment {
    private static boolean isFirstLaunch = true;

    private static final int SHOW_LOCAL = 0;
    private static final int SHOW_CONTROLLABLE = 1;  //local + remote pwd
    private static final int SHOW_ALL = 2;  // all even without pwd

    View headerView;
    ToggleButton multi_state_switch;

    boolean inMultipleMode = false;
    int mShowMode = SHOW_ALL;
    boolean needClearWhenSwitchMode = false;

    HashSet<String> mSelectedLights = new HashSet<>();
    String name = null;
    String model = null;

    public static SwitchFragment newInstance(int sectionNumber){
        SwitchFragment fragment = new SwitchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.switchs, menu);
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
                            editor.putString("default_switch_show_mode", String.valueOf(mShowMode));
                            editor.commit();
                            needClearWhenSwitchMode = true;
                            getLoaderManager().restartLoader(SwitchFragment.class.hashCode(), null, SwitchFragment.this);/* Update UI */
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
                startActivity(new Intent(getActivity(), AddSwitchsActivity.class));
                return true;
            case R.id.action_select_all:
                selectAll(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderView();
    }

    private void initHeaderView(){
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
//                                                      ToggleButton cb = (ToggleButton) v;
//                                                      String[] lights = mSelectedLights.toArray(new String[mSelectedLights.size()]);
//                                                      Command cmd = new Command(Command.CommandType.CommandTypeState, cb.isChecked() ? 1 : 0);
//                                                      Utils.controlLightsById(App.getApp(), lights, cmd, true);
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

    private void selectAll(boolean autoClear) {
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

    void updateHeaderView() {
        TextView tv = (TextView) headerView.findViewById(R.id.title);
        tv.setText(getString(R.string.header_selected_switchs, mSelectedLights.size()));
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

        mAdapter = new SwitchAdapter(getActivity());
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(getClass().hashCode(), null, this);       /* Init LoaderManager */
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        return new CursorLoader(getActivity(), YanKonProvider.URI_SWITCHS, null, where, null,
                "connected desc, owned_time asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (isFirstLaunch) {
            isFirstLaunch = false;
            int num = 0;
            Cursor c = getActivity().getContentResolver()
                    .query(YanKonProvider.URI_SWITCHS, new String[]{"_id"}, null, null, null);
            if (c != null) {
                num = c.getCount();
                c.close();
            }
            if (num == 0) {
                startActivity(new Intent(getActivity(), AddSwitchsActivity.class));
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), SwitchInfoActivity.class);
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
                        intent.putExtra(SwitchInfoActivity.EXTRA_LIGHT_ID, Integer.parseInt(lights[0]));
                    }
                    cursor.close();
                }
            } else {
                name = getString(R.string.lights_multiple_title, mSelectedLights.size());
                intent.putExtra(SwitchInfoActivity.EXTRA_LIGHTS, lights);
                Cursor c = getActivity().getContentResolver().query(YanKonProvider.URI_LIGHTS, null,
                        "_id in " + Utils.buildNumsInSQL(lights), null, null);
                if (c != null) {
                    boolean isFirst = true;
                    int state = 0, color = 0, brightness = 0, CT = 0, mode = 0;

                    while (c.moveToNext()) {
                        if (isFirst) {
                            isFirst = false;
                            state = c.getInt(c.getColumnIndex("state"));
                            mode = c.getInt(c.getColumnIndex("mode"));
                            intent.putExtra("state", state > 0);
                            intent.putExtra("mode", mode);
                            intent.putExtra("model", model);
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
            String modelName = cursor.getString(cursor.getColumnIndex("model"));
            intent.putExtra(SwitchInfoActivity.EXTRA_MODEL, modelName);
            intent.putExtra(SwitchInfoActivity.EXTRA_LIGHT_ID, (int) id);
        }
        intent.putExtra(SwitchInfoActivity.EXTRA_NAME, name);
        startActivity(intent);
    }

    class SwitchAdapter extends CursorAdapter {

        public SwitchAdapter(Context context) {
            super(context, null, 0);
        }

        /*
        *   数据变化的时候调用，但是重绘的时候不会
        */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(SwitchItemViewHolder.layout_id, parent, false);
            SwitchItemViewHolder holder = new SwitchItemViewHolder(view);
            view.setTag(holder);
            return view;
        }

        /*
        *   在重绘的时候会调用
        */
        @Override
        public void bindView(final View view, Context context, Cursor cursor) {
            SwitchItemViewHolder holder = (SwitchItemViewHolder) view.getTag();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            holder.title.setText(name);
        }
    }



}
