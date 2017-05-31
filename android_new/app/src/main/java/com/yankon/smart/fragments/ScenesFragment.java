package com.yankon.smart.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yankon.smart.App;
import com.yankon.smart.R;
import com.yankon.smart.activities.AddScenesActivity;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.DataHelper;
import com.yankon.smart.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Evan on 14/11/26.
 */
public class ScenesFragment extends BaseListFragment {

    public static ScenesFragment newInstance(int sectionNumber) {
        ScenesFragment fragment = new ScenesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddScenesActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ScenesAdapter(getActivity());
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(getClass().hashCode(), null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), YanKonProvider.URI_SCENES, null, "deleted=0", null, "created_time desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        Cursor cursor = (Cursor) mAdapter.getItem(position);
//        String name = cursor.getString(cursor.getColumnIndex("name"));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Cursor cursor = (Cursor) mAdapter.getItem(info.position);
        String name = cursor.getString(cursor.getColumnIndex("name"));
        menu.setHeaderTitle(name);
        menu.add(0, MENU_EDIT, 0, R.string.menu_edit);
        menu.add(0, MENU_DELETE, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case MENU_EDIT: {
                Cursor cursor = (Cursor) mAdapter.getItem(info.position);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Intent intent = new Intent(getActivity(), AddScenesActivity.class);
                intent.putExtra(AddScenesActivity.EXTRA_SCENE_NAME, name);
                intent.putExtra(AddScenesActivity.EXTRA_SCENE_ID, cid);
                startActivity(intent);
            }
            break;
            case MENU_DELETE: {
                Cursor cursor = (Cursor) mAdapter.getItem(info.position);
                int cid = cursor.getInt(cursor.getColumnIndex("_id"));
                DataHelper.deleteSceneById(cid);
            }
            break;
        }
        return true;
    }

    class ScenesAdapter extends CursorAdapter {
        DateFormat dateFormat;

        public ScenesAdapter(Context context) {
            super(context, null, 0);
            dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.scene_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(name);
//            int num = cursor.getInt(cursor.getColumnIndex("num"));
            tv = (TextView) view.findViewById(android.R.id.text2);
            long last_used_time = cursor.getLong(cursor.getColumnIndex("last_used_time"));
            if (last_used_time == 0) {
                tv.setText(context.getString(R.string.never_used));
            } else {
                Date date = new Date(last_used_time);
                tv.setText(getString(R.string.scene_last_used, dateFormat.format(date)));
            }
//            tv.setText(context.getString(R.string.group_num_format, num));
            final int sid = cursor.getInt(cursor.getColumnIndex("_id"));
            View icon = view.findViewById(R.id.light_icon);
            icon.setBackgroundResource(R.drawable.scene);
            Button applyBtn = (Button) view.findViewById(R.id.light_apply);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.controlScene(App.getApp(), sid, v.getId() == R.id.light_close, true);
                }
            };
            applyBtn.setOnClickListener(listener);
            Button closeBtn = (Button) view.findViewById(R.id.light_close);
            closeBtn.setOnClickListener(listener);
        }
    }
}
