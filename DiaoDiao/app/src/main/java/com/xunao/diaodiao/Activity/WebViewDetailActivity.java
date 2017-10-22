package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.WebViewDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.WebViewDetailView;
import com.xunao.diaodiao.Widget.CustomWebView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_WAIT;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;

/**
 * create by
 */
public class WebViewDetailActivity extends BaseActivity implements WebViewDetailView {

    @Inject
    WebViewDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.web_view)
    CustomWebView webView;
    @BindView(R.id.apply)
    Button apply;
    @BindView(R.id.change_info)
    TextView changeInfo;
    @BindView(R.id.again_post)
    TextView againPost;
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;

    OrderCompRes.Project project;
    OrderSkillFinishRecieveRes.OddBean odd;
    HomeResponseBean.Carousel carousel;
    MessageListRes.MessageBean messageBean;


    int who;
    String url;
    @BindView(R.id.share)
    FloatingActionButton share;
    private boolean isCancle = false;

    private FindProjDetailRes projectBean;
    private FindLingGongRes oddBean;

    private ShareSDK myShareSDK;
    private String title;

    //暖通公司 项目
    public static void startActivity(Context context, OrderCompRes.Project bean, int status) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("project", bean);
        intent.putExtra("who", status);
        context.startActivity(intent);
    }

    //技术人员  项目
    public static void startActivity(Context context, OrderSkillFinishRecieveRes.OddBean bean, int status) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("skill_proj_finish", bean);
        intent.putExtra("who", status);
        context.startActivity(intent);
    }

    //首页轮播
    public static void startActivity(Context context, HomeResponseBean.Carousel carousel) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("carousel", carousel);
        context.startActivity(intent);
    }


    //收藏列表
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("favorite", url);
        context.startActivity(intent);
    }

    //Notification
    public static void startActivityFromNotification(Context context, String url) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("notification", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //消息列表
    public static void startActivity(Context context, MessageListRes.MessageBean carousel) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("message", carousel);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "详情");

        project = (OrderCompRes.Project) getIntent().getSerializableExtra("project");
        odd = (OrderSkillFinishRecieveRes.OddBean) getIntent().getSerializableExtra("skill_proj_finish");
        who = getIntent().getIntExtra("who", 0);

        carousel = (HomeResponseBean.Carousel) getIntent().getSerializableExtra("carousel");
        messageBean = (MessageListRes.MessageBean) getIntent().getSerializableExtra("message");

        url = getIntent().getStringExtra("favorite");
        String notification = getIntent().getStringExtra("notification");
        if(!TextUtils.isEmpty(notification)){
            share.setVisibility(View.GONE);

            url = notification;
        }
        if (!TextUtils.isEmpty(url)) {
            //我的收藏
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.GONE);
        }

        if (project != null) {
            url = project.getUrl();
            if (who == COMPANY_RELEASE_PROJECT_WAIT) {
                bottomBtnLayout.setVisibility(View.VISIBLE);
                apply.setVisibility(View.GONE);
                if (project.getStatus() == 4) {
                    isCancle = true;
                }
                presenter.getFindProjDetail(this, project.getProject_id());
            } else if (who == COMPANY_RELEASE_PROJECT_DOING) {
                bottomBtnLayout.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);

            } else if (who == COMPANY_RELEASE_PROJECT_DONE) {
                bottomBtnLayout.setVisibility(View.GONE);
                if (project.getEvaluate_status() == 1 || project.getStatus() == 4) {
                    //已评价
                    apply.setVisibility(View.GONE);
                } else {
                    apply.setVisibility(View.VISIBLE);
                    apply.setText("去评价");
                }

            }
        }


        if (odd != null) {
            url = odd.getUrl();
            if (who == SKILL_RECIEVE_PROJECT) {
                bottomBtnLayout.setVisibility(View.GONE);
                if (odd.getEvaluate_status() == 1) {
                    //已评价
                    apply.setVisibility(View.GONE);
                } else {
                    apply.setVisibility(View.VISIBLE);
                    apply.setText("去评价");
                }
            }
        }

        if(messageBean != null){
            url = messageBean.getUrl();
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }


        if (carousel != null) {
            if (carousel.getType() == 1) {
                //站外
                url = carousel.getLink();
                webView.getWebView().loadUrl(url);
            } else {
                bottomBtnLayout.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                webView.loadUrl(url)
                        .setWebViewClient(webView.new GWebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                super.shouldOverrideUrlLoading(view, url);
                                if (url.contains("action=1")) {
                                    //项目
                                    JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
                                } else if (url.contains("action=2")) {
                                    //零工
                                    JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
                                }
                                return true;
                            }
                        })
                        .setWebChromeClient(webView.new GWebChromeClient(){
                            @Override
                            public void onReceivedTitle(WebView view, String title) {
                                super.onReceivedTitle(view, title);
                                WebViewDetailActivity.this.title = title;
                            }
                        });

            }

        } else {
            webView.loadUrl(url)
                    .setWebViewClient(webView.new GWebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            super.shouldOverrideUrlLoading(view, url);
                            if (url.contains("action=1")) {
                                //项目
                                JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
                            } else if (url.contains("action=2")) {
                                //零工
                                JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
                            }
                            return true;
                        }
                    })
                    .setWebChromeClient(webView.new GWebChromeClient(){
                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            super.onReceivedTitle(view, title);
                            WebViewDetailActivity.this.title = title;
                        }
                    });
        }


        againPost.setOnClickListener(v -> {
            //再次快捷发布
            if (false) {
                ReleaseSkillActivity.startActivity(this);
            } else if (who == COMPANY_RELEASE_PROJECT_WAIT) {
                ReleaseProjActivity.startActivity(this);
            }

        });

        changeInfo.setOnClickListener(v -> {
            //修改项目信息
            if (who == COMPANY_RELEASE_PROJECT_WAIT) {
                if (projectBean != null) {

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

                    req.setRegion(bean.getRegion());
                    req.setProject_type_class(bean.getProject_type_class());
                    req.setProject_type_name(bean.getProject_type_name());
                    req.setProject_id(project.getProject_id());
                    ReleaseProjSecondActivity.startActivity(this, req, true);
                }

            }
        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "update_project"))
                .subscribe(s -> {
                    finish();
                });

        apply.setOnClickListener(v -> {
            //评价 1 项目
            if (who == SKILL_RECIEVE_PROJECT) {
                RecommandActivity.startActivity(this,
                        odd.getProject_id(), 1);
            } else {
                RecommandActivity.startActivity(this,
                        odd.getProject_id(), 1);
            }

        });


        share.setOnClickListener(v -> {

            myShareSDK = new ShareSDK();
            myShareSDK.initSDK(this);
            WechatMoments.ShareParams sp=new WechatMoments.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setUrl(url);
            sp.setTitleUrl(url);
            if(project != null){
                sp.setText(project.getTitle());
                sp.setTitle(project.getTitle());
            }else if(odd != null){
                sp.setText(odd.getTitle());
                sp.setTitle(odd.getTitle());
            }else if(carousel != null){
                sp.setText(carousel.getName());
                sp.setTitle(carousel.getName());
            }else{
                sp.setText(title);
                sp.setTitle(title);
            }



            Platform wx = myShareSDK.getPlatform (WechatMoments.NAME);
            wx.share(sp);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (who == Constants.COMPANY_RELEASE_PROJECT_WAIT) {
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                //取消项目
                presenter.myProjectCancel(this, project.getProject_id());
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
    public void myProjectCancel(Object res) {
        ToastUtil.show("取消项目成功");
        finish();
    }

    @Override
    public void getData(FindProjDetailRes res) {
        //拿到项目详情
        projectBean = res;
    }
}
