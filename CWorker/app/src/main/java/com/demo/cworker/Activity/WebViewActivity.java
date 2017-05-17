package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;

import com.demo.cworker.R;
import com.demo.cworker.Widget.CustomWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {
    private static final String INTENT_KEY = "intent_key";
    private static final String TITLE_KEY = "title_key";
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.web_view)
    CustomWebView webView;

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(TITLE_KEY, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            webView.loadUrl(getIntent().getStringExtra(INTENT_KEY))
                .setWebViewClient(webView.new GWebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                    }
                });
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra(TITLE_KEY))){
            showToolbarBack(toolBar, titleText, getIntent().getStringExtra(TITLE_KEY));
        }else{
            showToolbarBack(toolBar, titleText, "详情");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.getWebView().destroy();
        webView = null;
    }
}
