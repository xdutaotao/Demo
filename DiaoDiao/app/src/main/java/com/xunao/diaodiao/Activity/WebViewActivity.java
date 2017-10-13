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
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
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
    private String url;

    public static final String LG_DETAIL = "release_lg_detail";
    public static final String RECEIVE_LG_DETAIL = "receive_release_lg_detail";
    public static final String RECEIVE_PROJ_DETAIL = "receive_release_proj_detail";
    public static final String HOME_DETAIL = "home_detail";
    public static final String COMPANY_PROJ = "company_proj";
    /**
     *  1项目2监理3零工4维保
     */
    private int project_type;

    private FindProjDetailRes projectBean;

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
        project_type = getIntent().getIntExtra("project_type", 0);
        url = getIntent().getStringExtra(INTENT_KEY);
        if (url.contains("has_collected=1")){
            //收藏
            isCollect = true;
        }

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
                if (ShareUtils.getValue(TYPE_KEY, 0) == 0) {
                    ToastUtil.show("请完善个人信息");
                    return;
                }
                presenter.postProject(this, id, type);
            }else {
                if (project_type == 0){
                    //联系发布人
                    Utils.startCallActivity(this, "12345678900");
                }else if (project_type == 1){
                    // 1 项目
                    RecommandActivity.startActivity(this, id, project_type);
                }

            }

        });

        againPost.setOnClickListener(v -> {
            //再次快捷发布
            ReleaseProjActivity.startActivity(this);
        });

        changeInfo.setOnClickListener(v -> {
            //修改项目信息
            int types = ShareUtils.getValue(TYPE_KEY, 0);
            if (types == COMPANY_TYPE){
                if (projectBean != null){

                    FindProjDetailRes.DetailBean bean = projectBean.getDetail();
                    ReleaseProjReq req = new ReleaseProjReq();
                    req.setProject_type(bean.getProject_type());
                    req.setProject_class(bean.getProject_class());
                    req.setTitle(bean.getTitle());
                    req.setProvince(bean.getProvince());
                    req.setCity(bean.getCity());
                    req.setDistrict(bean.getDistrict());
                    req.setAddress(bean.getAddress());
                    req.setContact(bean.getContact());
                    req.setContact_mobile(bean.getContact_mobile());
                    req.setBuild_time(bean.getBuild_time());
                    req.setDescribe(bean.getDescribe());
                    req.setImages(bean.getImages());
                    req.setService_cost(bean.getService_cost());
                    req.setProject_fee(bean.getProject_fee());
                    req.setTotal_price(bean.getTotal_price());
                    req.setExpenses(bean.getExpenses());

                    req.setRegion(projectBean.getProject().getRegion());
                    req.setProject_type_class(bean.getProject_type_class());
                    req.setProject_type_name(bean.getProject_type_name());
                    req.setProject_id(id);
                    ReleaseProjSecondActivity.startActivity(this , req, true);
                }
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

        if (project_type != 0){
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.VISIBLE);
            apply.setText("去评价");
        }

        if (ShareUtils.getValue(TYPE_KEY, 0) == 1){
            //公司角色
            apply.setVisibility(View.GONE);
        }

        presenter.getFindProjDetail(this, id);

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "update_project"))
                .subscribe(s -> {
                    finish();
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (TextUtils.equals(btnType, HOME_DETAIL)){
            getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
            if (isCollect) {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02_fill);
            } else {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02);
            }
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
                if (!TextUtils.isEmpty(User.getInstance().getUserId())){
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
                }

                return true;

            case R.id.action_contact:
                //取消项目
                presenter.myProjectCancel(this, id);
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
        //拿到项目详情
        projectBean = res;
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
