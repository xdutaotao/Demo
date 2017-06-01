package com.kii.tutk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = GridViewActivity.class.getSimpleName();

    GridView mGridView = null;
    GridViewAdapter mGridViewAdapter = null;

    private DisplayMetrics dm = new DisplayMetrics();
    public static int mGridViewWidth = 0;

    List<String> mUIDList = new ArrayList<String>();

    static final String UID = "D7C991NEJE2FBH6PWFZ1";
    static final String UID1 = "UUKBN9Z2VM873KTD111A";

    Thread IOTCThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mGridViewWidth = dm.widthPixels/2-10;

        mUIDList.add(UID);
        mUIDList.add(UID1);

        mGridView = (GridView)findViewById(R.id.grid);

        mGridViewAdapter = new GridViewAdapter(GridViewActivity.this);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startInternal();

    }

    @Override
    protected void onPause() {
        super.onPause();

        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String UID = mUIDList.get(position);
        Intent intent = new Intent(this, TUTKVideoActivity.class);
        intent.putExtra(TUTKVideoActivity.EXTRA_UID, UID);
        startActivity(intent);
    }

    class ViewHolder{
        GridViewItemView surfaceView;
    }

    class GridViewAdapter extends BaseAdapter{

        private Context context;

        public GridViewAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return mUIDList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;


            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_grid_view, null);
                holder.surfaceView = (GridViewItemView)convertView.findViewById(R.id.surface);
                convertView.setLayoutParams(
                        new GridView.LayoutParams(mGridViewWidth, mGridViewWidth));
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.surfaceView.init(mUIDList.get(position));

            return convertView;
        }
    }

//    public void start() {
//        IOTCThread = new Thread(new Runnable(){
//            public void run() {
//                startInternal();
//            }
//        });
//        IOTCThread.start();
//
//    }

    private void startInternal() {

        System.out.println("StreamClient start...");
        // use which Master base on location, port 0 means to get a random port
        int ret = IOTCAPIs.IOTC_Initialize(0, "m1.iotcplatform.com",
                "m2.iotcplatform.com", "m4.iotcplatform.com",
                "m5.iotcplatform.com");
        System.out.printf("IOTC_Initialize() ret = %d\n", ret);
        if (ret == IOTCAPIs.IOTC_ER_ALREADY_INITIALIZED) {
            IOTCAPIs.IOTC_DeInitialize();
            ret = IOTCAPIs.IOTC_Initialize(0, "m1.iotcplatform.com",
                    "m2.iotcplatform.com", "m4.iotcplatform.com",
                    "m5.iotcplatform.com");
            System.out.printf("After DeInitialize, IOTC_Initialize() ret = %d\n", ret);
        }
        if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
            Log.e(LOG_TAG, "IOTCAPIs_Device exit...!!");
            return;
        }

        AVAPIs.avInitialize(mUIDList.size());
        mGridViewAdapter.notifyDataSetChanged();


    }
}
