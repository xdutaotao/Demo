package com.yankon.smart.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yankon.smart.R;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guzhenfu on 2015/8/19.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Bind(R.id.back_layout)
    RelativeLayout backLayout;
    private ListPreference mSyncConfigPref;

    private SharedPreferences mPref;

    private String[] strings;

    private int mDownX, mDownY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.addPreferencesFromResource(R.xml.preferences);

        initView();
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSyncConfigPref = (ListPreference) findPreference("win_policy");
        setupSyncPolicy();

        strings = getResources().getStringArray(R.array.sync_policies);
    }

    private void initView() {
        View back = findViewById(R.id.back_layout);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            ImageView add = (ImageView) findViewById(R.id.add);
            add.setVisibility(View.INVISIBLE);
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(getString(R.string.action_settings));
        }

    }

    private void setupSyncPolicy() {
        int winPolicyIndex = 0;
        try {
            winPolicyIndex = Integer.parseInt(mSyncConfigPref.getValue());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mSyncConfigPref.setSummary(getResources().getStringArray(R.array.sync_policies)[winPolicyIndex]);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        setupSyncPolicy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                LogUtils.i("MOVE", mDownX + "----" + event.getX() + "MOVE---------" + mDownY + "----" + event.getY());
                if (event.getX() - mDownX > Global.X_DISTANCE && Math.abs(event.getY() - mDownY) < Global.Y_DISTANCE)
                    finish();
                break;

        }
        return super.onTouchEvent(event);
    }

}
