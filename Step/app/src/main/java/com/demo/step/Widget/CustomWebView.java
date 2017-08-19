package com.demo.step.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.demo.step.R;
import com.demo.step.Utils.NetWorkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Created by guzhenfu on 2016/11/23 11:19.
 */

public class CustomWebView extends FrameLayout {
    protected ViewGroup mProgressView;
    protected ViewGroup mNoNetView;
    protected ViewGroup mErrorView;
    private WebView webview;

    private int mProgressId;
    private int mNoNetId;
    private int mErrorId;

    private SettingBuilder settingBuilder;
    private OnLoadFinishListener listener;

    private Observable<Long> mObservable;
    private Subscription subscription;
    private int timeOut = 10;

    public final static int Success = 0;
    public final static int Error = 1;
    public final static int NoNet = 2;
    public final static int Loading = 3;
    private int status;
    private boolean isError;
    private String url;

    public void setError(boolean error) {
        isError = error;
    }

    public void setListener(OnLoadFinishListener listener) {
        this.listener = listener;
    }

    public CustomWebView(Context context) {
        super(context);
        initView();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.custom_web_view);
        try {
            mNoNetId = a.getResourceId(R.styleable.custom_web_view_layout_nonet, 0);
            mProgressId = a.getResourceId(R.styleable.custom_web_view_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.custom_web_view_layout_error, 0);
        }finally {
            a.recycle();
        }
    }

    private void initView() {
        if(isInEditMode())
            return;

        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_web_view_layout, this);
        mProgressView = (ViewGroup) v.findViewById(R.id.progress);

        if (mProgressId == 0)
            mProgressId = R.layout.view_progress;
        LayoutInflater.from(getContext()).inflate(mProgressId,mProgressView);

        mNoNetView = (ViewGroup) v.findViewById(R.id.no_net);
        if (mNoNetId == 0)
            mNoNetId = R.layout.view_no_net;
        LayoutInflater.from(getContext()).inflate(mNoNetId,mNoNetView);

        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        if(mErrorId == 0)
            mErrorId = R.layout.view_error;
        LayoutInflater.from(getContext()).inflate(mErrorId,mErrorView);

        mErrorView.findViewById(R.id.reload).setOnClickListener(view -> {
            reloadUrl(url);
        });

        mNoNetView.findViewById(R.id.reload).setOnClickListener(view -> {
            reloadUrl(url);
        });
        initWebView(v);
    }

    private void initWebView(View v) {
        webview = (WebView) v.findViewById(R.id.web_view_in);
        settingBuilder = new SettingBuilder(getContext() , webview);
        mObservable = Observable.timer(timeOut, TimeUnit.SECONDS);
        setWebChromeClient(new GWebChromeClient());
        setWebViewClient(new GWebViewClient());
    }

    /**
     * 开始 webview 加载 定时
     */
    private void startTime(){
        subscription  = mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i("newProgress", aLong +"---");
                        if (subscription != null) {
                            isError = true;
                            setStatus(Error);
                            endTime();
                        }
//                        webview.pauseTimers();
//                        webview.stopLoading();
                        webview.pauseTimers();
                    }
                });
    }

    /**
     * 取消  webview 加载 定时
     */
    private void endTime(){
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public WebView getWebView(){
        return webview;
    }

    public CustomWebView loadUrl(String url){
        this.url = url;
        webview.loadUrl(url);
        startTime();
        setStatus(Loading);
        isError = false;
        return this;
    }

    public CustomWebView reloadUrl(String url){
        this.url = url;
        webview.reload();
        startTime();
        setStatus(Loading);
        isError = false;
        return this;
    }

    public CustomWebView setWebChromeClient(GWebChromeClient client){
        webview.setWebChromeClient(client);
        return this;
    }

    public CustomWebView setWebViewClient(GWebViewClient client){
        webview.setWebViewClient(client);
        return this;
    }


    public SettingBuilder getSettingBuilder() {
        return settingBuilder;
    }

    private void showErrorView(){
        webview.setVisibility(GONE);
        mNoNetView.setVisibility(GONE);
        mProgressView.setVisibility(GONE);
        mErrorView.setVisibility(VISIBLE);
    }

    private void showNoNetView(){
        webview.setVisibility(GONE);
        mNoNetView.setVisibility(VISIBLE);
        mProgressView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
    }

    private void showProgressView(){
        webview.setVisibility(GONE);
        mNoNetView.setVisibility(GONE);
        mProgressView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
    }

    private void showWebView(){
        webview.setVisibility(VISIBLE);
        mNoNetView.setVisibility(GONE);
        mProgressView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
    }

    public static final class SettingBuilder{

        WebSettings webSettings;
        public SettingBuilder(Context context , WebView webView){
            webSettings = webView.getSettings();
            webSettings.setSupportMultipleWindows(true);// 支持多窗口
//            webSettings.setSupportZoom(false);// 设置可以支持缩放
//            webSettings.setBuiltInZoomControls(false); // 设置支持缩放
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                webSettings.setDisplayZoomControls(false); // 隐藏webview缩放按钮
            }
            // 设置是否显示网络图像---true,封锁网络图片，不显示 false----允许显示网络图片
            webSettings.setBlockNetworkImage(false);
            webSettings.setJavaScriptEnabled(true);// 访问页面中有JavaScript,必须设置支持JavaScript
            webSettings.setDefaultTextEncodingName("UTF-8");
            webSettings.setLoadsImagesAutomatically(true); // 设置自动加载图片
            //webview无限向下滑动 webview大量空白区域 内容无法填充webview
            //webSettings.setUseWideViewPort(false); // 将图片调整到适合WebView大小
//            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDatabaseEnabled(true);// 启用数据库
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();  // 设置定位的数据库路径
            webSettings.setGeolocationDatabasePath(dir);
            webSettings.setGeolocationEnabled(true);// 启用地理定位
            webSettings.setDomStorageEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setDefaultFontSize((int)15);
            //解决WebView在5.0以上不显示图片问题
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadWithOverviewMode(true);
        }
        public WebSettings build(){
            return this.webSettings;
        }
    }

    @IntDef({Success, Error, NoNet, Loading})
    public @interface Status {}

    public void setStatus(@Status int status){
        this.status = status;
        switch (status){
            case Success:
                showWebView();
                break;

            case Error:
                showErrorView();
                break;

            case NoNet:
                showNoNetView();
                break;

            case Loading:
                showProgressView();
                break;
        }
    }

    public class GWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webview.resumeTimers();
                if (listener != null)
                    listener.loadFinish();
                endTime();

                if (!NetWorkUtils.isNetworkAvailable()){
                    setStatus(NoNet);
                }else if (isError) {
                    setStatus(Error);
                }else {
                    setStatus(Success);
                }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            isError = true;
        }
    }

    public class GWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress  == 100){
//                if (listener != null)
//                    listener.loadFinish();
//                endTime();
//
//                if (!NetWorkUtils.isNetworkAvailable()){
//                    setStatus(NoNet);
//                }else if (isError) {
//                    setStatus(Error);
//                }else {
//                    setStatus(Success);
//                }
            }
        }

    }

    public interface OnLoadFinishListener{
        void loadFinish();
    }

}
