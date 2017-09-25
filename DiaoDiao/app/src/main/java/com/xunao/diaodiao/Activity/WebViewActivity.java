package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ProjectDetailView;
import com.xunao.diaodiao.Widget.CustomWebView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.ApiConstants.H5_URL;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

public class WebViewActivity extends BaseActivity implements ProjectDetailView {
    private static final String INTENT_KEY = "intent_key";
    private static final String TITLE_KEY = "title_key";
    private static final String ID_KEY = "id_key";
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.web_view)
    CustomWebView webView;

    @Inject
    ProjectDetailPresenter presenter;

    private int type;
    private boolean isCollect = false;
    private int id;

    public static void startActivity(Context context, String url, String id) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(TITLE_KEY, id);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, int id, int type) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "详情");

        type = getIntent().getIntExtra("TYPE", 0);
        id = getIntent().getIntExtra(ID_KEY, 0);
        if (id != 0) {
            webView.loadUrl(H5_URL + getIntent().getStringExtra(INTENT_KEY) +
                    "?project_id="+id+"&userid="+ User.getInstance().getUserId()+"&type="+ type+"&typeRole="+ShareUtils.getValue(TYPE_KEY, 0))
                .setWebViewClient(webView.new GWebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        super.shouldOverrideUrlLoading(view, url);
                        if (url.contains("action=1")){
                            //申请成功
                            ToastUtil.show("申请成功");
                            WebViewActivity.this.finish();
                        }else if (url.contains("action=2")){
                            JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), type);
                        }else if (url.contains("action=3")){
                            //没有权限
                            ToastUtil.show("请选择角色");
                            SelectMemoryActivity.startActivity(WebViewActivity.this);
                        }
                        return true;
                    }
                });
        }else{
            webView.loadUrl(getIntent().getStringExtra(INTENT_KEY))
                    .setWebViewClient(webView.new GWebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                        }
                    });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isCollect) {
            menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02_fill);
        } else {
            menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:
                int types = 0;
                if (type == 0) {
                    types = 1;
                } else if (type == 1) {
                    types = 3;
                } else if (type == 2) {
                    types = 4;
                } else if (type == 3) {
                    types = 5;
                } else if (type == 4) {
                    types = 2;
                } else {

                }
                if (!isCollect) {
                    presenter.collectWork(this, getIntent().getIntExtra(ID_KEY, 0), types);
                } else {
                    presenter.cancelCollect(this, getIntent().getIntExtra(ID_KEY, 0));
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.getWebView().destroy();
        webView = null;
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(FindProjDetailRes res) {

    }

    @Override
    public void getLingGongData(FindLingGongRes res) {

    }

    @Override
    public void postProject(String res) {

    }

    @Override
    public void collectWork(String s) {
        ToastUtil.show("收藏成功");
        isCollect = true;
        invalidateOptionsMenu();
    }

    @Override
    public void cancleCollect(String s) {
        ToastUtil.show("取消收藏成功");
        isCollect = false;
        invalidateOptionsMenu();
    }
}
