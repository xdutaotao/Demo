package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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


public class InputDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";

    public static final String ARG_TEXT = "text";

    private static final String ARG_HINT = "hint";

    private static final String ARG_MSG = "msg";

    private static final String ARG_TYPE = "type";

    public static final int TYPE_EDIT_LIGHT = 0;

    public static final int TYPE_RESET_PASSWORD = 1;

    public static final int TYPE_REMOTE_PWD = 2;

    private String title, text, hint, msg;

    private int type;

    InputDialogInterface mListener;

    public void setInputDialogInterface(InputDialogInterface mListener) {
        this.mListener = mListener;
    }

    public static InputDialogFragment newInstance(String title, String text, String hint,
            String msg, int type) {
        InputDialogFragment fragment = new InputDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TEXT, text);
        args.putString(ARG_HINT, hint);
        args.putString(ARG_MSG, msg);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public InputDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            text = getArguments().getString(ARG_TEXT);
            hint = getArguments().getString(ARG_HINT);
            msg = getArguments().getString(ARG_MSG);
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof InputDialogInterface) {
            mListener = (InputDialogInterface) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.fragment_input_dialog, null);
        final EditText et = (EditText) v.findViewById(R.id.input_edit);
        et.setText(text);
        et.setHint(hint);
        if (!TextUtils.isEmpty(text)) {
            et.setSelection(text.length());
        }

        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        Effectstype effect= Effectstype.Fadein;

        dialogBuilder
                .withMessage(null)
                .withTitle(title)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withEffect(effect)                                 //def gone
                .setCustomView(v, getActivity())         //.setCustomView(View or ResId,context)
                .withButton1Text(getString(android.R.string.ok))
                .withButton2Text(getString(android.R.string.cancel))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = et.getText().toString().trim();
                        if (TextUtils.isEmpty(data)) {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.input_dialog_empty),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (type == TYPE_RESET_PASSWORD && !android.util.Patterns.EMAIL_ADDRESS
                                .matcher(data).matches()) {
                            Toast.makeText(getActivity(), R.string.invalid_email,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(ARG_TEXT, data);
                        if (getTargetFragment() != null) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    Activity.RESULT_OK,
                                    intent);
                        }
                        if (mListener != null) {
                            mListener.onInputDialogTextDone(data);
                            dialogBuilder.dismiss();
                        }
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getTargetFragment() != null) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    Activity.RESULT_CANCELED,
                                    getActivity().getIntent());
                        }
                        dialogBuilder.dismiss();
                    }
                })
                .show();

        if (!TextUtils.isEmpty(msg)) {
            dialogBuilder.withMessage(msg);
        }
        if (type == TYPE_RESET_PASSWORD) {
            et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else if (type == TYPE_REMOTE_PWD) {
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        return dialogBuilder;
    }

    public interface InputDialogInterface {
        public void onInputDialogTextDone(String text);
    }

}
