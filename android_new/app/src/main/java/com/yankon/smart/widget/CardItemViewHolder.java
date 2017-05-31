package com.yankon.smart.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yankon.smart.R;

import java.util.Random;

/**
 * Created by Evan on 15/1/20.
 */
public class CardItemViewHolder {

    public static final int layout_id = R.layout.light_item;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_LOCAL = 1;
    public static final int STATUS_REMOTE = 2;

    public TextView title;
    public ImageView lightIcon;
    public ToggleButton switchButton;

    public View infoButton;
    public View keyView;
    public View remoteView;
    Context mContext;

    public CardItemViewHolder(View view) {
        super();
        mContext = view.getContext().getApplicationContext();
        title = (TextView) view.findViewById(R.id.title);
        switchButton = (ToggleButton) view.findViewById(R.id.toggle_btn);
        infoButton = view.findViewById(R.id.light_info);
        lightIcon = (ImageView) view.findViewById(R.id.light_icon);
        keyView = view.findViewById(R.id.light_controll);
        remoteView = view.findViewById(R.id.remote);
    }

    public void updateStatus(int status, boolean hasPassword) {
        if (status == STATUS_REMOTE)
            remoteView.setVisibility(View.VISIBLE);
        else
            remoteView.setVisibility(View.GONE);

        if (status != STATUS_NONE && hasPassword) {
            keyView.setVisibility(View.VISIBLE);
        } else {
            keyView.setVisibility(View.GONE);
        }
    }

    public void setIconColor(){
        GradientDrawable myGrad = (GradientDrawable)lightIcon.getBackground();
        Random random = new Random();
        int r = random.nextInt(255) << 16;
        r = r & 0xFF0000;
        int g = random.nextInt(255) << 8;
        g = g & 0xFF00;
        int b = random.nextInt(255) & 0xFF;
        int color =  r + g + b;
        myGrad.setColor(-color);
    }

}
