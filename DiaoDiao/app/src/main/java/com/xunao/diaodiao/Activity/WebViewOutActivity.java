package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.xunao.diaodiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class WebViewOutActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView webView;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewOutActivity.class);
        intent.putExtra(INTENT_KEY, url);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_out);
        ButterKnife.bind(this);

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getIntent().getStringExtra(INTENT_KEY));
        intent.setData(content_url);
        startActivity(intent);

        finish();
    }
}
