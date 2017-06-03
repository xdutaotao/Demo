package com.yankon.smart.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gc.materialdesign.views.Slider;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.model.Command;
import com.yankon.smart.providers.YanKonObserver;
import com.yankon.smart.providers.YanKonProvider;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Utils;
import com.yankon.smart.widget.OnColorChangedListener;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 灯信息界面
 */
public class LightInfoActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, OnColorChangedListener {

    public static final String EXTRA_LIGHT_ID = "light_id";
    public static final String EXTRA_GROUP_ID = "group_id";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_LIGHTS = "lights";
    public static final String EXTRA_RETURN = "return_data";
    public static final int DATA_CHANGE = 4;

    int light_id = -1;
    int group_id = -1;
    String mName;
    String[] lights;
    boolean isForReturnValue;

    ToggleButton mSwitch;
    Slider mBrightnessSeekBar;

    TextView titleText;
    EditText type;
    EditText number;
    ToggleButton emulator;
    Slider light;
    LinearLayout settingLayout;
    Button send;
    Toolbar toolbarActionbar;
    Slider seekbarBrightness;
    ToggleButton lightSwitch;
    @Bind(R.id.run_modle)
    ToggleButton runModle;

    private int color;
    private int brightness;
    private int CT;
    private boolean state;
    private int mode;
    int displayMode;

    DelayHandler mDelayHandler;
    private YanKonObserver observer;
    public static boolean FLAG = false;

