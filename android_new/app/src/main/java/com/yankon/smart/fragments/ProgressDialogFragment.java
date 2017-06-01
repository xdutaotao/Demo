package com.yankon.smart.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;

import com.kii.tutk.TUTKVideoView;
import com.tutk.IOTC.IOTCAPIs;
import com.yankon.smart.utils.LogUtils;


public class ProgressDialogFragment extends DialogFragment implements TUTKVideoView.ListenIOTCTime{

    private static final String ARG_TITLE = "title";
    private static final String ARG_MSG = "msg";

    private String mTitle;
    private String mMessage;
    private long startTime = -1L;
    private CancleIOTCListener cancleIOTCListener;

    public void setCancleIOTCListener(CancleIOTCListener cancleIOTCListener) {
        this.cancleIOTCListener = cancleIOTCListener;
    }

    public static ProgressDialogFragment newInstance(String title, String msg) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    public ProgressDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mMessage = getArguments().getString(ARG_MSG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(mMessage);
        dialog.setTitle(mTitle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                long endTime = System.currentTimeMillis();

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (endTime - startTime > 10*1000) {
                        LogUtils.i("TAG", "iotc time: " + (endTime-startTime) + "-----" + "true");
                        return true;
                    }
                    else{
                        LogUtils.i("TAG", "iotc time: " + (endTime-startTime) + "-----" + "false");
                        IOTCAPIs.IOTC_Connect_Stop();
                        if (cancleIOTCListener != null)
                            cancleIOTCListener.onCancleIOTCListener();
                    }
                }
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onListenIOTCTime(long time) {
        startTime = time;
    }

    public interface CancleIOTCListener {
        void onCancleIOTCListener();
    }
}
