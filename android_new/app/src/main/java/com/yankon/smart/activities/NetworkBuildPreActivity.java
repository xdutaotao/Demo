package com.yankon.smart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;

/**
 * Created by guzhenfu on 2015/10/28.
 */
public class NetworkBuildPreActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netwok_build_pre);
        initLayout();
        Button mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(this);
    }

    private void initLayout() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        View backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.action_addlights_network));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                startActivity(new Intent(this, NetworkBuildActivity.class));
                finish();
                break;

            case R.id.back:
            case R.id.back_layout:
                finish();
                break;
        }
    }


}
