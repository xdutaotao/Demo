package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.PDFPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.PDFView;
import com.gzfgeh.iosdialog.IOSDialog;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class HomeDetailActivity extends BaseActivity implements PDFView {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.vip)
    TextView vip;
    @BindView(R.id.read)
    Button read;
    @BindView(R.id.content)
    TextView content;

    @Inject
    PDFPresenter presenter;

    private HomeResponseBean bean;

    private IOSDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;
    private File file;


    public static void startActivity(Context context, String data) {
        Intent intent = new Intent(context, HomeDetailActivity.class);
        intent.putExtra(INTENT_KEY, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, title, getIntent().getStringExtra(INTENT_KEY));

    }

    @Override
    public void onFailure() {
        dialog.dismiss();
        ToastUtil.show("下载PDF失败");
    }

    @Override
    public void getProgress(float progress) {
        int downProgress = (int) (progress * 100);
        progressBar.setProgress(downProgress);
        progressText.setText(String.valueOf(downProgress) + "%");
        if (progress == 1.0f) {
            dialog.dismiss();
        }
    }
}
