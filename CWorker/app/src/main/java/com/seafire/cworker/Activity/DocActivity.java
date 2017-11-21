package com.seafire.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.seafire.cworker.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.seafire.cworker.Utils.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocActivity extends BaseActivity implements OnErrorListener, OnLoadCompleteListener, OnPageChangeListener {
    private static final String FILE_PATH = "FILE_PATH";

    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.content)
    TextView content;

    private int pageNumber;

    public static void startActivity(Context context, String filePath) {
        Intent intent = new Intent(context, DocActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        ButterKnife.bind(this);

        showToolbarBack(toolBar, titleText, "");

        if (!TextUtils.isEmpty(getIntent().getStringExtra(FILE_PATH))) {
            String path = getIntent().getStringExtra(FILE_PATH);
            if (path.contains(".pdf") || path.contains(".PDF")) {
                File file = new File(path);
                if (file.exists()) {
                    content.setVisibility(View.GONE);
//                    pdfView.fromFile(file)
//                            .load();
                    try{
                        pdfView.fromUri(Uri.fromFile(file))
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
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            } else if (path.contains(".xls") || path.contains(".xlsx")) {
//                Workbook wb = null;
//                String s = "";
//                try {
//                    InputStream is = new FileInputStream(path);
//                    wb = new HSSFWorkbook(is);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                for (Sheet sheet : wb) {
//                    for (Row row : sheet) {
//                        for (Cell cell : row) {
//                            s = cell.getStringCellValue() + "";// Do something here
//                        }
//                        s+="/n";
//                    }
//                }
//                pdfView.setVisibility(View.GONE);
//                content.setVisibility(View.VISIBLE);
//                content.setText(s);
            }

        }
    }

    @Override
    public void onError(Throwable t) {
        ToastUtil.show(t.getMessage());
    }

    @Override
    public void loadComplete(int nbPages) {
        ToastUtil.show(nbPages + "");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }
}
