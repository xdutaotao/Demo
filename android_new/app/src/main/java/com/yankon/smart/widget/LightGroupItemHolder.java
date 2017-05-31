package com.yankon.smart.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yankon.smart.R;

/**
 * Created by guzhenfu on 2015/8/26.
 */
public class LightGroupItemHolder {
    public static final int layout_id = R.layout.light_group_item;

    public CheckBox checkBox;

    public ToggleButton switchButton;

    public TextView textView1;

    public TextView textView2;

    public View icon;

    Context mContext;

    public LightGroupItemHolder(View view) {
        super();
        mContext = view.getContext().getApplicationContext();
        icon = view.findViewById(R.id.light_icon);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        switchButton = (ToggleButton) view.findViewById(R.id.light_switch);
        textView1 = (TextView) view.findViewById(android.R.id.text1);
        textView2 = (TextView) view.findViewById(android.R.id.text2);
    }

}