    private int lightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_info);
        ButterKnife.bind(this);
        initActivityUI();

        titleText = (TextView) findViewById(R.id.title_text);
        toolbarActionbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        type = (EditText) findViewById(R.id.type);
        number = (EditText) findViewById(R.id.number);
        emulator = (ToggleButton) findViewById(R.id.emulator);
        light = (Slider) findViewById(R.id.light);
        settingLayout = (LinearLayout) findViewById(R.id.setting_layout);
        send = (Button) findViewById(R.id.send);
        seekbarBrightness = (Slider) findViewById(R.id.seekbar_brightness);
        lightSwitch = (ToggleButton) findViewById(R.id.light_switch);


        light_id = getIntent().getIntExtra(EXTRA_LIGHT_ID, -1);
        group_id = getIntent().getIntExtra(EXTRA_GROUP_ID, -1);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        lights = getIntent().getStringArrayExtra(EXTRA_LIGHTS);
        if (!TextUtils.isEmpty(mName)) {
            setTitle(mName);
        }

        mDelayHandler = new DelayHandler(this);

        Cursor c = null;
        if (!getIntent().hasExtra("state")) {
            if (light_id > 0) {
                c = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "_id=" + light_id, null, null);
            } else if (group_id > 0) {
                c = getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, null, "_id=" + group_id, null, null);
            }
        }

        if (c != null) {
            if (c.moveToFirst()) {
                color = c.getInt(c.getColumnIndex("color"));
                brightness = c.getInt(c.getColumnIndex("brightness"));
                if (brightness <= SWDefineConst.minbri) {
                    brightness = 0;
                }
                CT = c.getInt(c.getColumnIndex("CT"));
                mode = c.getInt(c.getColumnIndex("mode"));
                if (light_id > 0) {
                    state = c.getInt(c.getColumnIndex("state")) != 0;
                } else if (group_id > 0) {
                    state = c.getInt(c.getColumnIndex("num")) == c.getInt(c.getColumnIndex("on_num"));
                }
                if (c.getInt(c.getColumnIndex("type")) != 0)
                    type.setText(String.valueOf(c.getInt(c.getColumnIndex("type"))));

                if (c.getInt(c.getColumnIndex("number")) != 0)
                    number.setText(String.valueOf(c.getInt(c.getColumnIndex("number"))));

                if (c.getInt(c.getColumnIndex("sens")) != 0)
                    emulator.setChecked(c.getInt(c.getColumnIndex("sens")) == 0x80);

                if (c.getInt(c.getColumnIndex("lux")) != 0)
                    light.setValue(c.getInt(c.getColumnIndex("lux")));
            }
            c.close();
        } else {
            state = getIntent().getBooleanExtra("state", true);
            color = getIntent().getIntExtra("color", Constants.DEFAULT_COLOR);
            brightness = getIntent().getIntExtra("brightness", Constants.DEFAULT_BRIGHTNESS);
            CT = getIntent().getIntExtra("CT", Constants.DEFAULT_CT);
            mode = getIntent().getIntExtra("mode", Constants.DEFAULT_MODE);

            type.setText(String.valueOf(getIntent().getIntExtra("type", Constants.DEFAULT_MODE)));
            number.setText(String.valueOf(getIntent().getIntExtra("number", Constants.DEFAULT_MODE)));
            emulator.setChecked(getIntent().getIntExtra("sens", Constants.DEFAULT_MODE) == 0x80);
            light.setValue(getIntent().getIntExtra("lux", Constants.DEFAULT_MODE));
        }
        isForReturnValue = getIntent().getBooleanExtra(EXTRA_RETURN, false);

        mSwitch = (ToggleButton) findViewById(R.id.light_switch);
        mSwitch.setChecked(state);
        mSwitch.setOnCheckedChangeListener(this);
        mBrightnessSeekBar = (Slider) findViewById(R.id.seekbar_brightness);
        mBrightnessSeekBar.setValue(brightness);
        mBrightnessSeekBar.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                brightness = i;
                mDelayHandler.sendEmptyMessage(DelayHandler.MSG_CHANGE_BRIGHTNESS);
            }
        });

        if (mode < 0 || mode > 1) {
            mode = Constants.DEFAULT_MODE;
        }

        displayMode = mode;
        observer = new YanKonObserver(mDelayHandler);
        getContentResolver().registerContentObserver(YanKonProvider.URI_LIGHTS, true, observer);
        FLAG = true;


        type.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        number.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        showToolbarBack(toolbarActionbar, titleText, "设置");
        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingLayout.getVisibility() == View.VISIBLE) {
                    settingLayout.setVisibility(View.GONE);
                } else {
                    settingLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        light.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                lightValue = value;
                mDelayHandler.sendEmptyMessage(DelayHandler.MSG_CHANGE_LIGHT_VALUE);
            }
        });

        emulator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Command cmd = new Command(Command.CommandType.CommandTypeSENS, isChecked ? 1 : 0);
                applyChanges(cmd, true);
            }
        });

        runModle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Command cmd = new Command(Command.CommandType.CommandRunModel, isChecked ? 1 : 0);
            applyChanges(cmd, true);
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(type.getText().toString()) &&
                        TextUtils.isEmpty(number.getText().toString())) {
                    Toast.makeText(LightInfoActivity.this, "请输入发送的值", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(number.getText().toString())) {
                    Command numberCmd = new Command(Command.CommandType.CommandTypeID, Integer.valueOf(number.getText().toString()));
                    applyChanges(numberCmd, true);
                }

                if (!TextUtils.isEmpty(type.getText().toString())) {
                    Command cmd = new Command(Command.CommandType.CommandTypeType, Integer.valueOf(type.getText().toString()));
                    applyChanges(cmd, true);
                }


            }
        });


    }

    private void updataUI() {
        Cursor c = null;
        if (light_id > 0) {
            c = getContentResolver().query(YanKonProvider.URI_LIGHTS, null, "_id=" + light_id, null, null);
        } else if (group_id > 0) {
            c = getContentResolver().query(YanKonProvider.URI_LIGHT_GROUPS, null, "_id=" + group_id, null, null);
        }
        if (c != null) {
            if (c.moveToFirst()) {
                color = c.getInt(c.getColumnIndex("color"));
                brightness = c.getInt(c.getColumnIndex("brightness"));
                if (brightness <= SWDefineConst.minbri) {
                    brightness = 0;
                }
                CT = c.getInt(c.getColumnIndex("CT"));
                mode = c.getInt(c.getColumnIndex("mode"));
                if (light_id > 0) {
                    state = c.getInt(c.getColumnIndex("state")) != 0;
                } else if (group_id > 0) {
                    state = c.getInt(c.getColumnIndex("num")) == c.getInt(c.getColumnIndex("on_num"));
                }
            }
            c.close();
        }
        mSwitch.setChecked(state);
        mBrightnessSeekBar.setValue(brightness);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getContentResolver().unregisterContentObserver( new YanKonObserver(mDelayHandler));
        getContentResolver().unregisterContentObserver(observer);
        if (FLAG)
            FLAG = false;
    }

    protected void saveOnExit() {

    }

    void applyChanges(Command cmd, boolean doItNow) {
        if (light_id > 0) {
            Utils.controlLight(this, light_id, cmd, doItNow);
        } else if (group_id > 0) {
            Utils.controlGroup(this, group_id, cmd, doItNow);
        } else if (lights != null && lights.length > 0) {
            Utils.controlLightsById(this, lights, cmd, doItNow);
        }
    }


    public void onColorChanged(View view, int newColor) {
        color = newColor;
        mode = Constants.MODE_COLOR;
        mDelayHandler.sendEmptyMessage(DelayHandler.MSG_CHANGE_COLOR);
    }

    @Override
    public void onColorPicked(View view, int newColor) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_info, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_done).setVisible(isForReturnValue);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        saveOnExit();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                saveOnExit();
                finish();
                return true;
            case R.id.action_done: {
                Intent intent = new Intent();
                intent.putExtra("state", state);
                intent.putExtra("color", color);
                intent.putExtra("brightness", brightness);
                intent.putExtra("CT", CT);
                intent.putExtra("mode", mode);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        state = isChecked;
        Command cmd = new Command(Command.CommandType.CommandTypeState, state ? 1 : 0);
        applyChanges(cmd, true);
    }


    public void execCommand(Command.CommandType type) {
        Command cmd = null;
        switch (type) {
            case CommandTypeBrightness:
                cmd = new Command(type, brightness);
                break;
            case CommandTypeCT:
                cmd = new Command(type, CT);
                break;
            case CommandTypeColor:
                cmd = new Command(type, color);
                break;
            case CommandTypeLux:
                cmd = new Command(type, lightValue);
                break;
        }
        if (cmd != null) {
            applyChanges(cmd, true);
        }
    }

    static class DelayHandler extends Handler {

        public static final int MSG_CHANGE_BRIGHTNESS = 1;
        public static final int MSG_CHANGE_CT = 2;
        public static final int MSG_CHANGE_COLOR = 3;
        public static final int MSG_CHANGE_LIGHT_VALUE = 6;

        WeakReference<LightInfoActivity> parentRef;

        public DelayHandler(LightInfoActivity activity) {
            super();
            parentRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what < 10) {
                Message scheduledMsg = obtainMessage();
                scheduledMsg.what = msg.what + 10;
                ///add for 15

                if (parentRef == null)
                    return;
                LightInfoActivity activity = parentRef.get();
                if (activity == null)
                    return;
                switch (msg.what) {
                    case MSG_CHANGE_BRIGHTNESS:
                        if (activity.brightness <= SWDefineConst.minbri) {
                            activity.brightness = 0;
                            activity.mBrightnessSeekBar.setValue(activity.brightness);
                        }
                        break;
                    case MSG_CHANGE_COLOR:
                        break;
                    case MSG_CHANGE_CT:
                        break;

                    case MSG_CHANGE_LIGHT_VALUE:
                        if (activity.lightValue <= SWDefineConst.minbri) {
                            activity.lightValue = 0;
                            activity.light.setValue(activity.lightValue);
                        }
                        break;
                }

                ///add for 15
                //下面两个方法不能同时使用
                //以下方法数值连续变化时不生效，直到该值连续DELAY_TIME时间都没有变化
//                removeMessages(scheduledMsg.what);
//                sendMessageDelayed(scheduledMsg, Constants.UI_OPERATION_DELAY_TIME);

                //这种方法在数值连续变化时，每隔DELAY_TIME生效一次
                if (!hasMessages(scheduledMsg.what)) {
                    sendMessageDelayed(scheduledMsg, Constants.UI_OPERATION_DELAY_TIME);
                }

            } else {
                if (parentRef == null)
                    return;
                LightInfoActivity activity = parentRef.get();
                if (activity == null)
                    return;
                switch (msg.what - 10) {
                    case MSG_CHANGE_BRIGHTNESS:
                        activity.execCommand(Command.CommandType.CommandTypeBrightness);
                        break;
                    case MSG_CHANGE_COLOR:
                        activity.execCommand(Command.CommandType.CommandTypeColor);
                        break;
                    case MSG_CHANGE_CT:
                        activity.execCommand(Command.CommandType.CommandTypeCT);
                        break;
                    case DATA_CHANGE:
                        activity.updataUI();
                        break;

                    case MSG_CHANGE_LIGHT_VALUE:
                        activity.execCommand(Command.CommandType.CommandTypeLux);
                        break;
                }
            }
        }
    }
}
