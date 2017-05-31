package com.yankon.smart.widget;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by guzhenfu on 2015/8/24.
 */
public class DialogPreferenceEx extends DialogPreference {
    private Context context;

    public DialogPreferenceEx(Context context) {
        this(context, null);
    }

    public DialogPreferenceEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void setDialogLayoutResource(int dialogLayoutResId) {
        super.setDialogLayoutResource(dialogLayoutResId);
    }

}
