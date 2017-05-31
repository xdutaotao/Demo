package com.yankon.smart.activities;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.SyncUITask;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.UUID;


public class AddScheduleActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_SCHEDULE_ID = "schedule_id";

    private static final int REQUEST_SETTINGS = 0x1001;
    private static final int REQUEST_TARGET = 0x1002;
    private static final int REQUEST_REPEAT = 0x1003;
    EditText mNameField;
    int schedule_id = -1;
    int color = Constants.DEFAULT_COLOR;
    int brightness = Constants.DEFAULT_BRIGHTNESS;
    int CT = Constants.DEFAULT_CT;
    int mode = Constants.DEFAULT_MODE;
    int state = 1;
    int time = 0;
    int type = -1;
    int closeAll = 0;
    String targetId = null;
    Button mTargetBtn, mSettingsBtn, mRepeatBtn;
    TimePicker mTimePicker;
    View settingsRow, settingsRow2;
    boolean[] repeatDays = new boolean[7];
    RadioGroup closeAllGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initAcitivityUI();
        mTargetBtn = (Button) findViewById(R.id.target_btn);
        mTargetBtn.setOnClickListener(this);
        mSettingsBtn = (Button) findViewById(R.id.settings_btn);
        mSettingsBtn.setOnClickListener(this);
        mRepeatBtn = (Button) findViewById(R.id.repeat_btn);
        mRepeatBtn.setOnClickListener(this);
        settingsRow = findViewById(R.id.settings_row);
        settingsRow2 = findViewById(R.id.settings2_row);
        findViewById(R.id.schedule_cancel).setOnClickListener(this);
        findViewById(R.id.schedule_ok).setOnClickListener(this);
        mNameField = (EditText) findViewById(R.id.schedule_name);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        closeAllGroup = (RadioGroup) findViewById(R.id.scene_setting);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        schedule_id = getIntent().getIntExtra(EXTRA_SCHEDULE_ID, -1);
        if (schedule_id >= 0) {
            Cursor c = getContentResolver().query(YanKonProvider.URI_SCHEDULE, null, "_id=" + schedule_id, null, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex("name"));
                    mNameField.setText(name);
                    color = c.getInt(c.getColumnIndex("color"));
                    brightness = c.getInt(c.getColumnIndex("brightness"));
                    CT = c.getInt(c.getColumnIndex("CT"));
                    state = c.getInt(c.getColumnIndex("state"));
                    mode = c.getInt(c.getColumnIndex("mode"));
                    time = c.getInt(c.getColumnIndex("time"));
                    hour = time / 60;
                    minute = time % 60;
                    closeAll = c.getInt(c.getColumnIndex("closeAll"));
                    String repeat = c.getString(c.getColumnIndex("repeat"));
                    try {
                        JSONArray arr = new JSONArray(repeat);
                        for (int i = 0; i < 7; i++) {
                            repeatDays[i] = arr.optBoolean(i, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    targetId = c.getString(c.getColumnIndex("light_id"));
                    type = -1;
                    if (!TextUtils.isEmpty(targetId)) {
                        type = 0;
                    }
                    if (type < 0) {
                        targetId = c.getString(c.getColumnIndex("group_id"));
                        if (!TextUtils.isEmpty(targetId)) {
                            type = 1;
                        }
                    }
                    if (type < 0) {
                        targetId = c.getString(c.getColumnIndex("scene_id"));
                        if (!TextUtils.isEmpty(targetId)) {
                            type = 2;
                        }
                    }
                }
                c.close();
            }
        }
        updateSettingsInfo();
        updateTargetInfo();
        updateRepeatInfo();
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

    public void updateSettingsInfo() {
        mSettingsBtn.setTextColor(color);
        mSettingsBtn.setText(getString(R.string.schedule_settings_format,
                state == 0 ? getString(R.string.state_off) : getString(R.string.state_on), brightness, CT));
        closeAllGroup.check(closeAll == 0 ? R.id.light_apply : R.id.light_close);
    }

    public void updateTargetInfo() {
        String name = getString(R.string.none_target);
        Cursor cursor = null;
        switch (type) {
            case 0:
                cursor = getContentResolver().query(YanKonProvider.URI_LIGHTS, new String[]{"name"}, "MAC=(?)", new String[]{targetId}, null);
                break;
            case 1:
                cursor = getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, new String[]{"name"}, "objectID=(?)", new String[]{targetId}, null);
                break;
            case 2:
                cursor = getContentResolver().query(YanKonProvider.URI_SCENES, new String[]{"name"}, "objectID=(?)", new String[]{targetId}, null);
                break;
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
            cursor.close();
        }
        mTargetBtn.setText(name);
        settingsRow.setVisibility(type == 2 ? View.GONE : View.VISIBLE);
        settingsRow2.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
    }

    public void updateRepeatInfo() {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        String[] dayName = getResources().getStringArray(R.array.short_days);
        if (repeatDays != null) {
            for (int i = 0; i < 7; i++) {
                if (repeatDays[i]) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                     }
                    sb.append(dayName[i]);
                    count++;
                }
            }
        }
        if (count == 0) {
            mRepeatBtn.setText(R.string.repeat_none);
        } else if (count == 7) {
            mRepeatBtn.setText(R.string.repeat_everyday);
        } else {
            mRepeatBtn.setText(sb.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.target_btn: {
                Intent intent = new Intent(this, PickTargetActivity.class);
                startActivityForResult(intent, REQUEST_TARGET);
            }
            break;
            case R.id.settings_btn: {
                Intent intent = new Intent(this, LightInfoActivity.class);
                intent.putExtra("state", state > 0);
                intent.putExtra("color", color);
                intent.putExtra("brightness", brightness);
                intent.putExtra("CT", CT);
                intent.putExtra("mode", mode);
                intent.putExtra(LightInfoActivity.EXTRA_RETURN, true);
                startActivityForResult(intent, REQUEST_SETTINGS);
            }
            break;
            case R.id.repeat_btn: {
                Intent intent = new Intent(this, RepeatDaysActivity.class);
                intent.putExtra("days", repeatDays);
                startActivityForResult(intent, REQUEST_REPEAT);
            }
            break;
            case R.id.schedule_cancel:
                finish();
                break;
            case R.id.schedule_ok:
                save();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SETTINGS) {
                state = data.getBooleanExtra("state", true) ? 1 : 0;
                color = data.getIntExtra("color", Constants.DEFAULT_COLOR);
                brightness = data.getIntExtra("brightness", Constants.DEFAULT_BRIGHTNESS);
                CT = data.getIntExtra("CT", Constants.DEFAULT_CT);
                mode = data.getIntExtra("mode", Constants.DEFAULT_MODE);
                updateSettingsInfo();
            } else if (requestCode == REQUEST_TARGET) {
                type = data.getIntExtra("type", 0);
                targetId = data.getStringExtra("id");
                updateTargetInfo();
            } else if (requestCode == REQUEST_REPEAT) {
                repeatDays = data.getBooleanArrayExtra("days");
                updateRepeatInfo();
            }
        }
    }

    private void save() {
        String gName = mNameField.getText().toString().trim();
        if (TextUtils.isEmpty(gName)) {
            Toast.makeText(this, R.string.empty_schedule_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (type < 0) {
            Toast.makeText(this, R.string.empty_schedule_target, Toast.LENGTH_SHORT).show();
            return;
        }
        closeAll = (closeAllGroup.getCheckedRadioButtonId() == R.id.light_close) ? 1 : 0;
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", gName);
        values.put("synced", false);
        values.put("color", color);
        values.put("brightness", brightness);
        values.put("CT", CT);
        values.put("state", state);
        values.put("mode", mode);
        values.put("closeAll", closeAll);
        values.put("enabled", true);
        values.put("light_id", "");
        values.put("group_id", "");
        values.put("scene_id", "");
        switch (type) {
            case 0:
                values.put("light_id", targetId);
                break;
            case 1:
                values.put("group_id", targetId);
                break;
            case 2:
                values.put("scene_id", targetId);
                break;
        }
        try {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < 7; i++) {
                if (repeatDays != null && repeatDays[i]) {
                    arr.put(true);
                } else {
                    arr.put(false);
                }
            }
            values.put("repeat", arr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        values.put("time", mTimePicker.getCurrentHour() * 60 + mTimePicker.getCurrentMinute());
        if (schedule_id < 0) {
            values.put("objectID", UUID.randomUUID().toString());
            values.put("created_time", System.currentTimeMillis());
            Uri uri = cr.insert(YanKonProvider.URI_SCHEDULE, values);
            schedule_id = Integer.parseInt(uri.getLastPathSegment());
        } else {
            cr.update(YanKonProvider.URI_SCHEDULE, values, "_id=" + schedule_id, null);
        }
        new SyncTask(getFragmentManager(), getString(R.string.saving)).execute();
    }

    class SyncTask extends SyncUITask {
        SyncTask(FragmentManager fm, String msg) {
            super(fm, msg);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!checkIfSyncSucc()) {
                Toast.makeText(App.getApp(), R.string.sync_schedule_failed, Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
        }
    }
}
