package com.yankon.smart.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yankon.smart.R;

/**
 * Created by James on 2015/8/3.
 */
public class SwitchItemViewHolder {

    public static final int layout_id = R.layout.switch_list_item;
    public TextView title;
    private Context mContext;

    public SwitchItemViewHolder(View view) {
        super();
        mContext = view.getContext().getApplicationContext();
        title = (TextView) view.findViewById(R.id.title);
    }
}
