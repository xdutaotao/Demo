package com.yankon.smart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Settings;
import com.yankon.smart.utils.SyncUITask;

/**
 * Created by guzhenfu on 2015/8/21.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView email;
    private Button logOutButton, syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        initTitleView();
        email = (TextView) findViewById(R.id.email);
        logOutButton = (Button) findViewById(R.id.log_out_button);
        syncButton = (Button) findViewById(R.id.sync_button);

        String site = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("KII_SITE", null);
        String server = null;
        if (!TextUtils.isEmpty(site)) {
            if (site.equals("US")) {
                server = getString(R.string.server_us);
            } else if (site.equals("JP")) {
                server = getString(R.string.server_jp);
            } else if (site.equals("SG")) {
                server = getString(R.string.server_sg);
            } else if (site.equals("CN3")) {
                server = getString(R.string.server_cn);
            }
        }
        String emailDisplay = Settings.getEmail();
        if (!TextUtils.isEmpty(server))
            emailDisplay = emailDisplay + " (" + server + ")";
        email.setText(emailDisplay);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.logOut();
                LogUtils.d("ProfileFragment", "after log out, is logged in? " + Settings.isLoggedIn());
                LogUtils.d("ProfileFragment", "after log out, token is " + Settings.getToken());
                LocalBroadcastManager.getInstance(ProfileActivity.this).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_OUT));
                finish();
            }
        });
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncUITask(getFragmentManager(), getString(R.string.syncing)).execute();
            }
        });

    }

    public void initTitleView() {
        View back = findViewById(R.id.back_layout);
        back.setOnClickListener(this);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.log_in));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
