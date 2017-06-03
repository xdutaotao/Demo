package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.demo.cworker.R;

import org.textmining.text.extraction.WordExtractor;

import java.io.FileInputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.text)
    TextView text;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "使用帮助");
        text.setText(readWord());
    }

    private String readWord() {
        String text = "";
        try {
            //FileInputStream in = (FileInputStream) getResources().openRawResource(R.raw.demo);
            InputStream inputStream = getAssets().open("demo.docx");
            WordExtractor extractor = new WordExtractor();
            text = extractor.extractText(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
