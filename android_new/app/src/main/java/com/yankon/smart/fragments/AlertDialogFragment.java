package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;

import com.yankon.smart.R;
import com.yankon.smart.widget.Effectstype;
import com.yankon.smart.widget.NiftyDialogBuilder;

/**
 * Created by tian on 15/3/16:下午8:58.
 */
public class AlertDialogFragment extends DialogFragment {
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_BUILD_NETWORK = 1;
    public static final int TYPE_RESET_NETWORK = 2;
    private int mType;

    public static AlertDialogFragment newInstance(int type) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mType = getArguments().getInt("type");
        //AlertDialog.Builder builder = null;
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

        switch (mType) {
            case TYPE_LOGIN:
                dialogBuilder
                        .withMessage(null)
                        .withTitle(R.string.confirm_log_in)                                  //.withTitle(null)  no title
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(300)                                          //def
                        .withButton1Text(getString(android.R.string.ok))
                        .withButton2Text(getString(android.R.string.cancel));

//                builder = new Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
//                        R.string.confirm_log_in);
                break;
            case TYPE_BUILD_NETWORK:
                dialogBuilder
                        .withMessage(R.string.addlights_nolight_prompt)
                        .withTitle(null)                                  //.withTitle(null)  no title
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(300)                                          //def
                        .withButton1Text(getString(android.R.string.ok))
                        .withButton2Text(getString(android.R.string.cancel));

//                builder = new Builder(getActivity()).setMessage(
//                        R.string.addlights_nolight_prompt);
                break;
            case TYPE_RESET_NETWORK:
                dialogBuilder
                        .withMessage(R.string.addlights_resetall)
                        .withTitle(null)                                  //.withTitle(null)  no title
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(300)                                          //def
                        .withButton1Text(getString(android.R.string.ok))
                        .withButton2Text(getString(android.R.string.cancel));

//                builder = new Builder(getActivity()).setMessage(
//                        R.string.addlights_resetall);
                break;
        }

        dialogBuilder.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOk(mType);
                    dialogBuilder.dismiss();
                }
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCancel(mType);
                    dialogBuilder.dismiss();
                }
            }
        });
        return dialogBuilder;

//        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (mListener != null) {
//                    mListener.onOk(mType);
//                }
//            }
//        }).setNegativeButton(android.R.string.cancel, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (mListener != null) {
//                    mListener.onCancel(mType);
//                }
//            }
//        });
//        return builder.create();
    }

    public interface AlertDialogListener {
        public void onOk(int type);

        public void onCancel(int type);
    }

    AlertDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AlertDialogListener) {
            mListener = (AlertDialogListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
