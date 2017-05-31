package com.yankon.smart.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yankon.smart.R;
import com.yankon.smart.utils.SharedPreferencesUtils;

/**
 * Created by guzhenfu on 2015/8/24.
 */
public class ListPreferenceEx extends ListPreference {
    private Context context;

    public ListPreferenceEx(Context context) {
        this(context, null);
    }

    public ListPreferenceEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        int checkItem = 0;
        builder.setSingleChoiceItems(R.array.sync_policies, checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView listView = new ListView(context);
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                Effectstype effect= Effectstype.Fadein;

                dialogBuilder
                        .withMessage(null)
                        .withTitle("dddddddddd")                                  //.withTitle(null)  no title
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(300)                                          //def
                        .withEffect(effect)                                 //def gone
                        .setCustomView(listView, context)         //.setCustomView(View or ResId,context)
                        .show();
            }
        });
    }


}
