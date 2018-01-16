package com.xunao.diaodiao.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.Present.PDFPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.PermissionsUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.PDFView;
import com.xunao.diaodiao.Widget.DownloadDialog.DownloadDialogFactory;

import org.textmining.text.extraction.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class PDFActivity extends BaseActivity implements PDFView, OnPageChangeListener, OnErrorListener, OnLoadCompleteListener, OnDrawListener {

    @Inject
    PDFPresenter presenter;
    @BindView(R.id.pdf)
    com.github.barteksc.pdfviewer.PDFView pdf;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.content)
    TextView content;

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

        if ((getIntent().getStringExtra(INTENT_KEY) != null) && !TextUtils.equals(getIntent().getStringExtra(INTENT_KEY), "协议")) {
            content.setVisibility(View.GONE);
            pdf.setVisibility(View.VISIBLE);

            String[] temp = getIntent().getStringExtra(INTENT_KEY).split("/");
            String name = temp[temp.length - 1];

            file = new File(Environment.getExternalStorageDirectory(), name);
            if (file.exists()) {
                showPDF();
            } else {
                if (!PermissionsUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return;
                }

                showDownloadDialog();
                presenter.apkFileDownload(getIntent().getStringExtra(INTENT_KEY), file);
            }

        }else{
            content.setVisibility(View.VISIBLE);
            pdf.setVisibility(View.GONE);

            String text = "";
            try {
                InputStream in = getResources().openRawResource(R.raw.demo);
//            InputStream inputStream = getAssets().open("demo.doc");
                WordExtractor extractor = new WordExtractor();
                text = extractor.extractText(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            content.setMovementMethod(ScrollingMovementMethod.getInstance());
            if(!TextUtils.isEmpty(text))
                content.setText(text.substring(0, text.length()-30));

        }
    }

    private void showDownloadDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressText = (TextView) view.findViewById(R.id.progress_text);
        dialog = new IOSDialog(this);
        dialog.builder()
                .setTitle("加载中...")
                .setContentView(view)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }


    @Override
    public void onFailure() {
        ToastUtil.show("文档地址错误");
        if (dialog != null)
            dialog.dismiss();
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Override
    public void getProgress(float progress) {
        int downProgress = (int) (progress * 100);
        progressBar.setProgress(downProgress);
        progressText.setText(String.valueOf(downProgress) + "%");
        if (progress == 1.0f) {
            dialog.dismiss();
            showPDF();
        }

    }

    private void showPDF() {

        try {

            pdf.fromUri(Uri.fromFile(file))
                    .defaultPage(pageNumber)
                    .enableDoubletap(true)
                    .onDraw(this)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .enableDoubletap(true)
                    .load();


        }catch (Exception e){
            e.printStackTrace();
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
        //ToastUtil.show(nbPages + "");
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO: 2016/12/26 grantResults[0]
        if (grantResults != null && grantResults.length > 0) {
            switch (requestCode) {
                //权限申请成功
                case PermissionsUtils.WRITE_EXTERNAL_STORAGE_CODE:
                    showDownloadDialog();
                    presenter.apkFileDownload(getIntent().getStringExtra(INTENT_KEY), file);
                    break;
            }
        } else {
            PermissionsUtils.permissionNotice(this, requestCode);
        }
    }


}
