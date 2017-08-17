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

    private HomeResponseBean.TopicBean.GroupDataBean bean;

    private IOSDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;
    private File file;


    public static void startActivity(Context context, HomeResponseBean.TopicBean.GroupDataBean bean) {
        Intent intent = new Intent(context, HomeDetailActivity.class);
        intent.putExtra(INTENT_KEY, bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);


        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            bean = (HomeResponseBean.TopicBean.GroupDataBean) getIntent().getSerializableExtra(INTENT_KEY);
            showToolbarBack(toolBar, titleText, bean.getTitle());
            Glide.with(this).load(bean.getPic()).placeholder(R.drawable.ic_launcher_round).into(image);
            title.setText(bean.getTitle());
            author.setText("作者:" + bean.getAuthor());
            time.setText("上传时间:" + Utils.getStrTime(String.valueOf(bean.getUpdateTime())));
            vip.setVisibility(bean.getVipRes() != 0 ? View.VISIBLE : View.GONE);
            content.setText(bean.getDescription());
        }

        read.setOnClickListener(v -> {
            //VIP 文档
            if (bean.getVipRes() != 0){
                if (User.getInstance().getUserInfo() != null && User.getInstance().getUserInfo().getPerson().getVIP() != 0){
                    openPDF();
                }else{
                    showWarningDialog();
                }
            }else{
                openPDF();
            }
        });
    }

    private void openPDF(){
        if (!TextUtils.isEmpty(bean.getUrl())){
            String name;
            String[] temp = bean.getUrl().split("/");
            String[] names = temp[temp.length - 1].split(" ");

            String[] suffix = bean.getUrl().split("\\.");
            String suffixName = suffix[suffix.length-1];

            if (names.length == 3){
                name = names[0] + names[1] + "." + suffixName;
            }else if (names.length == 2){
                name = names[0] + "." + suffixName;
            }else{
                name = suffixName + "." + suffixName;
            }

            file = new File(Environment.getExternalStorageDirectory()+"/cwork/", name);
            if(file.exists()){
                startNewActivity();
            }else{
                showDownloadDialog();
                presenter.apkFileDownload(bean.getUrl(), file);
            }

        }
    }

    private void showDownloadDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressText = (TextView) view.findViewById(R.id.progress_text);
        dialog = new IOSDialog(this);
        dialog.builder()
                .setTitle("下载文件")
                .setContentView(view)
                .show();
    }


    private void showWarningDialog(){
        new IOSDialog(this).builder()
                .setTitle("此文档为VIP文档")
                .setMsg("您还不是VIP会员，请到会员中心兑换")
                .setPositiveButton("确定", null)
                .setPositiveBtnColor(R.color.colorPrimary)
                .show();
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
            startNewActivity();
        }
    }

    private void startNewActivity(){
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
//            startActivity(intent);
//        }catch (Exception e){
//            ToastUtil.show("请安装pdf阅读器查看");
//        }
//
//        DocActivity.startActivity(this, file.getAbsolutePath());

    }
}
