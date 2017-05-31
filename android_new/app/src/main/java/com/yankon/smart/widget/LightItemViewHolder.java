package com.yankon.smart.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yankon.smart.R;

/**
 * Created by Evan on 15/1/20.
 */
public class LightItemViewHolder {
    public static final int layout_id = R.layout.card_item;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_LOCAL = 1;
    public static final int STATUS_REMOTE = 2;

    public CheckBox checkBox;

    public TextView title;

    public ToggleButton switchButton;

    public TextView label1;

    public TextView label2;

    public TextView textView1;

    public TextView textView2;

    private TextView statusView;
    private View keyView;

    public View infoButton;
    Context mContext;

    public LightItemViewHolder(View view) {
        super();
        mContext = view.getContext().getApplicationContext();
        title = (TextView) view.findViewById(R.id.title);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        switchButton = (ToggleButton) view.findViewById(R.id.switch_button);
        label1 = (TextView) view.findViewById(R.id.label1);
        textView1 = (TextView) view.findViewById(R.id.data1);
        label2 = (TextView) view.findViewById(R.id.label2);
        textView2 = (TextView) view.findViewById(R.id.data2);
        statusView = (TextView) view.findViewById(R.id.light_local_status);
        keyView = view.findViewById(R.id.light_controll);
        infoButton = view.findViewById(R.id.info);
    }

    public void updateStatus(int status, boolean isRoaming, boolean hasPassword) {
        String status_text = "";
        switch (status) {
            case STATUS_NONE:
                statusView.setVisibility(View.GONE);
                break;
            case STATUS_LOCAL:
                statusView.setBackgroundResource(R.color.status_local_bg);
                status_text = mContext.getString(R.string.local);
                break;
            case STATUS_REMOTE:
                statusView.setBackgroundResource(R.color.status_remote_bg);
                status_text = mContext.getString(R.string.remote);
                break;
        }
        if (isRoaming) {
            status_text = status_text + " (" + mContext.getString(R.string.roaming) + ")";
        }
        statusView.setText(status_text);
        if (status != STATUS_NONE && hasPassword) {
            keyView.setVisibility(View.VISIBLE);
        } else {
            keyView.setVisibility(View.GONE);
        }
    }
}
