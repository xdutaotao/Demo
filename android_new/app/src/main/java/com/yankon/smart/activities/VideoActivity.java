package com.yankon.smart.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.tutk.TUTKVideoActivity;
import com.yankon.smart.App;
import com.yankon.smart.BaseListActivity;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.R;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.NiftyDialogBuilder;
import com.yankon.smart.widget.VideoItemViewHolder;

/**
 * Created by guzhenfu on 2015/11/19.
 */
public class VideoActivity extends BaseListActivity {
    private static boolean isFirstLaunch = true;
    private NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        initTitleView();
        mAdapter = new VideosAdapter(this);
        initListView();
        getListView().setOnItemClickListener(this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String where = "UID is not null and deleted=0";
//        String where = "group by UID";
        return new CursorLoader(this, YanKonProvider.URI_LIGHTS, null, where, null,
                "connected desc, owned_time asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if ((mAdapter.getCount() == 1) && (isFirstLaunch)){
            goToVideoInfo(0);
            isFirstLaunch = false;
            if (dialogBuilder != null)
                dialogBuilder.dismiss();
        }else if (mAdapter.getCount() == 0){
            dialogBuilder = NiftyDialogBuilder.getInstance(this);
            dialogBuilder
                    .withMessage(R.string.find_no_video)
                    .withTitle(null)                                  //.withTitle(null)  no title
                    .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                    .withDuration(300)                                          //def
                    .withButton1Text(getString(android.R.string.ok))
                    .withButton2Text(getString(android.R.string.cancel))
                    .setButton1Click(v ->  {
                        dialogBuilder.dismiss();
                        if (dialogBuilder != null)
                            dialogBuilder = null;
                        finish();
                    })
                    .setButton2Click(v1 -> {
                        dialogBuilder.dismiss();
                        if (dialogBuilder != null)
                            dialogBuilder = null;
                        finish();
                    })
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        goToVideoInfo(i);
    }

    private void goToVideoInfo(int i){
        if(Utils.isNetworkConnected()) {
            Intent intent = null;
            cursor = (Cursor) mAdapter.getItem(i);
            String uid = cursor.getString(cursor.getColumnIndex("UID"));
            intent = new Intent(this, TUTKVideoActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
        else
            Toast.makeText(this, getString(R.string.check_net), Toast.LENGTH_LONG).show();
    }

    @Override
    public void initTitleView() {
        super.initTitleView();
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.video));
    }

    class VideosAdapter extends CursorAdapter {

        public VideosAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(VideoItemViewHolder.layout_id, parent, false);
            VideoItemViewHolder holder = new VideoItemViewHolder(view);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            VideoItemViewHolder holder = (VideoItemViewHolder) view.getTag();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            holder.name.setText(name);
            boolean remote = cursor.getInt(cursor.getColumnIndex("connected")) > 0;
            holder.remoteView.setVisibility(remote ? View.GONE : View.VISIBLE);
        }
    }

}
