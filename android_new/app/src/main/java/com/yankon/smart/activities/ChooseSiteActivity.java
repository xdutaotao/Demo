package com.yankon.smart.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.MainActivity;
import com.yankon.smart.R;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;

import java.util.UUID;

public class ChooseSiteActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_site);
        findViewById(R.id.btn_us).setOnClickListener(this);
        findViewById(R.id.btn_sg).setOnClickListener(this);
        findViewById(R.id.btn_jp).setOnClickListener(this);
        findViewById(R.id.btn_cn3).setOnClickListener(this);

        //addDefaultGroupItem();
        //addDefaultSceneItem();
    }

    private void addDefaultSceneItem() {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "Default1");
        values.put("synced",false);
        values.put("objectID", UUID.randomUUID().toString());
        values.put("created_time", System.currentTimeMillis());
        cr.insert(YanKonProvider.URI_SCENES, values);

        ContentResolver cr2 = getContentResolver();
        ContentValues values2 = new ContentValues();
        values2.put("name", "Default1");
        values2.put("synced",false);
        values2.put("objectID", UUID.randomUUID().toString());
        values2.put("created_time", System.currentTimeMillis());
        cr2.insert(YanKonProvider.URI_SCENES, values2);

        ContentResolver cr3 = getContentResolver();
        ContentValues values3 = new ContentValues();
        values3.put("name", "Default1");
        values3.put("synced",false);
        values3.put("objectID", UUID.randomUUID().toString());
        values3.put("created_time", System.currentTimeMillis());
        cr3.insert(YanKonProvider.URI_SCENES, values3);
    }

    private void addDefaultGroupItem() {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "Default1");
        values.put("synced", false);
        values.put("objectID", UUID.randomUUID().toString());
        values.put("brightness", Constants.DEFAULT_BRIGHTNESS);
        values.put("CT", Constants.DEFAULT_CT);
        values.put("color", Constants.DEFAULT_COLOR);
        values.put("created_time", System.currentTimeMillis());
        cr.insert(YanKonProvider.URI_LIGHT_GROUPS, values);

        ContentResolver cr2 = getContentResolver();
        ContentValues values2 = new ContentValues();
        values2.put("name", "Default2");
        values2.put("synced", false);
        values2.put("objectID", UUID.randomUUID().toString());
        values2.put("brightness", Constants.DEFAULT_BRIGHTNESS);
        values2.put("CT", Constants.DEFAULT_CT);
        values2.put("color", Constants.DEFAULT_COLOR);
        values2.put("created_time", System.currentTimeMillis());
        cr2.insert(YanKonProvider.URI_LIGHT_GROUPS, values2);

        ContentResolver cr3 = getContentResolver();
        ContentValues values3 = new ContentValues();
        values3.put("name", "Default3");
        values3.put("synced", false);
        values3.put("objectID", UUID.randomUUID().toString());
        values3.put("brightness", Constants.DEFAULT_BRIGHTNESS);
        values3.put("CT", Constants.DEFAULT_CT);
        values3.put("color", Constants.DEFAULT_COLOR);
        values3.put("created_time", System.currentTimeMillis());
        cr3.insert(YanKonProvider.URI_LIGHT_GROUPS, values3);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_us:
                chooseServer("US");
                break;
            case R.id.btn_jp:
                chooseServer("JP");
                break;
            case R.id.btn_sg:
                chooseServer("SG");
                break;
            case R.id.btn_cn3:
                chooseServer("CN3");
                break;
        }
    }

    void chooseServer(String site) {
        SharedPreferences.Editor et = PreferenceManager.getDefaultSharedPreferences(this).edit();
        et.putString("KII_SITE", site);
        et.commit();
        Global.init(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
