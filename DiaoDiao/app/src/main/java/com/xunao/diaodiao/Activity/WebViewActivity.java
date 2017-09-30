package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ProjectDetailView;
import com.xunao.diaodiao.Widget.CustomWebView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
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
    @BindView(R.id.apply)
    Button apply;
    @BindView(R.id.change_info)
    TextView changeInfo;
    @BindView(R.id.again_post)
    TextView againPost;
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;

    private int type;
    private boolean isCollect = false;
    private int id;
    private String btnType;

    public static final String LG_DETAIL = "release_lg_detail";
    public static final String RECEIVE_LG_DETAIL = "receive_release_lg_detail";
    public static final String RECEIVE_PROJ_DETAIL = "receive_release_proj_detail";
    public static final String HOME_DETAIL = "home_detail";
    public static final String COMPANY_PROJ = "company_proj";

    private OrderCompRes.Project projectBean;
    private int project_type;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        context.startActivity(intent);
    }

    //首页轮播
    public static void startActivity(Context context, String url, String id) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(TITLE_KEY, id);
        context.startActivity(intent);
    }

    //底部没有button
    public static void startActivity(Context context, String url, int id) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        context.startActivity(intent);
    }

    //底部没有button
    public static void startActivity(Context context, String url, int id, String btnType) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        intent.putExtra("BTN_TYPE", btnType);
        context.startActivity(intent);
    }

    //底部没有button 传递btn
    public static void startActivity(Context context, String url, int id, String btnType, OrderCompRes.Project bean) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        intent.putExtra("BTN_TYPE", btnType);
        intent.putExtra("BEAN", bean);
        context.startActivity(intent);
    }

    //去评价
    public static void startActivity(Context context, String url, int id, int project_type) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        intent.putExtra("project_type", project_type);
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
        btnType = getIntent().getStringExtra("BTN_TYPE");
        projectBean = (OrderCompRes.Project) getIntent().getSerializableExtra("BEAN");
//        if (id != 0) {
//            webView.loadUrl(H5_URL + getIntent().getStringExtra(INTENT_KEY) +
//                    "?project_id=" + id + "&userid=" + User.getInstance().getUserId() + "&type=" + type + "&typeRole=" + ShareUtils.getValue(TYPE_KEY, 0))
//                    .setWebViewClient(webView.new GWebViewClient() {
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            super.shouldOverrideUrlLoading(view, url);
//                            if (url.contains("action=1")) {
//                                //申请成功
//                                ToastUtil.show("申请成功");
//                                WebViewActivity.this.finish();
//                            } else if (url.contains("action=2")) {
//                                JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), type);
//                            } else if (url.contains("action=3")) {
//                                //没有权限
//                                ToastUtil.show("请选择角色");
//                                SelectMemoryActivity.startActivity(WebViewActivity.this);
//                            }
//                            return true;
//                        }
//                    });
//        } else {
//            webView.loadUrl(getIntent().getStringExtra(INTENT_KEY))
//                    .setWebViewClient(webView.new GWebViewClient() {
//                        @Override
//                        public void onPageFinished(WebView view, String url) {
//                            super.onPageFinished(view, url);
//                        }
//                    });
//        }

        webView.loadUrl(getIntent().getStringExtra(INTENT_KEY))
                .setWebViewClient(webView.new GWebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        super.shouldOverrideUrlLoading(view, url);
                        if (url.contains("action=1")){
                            //项目
                            JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
                        }else if (url.contains("action=2")){
                            //零工
                            JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
                        }
                        return true;
                    }
                });

        apply.setOnClickListener(v -> {
            if (TextUtils.equals(btnType, HOME_DETAIL)){
                int type = ShareUtils.getValue(TYPE_KEY, 0);
                if (type == 0) {
                    ToastUtil.show("请完善个人信息");
                    return;
                }
                presenter.postProject(this, id, type);
            }else {
                //联系发布人
                Utils.startCallActivity(this, "12345678900");
            }

        });

        againPost.setOnClickListener(v -> {
            //再次快捷发布

        });

        changeInfo.setOnClickListener(v -> {
            //修改项目信息
            int types = ShareUtils.getValue(TYPE_KEY, 0);
            if (types == COMPANY_TYPE){
                
            }
        });

        if (TextUtils.equals(HOME_DETAIL, btnType)){
            //首页
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.VISIBLE);
        }else if (TextUtils.equals(LG_DETAIL, btnType)){
            bottomBtnLayout.setVisibility(View.VISIBLE);
            apply.setVisibility(View.GONE);
        }else if (TextUtils.equals(btnType, RECEIVE_LG_DETAIL)){
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setText("联系发布人");
        }else if (TextUtils.equals(btnType, RECEIVE_PROJ_DETAIL)){
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setText("联系发布人");
        }else if (TextUtils.equals(btnType, COMPANY_PROJ)){
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.VISIBLE);
        }
        else{
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (TextUtils.equals(btnType, HOME_DETAIL)){
            getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
        }else if (TextUtils.equals(btnType, COMPANY_PROJ)){
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (TextUtils.equals(btnType, HOME_DETAIL)){
            if (isCollect) {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02_fill);
            } else {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:
                //收藏项目
                if (TextUtils.equals(btnType, HOME_DETAIL)){
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
                }else if (TextUtils.equals(btnType, COMPANY_PROJ)){

                }
                return true;

            case R.id.action_contact:
                //取消项目
                presenter.myProjectCancel(this, projectBean.getProject_id());
                break;
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
    public void postProject(Object res) {
        ToastUtil.show("申请成功");
        finish();
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

    @Override
    public void myProjectCancel(Object s) {
        ToastUtil.show("取消项目成功");
        finish();
    }
}
