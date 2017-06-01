package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yankon.smart.R;
import com.yankon.smart.widget.Effectstype;
import com.yankon.smart.widget.NiftyDialogBuilder;


public class WarningDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";

    private static final String ARG_TYPE = "type";

    private String title;

    private int type;

    private WarningDialogInterface mListener;

    public void setWarningDialogInterface(WarningDialogInterface mListener) {
        this.mListener = mListener;
    }

    public static WarningDialogFragment newInstance(String title, int type) {
        WarningDialogFragment fragment = new WarningDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public WarningDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof WarningDialogInterface) {
            mListener = (WarningDialogInterface) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        Effectstype effect= Effectstype.Fadein;

        dialogBuilder
                .withMessage(null)
                .withTitle(title)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withEffect(effect)                                 //def gone
                .withButton1Text(getString(android.R.string.ok))
                .withButton2Text(getString(android.R.string.cancel))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onWarningDialogDone(type);
                            dialogBuilder.dismiss();
                        }
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
        return dialogBuilder;
    }

    public interface WarningDialogInterface {
        public void onWarningDialogDone(int type);
    }

}
