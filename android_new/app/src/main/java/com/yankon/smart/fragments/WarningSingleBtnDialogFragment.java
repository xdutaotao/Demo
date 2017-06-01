package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.yankon.smart.widget.Effectstype;
import com.yankon.smart.widget.NiftyDialogBuilder;


public class WarningSingleBtnDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";

    private static final String ARG_TYPE = "type";

    private String title;

    private int type;

    private WarningSingleDialogInterface mListener;

    public void setWarningDialogInterface(WarningSingleDialogInterface mListener) {
        this.mListener = mListener;
    }

    public static WarningSingleBtnDialogFragment newInstance(String title, int type) {
        WarningSingleBtnDialogFragment fragment = new WarningSingleBtnDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public WarningSingleBtnDialogFragment() {
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
        if (activity instanceof WarningSingleDialogInterface) {
            mListener = (WarningSingleDialogInterface) activity;
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
                .setButton2Visibility(View.GONE)
                .isCancelableOnTouchOutside(false)
                .withButton1Text(getString(android.R.string.ok))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onWarningDialogDone(type);
                            dialogBuilder.dismiss();
                        }
                    }
                })
                .show();
        return dialogBuilder;
    }

    public interface WarningSingleDialogInterface {
        public void onWarningDialogDone(int type);
    }

}
