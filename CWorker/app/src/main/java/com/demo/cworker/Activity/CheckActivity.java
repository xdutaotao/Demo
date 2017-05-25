package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class CheckActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.normal_img)
    ImageView normalImg;
    @BindView(R.id.normal_layout)
    RelativeLayout normalLayout;
    @BindView(R.id.out_img)
    ImageView outImg;
    @BindView(R.id.out_layout)
    RelativeLayout outLayout;
    @BindView(R.id.other_img)
    ImageView otherImg;
    @BindView(R.id.other_layout)
    RelativeLayout otherLayout;

    private String checkString;
    private String[] strings = {"常规核查", "出口核查", "其它"};

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, CheckActivity.class);
        intent.putExtra(INTENT_KEY, s);
        context.startActivityForResult(intent, CollectActivity.REQUEST_CHECK_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "核查");

        if (getIntent().getStringExtra(INTENT_KEY) != null){
            checkString = getIntent().getStringExtra(INTENT_KEY);
            if (TextUtils.equals(checkString, strings[0])){
                normalImg.setVisibility(View.VISIBLE);
            }else if(TextUtils.equals(checkString, strings[1])){
                outImg.setVisibility(View.VISIBLE);
            }else if (TextUtils.equals(checkString, strings[2])){
                otherImg.setVisibility(View.VISIBLE);
            }
        }

        normalLayout.setOnClickListener(v -> {
            select(strings[0]);
        });

        outLayout.setOnClickListener(v -> {
            select(strings[1]);
        });

        otherLayout.setOnClickListener(v -> {
            select(strings[2]);
        });
    }

    private void select(String s){
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY, s);
        setResult(RESULT_OK, intent);
        finish();
    }
}
