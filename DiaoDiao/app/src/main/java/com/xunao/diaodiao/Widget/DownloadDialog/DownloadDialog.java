package com.xunao.diaodiao.Widget.DownloadDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.R;

/**
 * Description:
 * Created by guzhenfu on 2017/1/20 11:32.
 */

public class DownloadDialog implements DownloadInterface {
    private IOSDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;

    public static DownloadDialog getInstance(){
        return SingleHolder.instance;
    }

    private static class SingleHolder{
        public static DownloadDialog instance = new DownloadDialog();
    }

    private DownloadDialog(){}

    @Override
    public void showDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressText = (TextView) view.findViewById(R.id.progress_text);
        if (dialog != null){
            return;
        }
        dialog = new IOSDialog(context);
        dialog.builder()
                .setTitle("下载APP")
                .setContentView(view)
                .show();
    }

    @Override
    public void showProgress(float progress) {
        if (dialog == null){
            return;
        }
        int downProgress = (int) (progress * 100);
        progressBar.setProgress(downProgress);
        progressText.setText(String.valueOf(downProgress) + "%");
    }

    @Override
    public void dismissDialog() {
        if (dialog == null){
            return;
        }
        dialog.dismiss();
    }
}
