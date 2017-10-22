package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xunao.diaodiao.Present.PDFPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.gzfgeh.iosdialog.IOSDialog;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class PDFActivity extends BaseActivity implements PDFView, OnPageChangeListener, OnErrorListener, OnLoadCompleteListener {

    @Inject
    PDFPresenter presenter;
    @BindView(R.id.pdf)
    com.github.barteksc.pdfviewer.PDFView pdf;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private IOSDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;
    private File file;

    private int pageNumber;

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, PDFActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pd);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, getIntent().getStringExtra("title"));

        if (getIntent().getStringExtra(INTENT_KEY) != null) {
            String[] temp = getIntent().getStringExtra(INTENT_KEY).split("/");
            String[] names = temp[temp.length - 1].split(" ");
            String name;
            if (names.length == 3){
                name = names[0] + names[1] + ".pdf";
            }else if (names.length == 2){
                name = names[0] + ".pdf";
            }else{
                name = "pdf.pdf";
            }

            file = new File(Environment.getExternalStorageDirectory(), name);
            showDownloadDialog();
            presenter.apkFileDownload(getIntent().getStringExtra(INTENT_KEY), file);
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


    @Override
    public void onFailure() {
        ToastUtil.show("文档地址错误");
        if(dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getProgress(float progress) {
        int downProgress = (int) (progress * 100);
        progressBar.setProgress(downProgress);
        progressText.setText(String.valueOf(downProgress) + "%");
        if (progress == 1.0f) {
            dialog.dismiss();
            pdf.fromUri(Uri.fromFile(file))
                    .pages(pageNumber) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .enableDoubletap(true)
                    .onError(this)
                    .defaultPage(0)
                    .onLoad(this)
                    .onPageChange(this)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .load();
        }
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void onError(Throwable t) {
        ToastUtil.show(t.getMessage());
    }

    @Override
    public void loadComplete(int nbPages) {
        ToastUtil.show(nbPages + "");
    }
}
