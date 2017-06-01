package com.yankon.smart.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yankon.smart.R;
import com.yankon.smart.SWDefineConst;
import com.yankon.smart.model.Command;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.widget.Effectstype;
import com.yankon.smart.widget.NiftyDialogBuilder;

import java.lang.ref.WeakReference;

/**
 * Created by guzhenfu on 2015/10/20.
 */
public class DelayDialogFragment extends DialogFragment {
    private String title;
    private TextView tv;
    private DelayDialogInterface delayDialogInterface;
    private int time;
    private NiftyDialogBuilder dialogBuilder;
    private Runnable delayTimer;

    private DelayHandler handler;

    public void setDelayDialogInterface(DelayDialogInterface delayDialogInterface) {
        this.delayDialogInterface = delayDialogInterface;
    }

    public static DelayDialogFragment newInstance(String title, int time) {
        Bundle args = new Bundle();
        DelayDialogFragment fragment = new DelayDialogFragment();
        args.putString("title", title);
        args.putInt("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            time = getArguments().getInt("time");
        }
        handler = new DelayHandler(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DelayDialogInterface) {
            delayDialogInterface = (DelayDialogInterface) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delayDialogInterface = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.delay_dialog, null);
        tv = (TextView) v.findViewById(R.id.tv_delay);
        tv.setText(String.format(getResources().getString(R.string.delay_time), time));

        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        Effectstype effect= Effectstype.Fadein;

        dialogBuilder
                .withMessage(null)
                .withTitle(title)                                  //.withTitle(null)  no title
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(300)                                          //def
                .withEffect(effect)                                 //def gone
                .setCustomView(v, getActivity())         //.setCustomView(View or ResId,context)
                .withButton1Text(getString(android.R.string.ok))
                .setButton2Visibility(View.GONE)
                .setButton1Visibility(View.GONE)
                .show();

        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });

        delayTimer = new ThreadShow();
        new Thread(delayTimer).start();
        return dialogBuilder;
    }

    public interface DelayDialogInterface {
        public void delayDialogDone();
    }

    class ThreadShow implements Runnable {

        @Override
        public void run() {
            while (time != 0) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    time--;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handlerMessage() {
        if (delayDialogInterface != null && (time <= 0)) {
            delayDialogInterface.delayDialogDone();
            dialogBuilder.dismiss();
        }else if (isAdded())
            tv.setText(String.format(getResources().getString(R.string.delay_time), time));
        else
            ;
    }

    static class DelayHandler extends Handler {

        WeakReference<DelayDialogFragment> parentRef;

        public DelayHandler(DelayDialogFragment activity) {
            super();
            parentRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
                if (parentRef == null)
                    return;
                DelayDialogFragment activity = parentRef.get();
                if (activity == null)
                    return;
                if (msg.what == 1) {
                    activity.handlerMessage();
                }
        }
    }



}
