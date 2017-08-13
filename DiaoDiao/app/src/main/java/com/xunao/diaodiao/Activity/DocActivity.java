package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xunao.diaodiao.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocActivity extends BaseActivity {
    private static final String FILE_PATH = "FILE_PATH";

    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.content)
    TextView content;

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
                    pdfView.fromFile(file)
                            .load();
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
}
