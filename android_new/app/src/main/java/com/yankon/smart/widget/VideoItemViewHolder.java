package com.yankon.smart.widget;

import android.view.View;
import android.widget.TextView;

import com.yankon.smart.R;

/**
 * Created by guzhenfu on 2015/11/21.
 */
public class VideoItemViewHolder {
    public static final int layout_id = R.layout.video_list_item;
    public TextView name;
    public View remoteView;

    public VideoItemViewHolder(View view){
        name = (TextView) view.findViewById(R.id.name);
        remoteView = view.findViewById(R.id.remote_icon);
    }
}
