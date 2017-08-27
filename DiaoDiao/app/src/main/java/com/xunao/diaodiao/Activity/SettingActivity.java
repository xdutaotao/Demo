package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.UpdateVersionBean;
import com.xunao.diaodiao.Present.SettingPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.SettingView;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SettingActivity extends BaseActivity implements SettingView {

    @Inject
    SettingPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.update)
    RelativeLayout update;
    @BindView(R.id.clear_cache)
    RelativeLayout clearCache;
    @BindView(R.id.abc)
    RelativeLayout abc;
    @BindView(R.id.login)
    Button login;

    private IOSDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;
    private File file;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "设置");

        update.setOnClickListener(v -> {
            CheckPhoneActivity.startActivity(SettingActivity.this, 2);
        });

        abc.setOnClickListener(v -> {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=com.xunao.diaodiao"));
                startActivity(i);
            } catch (Exception e) {
                ToastUtil.show("您的手机上没有安装Android应用市场");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getUpdateVersion(UpdateVersionBean.DataBean bean) {
        if (TextUtils.equals(bean.getLastVersion(), bean.getVersion())) {
            ToastUtil.show("目前是最新版本");
        } else {
            new IOSDialog(this).builder()
                    .setTitle("升级版本")
                    .setMsg(bean.getRemark())
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", v -> {
                        showDownloadDialog();
                        file = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "cwork" + File.separator, bean.getVersion() + ".apk");
                        presenter.apkFileDownload(bean.getDownloadUrl(), file);
                    })
                    .show();
        }
    }

    @Override
    public void getProgress(float progress) {
        int downProgress = (int) (progress * 100);
        progressBar.setProgress(downProgress);
        progressText.setText(String.valueOf(downProgress) + "%");
        if (progress == 1.0f) {
            dialog.dismiss();
            Utils.installApk(this, file);
        }
    }

    @Override
    public void getData(String data) {
        ToastUtil.show(data);
    }

    private void showDownloadDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressText = (TextView) view.findViewById(R.id.progress_text);
        dialog = new IOSDialog(this);
        dialog.builder()
                .setTitle("下载文件")
                .setContentView(view)
                .show();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
