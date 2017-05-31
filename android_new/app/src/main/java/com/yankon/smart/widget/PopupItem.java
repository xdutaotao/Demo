package com.yankon.smart.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by guzhenfu on 2015/8/18.
 */
public class PopupItem {
    public String mTitle;
    public int resID;

    public PopupItem(String title){
        this.mTitle = title;
    }

    public PopupItem(Context context, int titleId){
        this.mTitle = context.getResources().getString(titleId);
        resID = titleId;
    }

    public PopupItem(Context context, String title) {
        this.mTitle = title;
    }
}
