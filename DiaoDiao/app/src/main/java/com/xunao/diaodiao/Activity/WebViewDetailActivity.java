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
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.OrderSkillFinishRecieveRes;
import com.xunao.diaodiao.Bean.OrderSkillRes;
import com.xunao.diaodiao.Bean.ReleaseHelpReq;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.WeiBaoDetailRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.WebViewDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.WebViewDetailView;
import com.xunao.diaodiao.Widget.CustomWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_HUZHU_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_WAIT;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO_WAIT;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

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
    HomeResponseBean.Advertisement advertisement;


    int who;
    String url;
    @BindView(R.id.share)
    FloatingActionButton share;
    private boolean isCancle = false;

    private FindProjDetailRes projectBean;
    private WeiBaoDetailRes weiBaoBean;
    private FindLingGongRes oddBean;
    private OrderSkillRes.OddBean skillWBBean;

    private FindProjectRes.FindProject huzhuProject;

    private ShareSDK myShareSDK;
    private String title;
    private String phone;

    //技术人员 发布维保
    public static void startActivity(Context context, OrderSkillRes.OddBean bean, int who) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("weibao", bean);
        intent.putExtra("who", who);
        context.startActivity(intent);
    }


    //暖通公司 项目
    public static void startActivity(Context context, FindProjectRes.FindProject bean) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("huzhu", bean);
        context.startActivity(intent);
    }

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

    //首页广告
    public static void startActivity(Context context, HomeResponseBean.Advertisement advertisement) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("advertisement", advertisement);
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

    //合作商家
    public static void startActivity(Context context, String url, int who) {
        Intent intent = new Intent(context, WebViewDetailActivity.class);
        intent.putExtra("company", url);
        intent.putExtra("companyInfo", who);
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
        advertisement = (HomeResponseBean.Advertisement) getIntent().getSerializableExtra("advertisement");
        skillWBBean = (OrderSkillRes.OddBean) getIntent().getSerializableExtra("weibao");

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

        huzhuProject = (FindProjectRes.FindProject) getIntent().getSerializableExtra("huzhu");
        //互助
        if(huzhuProject != null){
            url = huzhuProject.getUrl();
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.VISIBLE);
            apply.setText("联系ta");
            phone = huzhuProject.getContact_mobile();
        }

        if (project != null) {
            url = project.getUrl();
            if (who == COMPANY_RELEASE_PROJECT_WAIT ||
                    who == COMPANY_RELEASE_WEIBAO_WAIT ||
                        who == COMPANY_RELEASE_JIANLI_WAIT ||
                            who == COMPANY_RELEASE_HUZHU_WAIT) {
                bottomBtnLayout.setVisibility(View.VISIBLE);
                apply.setVisibility(View.GONE);
                if (project.getStatus() == 4) {
                    isCancle = true;
                }
                if(who == COMPANY_RELEASE_PROJECT_WAIT){
                    presenter.getFindProjDetail(this, project.getProject_id(), who);
                }else if(who == COMPANY_RELEASE_WEIBAO_WAIT){
                    presenter.getFindWBDetail(this, project.getMaintenance_id(), who);
                }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
                    showToolbarBack(toolBar, titleText, "修改监理信息");
                    presenter.getFindProjDetail(this, project.getSupervisor_id(), who);
                }else if(who == COMPANY_RELEASE_HUZHU_WAIT){
                    bottomBtnLayout.setVisibility(View.GONE);
                    presenter.getFindProjDetail(this, project.getMutual_id(), who);
                }


            } else if (who == COMPANY_RELEASE_PROJECT_DOING ||
                            who == COMPANY_RELEASE_WEIBAO_DOING ||
                                who == COMPANY_RELEASE_JIANLI_DOING ) {
                bottomBtnLayout.setVisibility(View.GONE);
                apply.setVisibility(View.VISIBLE);
                apply.setText("联系ta");
                //phone = "110";
            } else if (who == COMPANY_RELEASE_PROJECT_DONE ||
                            who == COMPANY_RELEASE_WEIBAO_DONE ||
                                who == COMPANY_RELEASE_JIANLI_DONE) {
                bottomBtnLayout.setVisibility(View.GONE);
                if (project.getEvaluate_status() == 1 || project.getStatus() == 4) {
                    //已评价
                    apply.setVisibility(View.GONE);
                } else {
                    apply.setVisibility(View.VISIBLE);
                    apply.setText("去评价");
                }

            }else if(who == COMPANY_RELEASE_HUZHU_DONE){
                bottomBtnLayout.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
            }
        }


        if (odd != null) {
            url = odd.getUrl();
            if (who == SKILL_RECIEVE_PROJECT ||
                    (who == SKILL_RECIEVE_WEIBAO) ||
                        (who == SKILL_RECIEVE_JIANLI) ||
                            (who == SKILL_RECIEVE_LINGGONG)) {
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

        if(advertisement != null){
            url = advertisement.getLink();
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }

        if(skillWBBean != null){
            url = skillWBBean.getUrl();
            bottomBtnLayout.setVisibility(View.VISIBLE);
            changeInfo.setText("修改维保信息");
            apply.setVisibility(View.GONE);
            presenter.getFindWBDetail(this,skillWBBean.getMaintenance_id(),COMPANY_RELEASE_WEIBAO_WAIT);
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
                        .setWebChromeClient(webView.new GWebChromeClient(){
                            @Override
                            public void onReceivedTitle(WebView view, String title) {
                                super.onReceivedTitle(view, title);
                                WebViewDetailActivity.this.title = title;
                            }
                        })
                        .setWebViewClient(webView.new GWebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                super.shouldOverrideUrlLoading(view, url);
//                                if (url.contains("action=1")) {
//                                    //项目
//                                    JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
//                                } else if (url.contains("action=2")) {
//                                    //零工
//                                    JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
//                                }
                                return true;
                            }
                        });


            }

        }else if(TextUtils.isEmpty(url)){
            url = getIntent().getStringExtra("company");
            if(!TextUtils.isEmpty(url)){
                //url = "https://map.baidu.com/mobile/webapp/index/index/foo=bar/vt=map";
                webView.getWebView().loadUrl(url);
                //url = URLEncoder.encode(url, "utf-8");

                bottomBtnLayout.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
            }

        }
        else {
            webView.loadUrl(url)
                    .setWebViewClient(webView.new GWebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            super.shouldOverrideUrlLoading(view, url);
//                            if (url.contains("action=1")) {
//                                //项目
//                                JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
//                            } else if (url.contains("action=2")) {
//                                //零工
//                                JoinDetailActivity.startActivity(WebViewDetailActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
//                            }
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
            if (who == SKILL_RELEASE_LINGGONG) {
                ReleaseSkillActivity.startActivity(this);
            } else if (who == COMPANY_RELEASE_PROJECT_WAIT) {
                ReleaseProjActivity.startActivity(this, false);
            }else if(who == COMPANY_RELEASE_WEIBAO_WAIT ||
                        who == SKILL_RELEASE_WEIBAO_WAIT){
                ReleaseHelpActivity.startActivity(this);
            }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
                ReleaseProjActivity.startActivity(this, true);
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

            }else if(who == COMPANY_RELEASE_WEIBAO_WAIT ||
                        who == SKILL_RELEASE_WEIBAO_WAIT){
                if(weiBaoBean != null){
                    WeiBaoDetailRes.WBDetailBean bean = weiBaoBean.getDetail();
                    ReleaseHelpReq req = new ReleaseHelpReq();
                    req.setProject_type(bean.getProject_type());
                    req.setProject_class(bean.getProject_class());
                    req.setTitle(bean.getTitle());
                    req.setProvince(bean.getProvince());
                    req.setCity(bean.getCity());
                    req.setDistrict(bean.getDistrict());
                    req.setAddress(bean.getAddress());
                    req.setContact(bean.getContact());
                    req.setContact_mobile(bean.getContact_mobile());
                    req.setDescribe(bean.getDescribe());
                    req.setImages(bean.getImages());
                    req.setService_fee(bean.getService_fee());
                    req.setDoor_fee(bean.getDoor_fee());
                    req.setDoor_time(Utils.convertTime2long(bean.getDoor_time()));
                    req.setBuildTimeString(bean.getDoor_time());
                    req.setRegion(bean.getRegion());
                    req.setEquip_type(weiBaoBean.getMaintenance().getEquip_type());
                    req.setMaintenance_id(bean.getMaintenance_id());
                    ReleaseSkillInforActivity.startActivity(this, req, true);
                }
            }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
                if(projectBean != null){
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
                    req.setSupervisor_time(Utils.convertTime2long(bean.getSupervisor_time()));
                    req.setBuild_time_string(bean.getSupervisor_time());
                    req.setDescribe(bean.getDescribe());
                    req.setImages(bean.getImages());
                    req.setService_cost(bean.getService_cost());
                    req.setSupervisor_fee(bean.getSupervisor_fee());
                    req.setTotal_price(bean.getTotal_price());

                    req.setRegion(bean.getRegion());
                    req.setProject_type_class(bean.getProject_type_class());
                    req.setProject_type_name(bean.getProject_type_name());
                    ReleaseProjSecondActivity.startActivity(this, req, true, true);
                }
            }
        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "update_project"))
                .subscribe(s -> {
                    finish();
                });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });

        apply.setOnClickListener(v -> {
            //评价 1 项目
            if (who == SKILL_RECIEVE_PROJECT) {
                RecommandActivity.startActivity(this,
                        odd.getProject_id(), 1);
            } else if(who == SKILL_RECIEVE_WEIBAO){
                RecommandActivity.startActivity(this,
                        odd.getMaintenance_id(), 4);
            }else if(who == SKILL_RECIEVE_JIANLI){
                RecommandActivity.startActivity(this,
                        odd.getSupervisor_id(), 2);
            }else if(who == COMPANY_RELEASE_PROJECT_DONE){
                RecommandActivity.startActivity(this,
                        project.getProject_id(), 1);
            }else if(who == COMPANY_RELEASE_JIANLI_DONE){
                RecommandActivity.startActivity(this,
                        project.getSupervisor_id(), 2);
            }else if(who == COMPANY_RELEASE_WEIBAO_DONE){
                RecommandActivity.startActivity(this,
                        project.getMaintenance_id(), 4);
            }else if(who == SKILL_RECIEVE_LINGGONG){
                RecommandActivity.startActivity(this,
                        odd.getOdd_id(), 3);
            }

            if (TextUtils.equals("联系ta", apply.getText().toString())){
                if(!TextUtils.isEmpty(phone)){
                    new IOSDialog(WebViewDetailActivity.this).builder()
                            .setMsg(phone)
                            .setNegativeButton("呼叫", v1 -> {
                                Utils.startCallActivity(this, phone);
                            })
                            .setPositiveButton("取消", null)
                            .show();
                }

            }

        });


        share.setOnClickListener(v -> {

            showPicDialog();


        });

        if (ShareUtils.getValue(TYPE_KEY, 0) == 3) {
            //公司角色
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.GONE);
        }

        webView.getWebView().addJavascriptInterface(new AndroidtoJs(), "AndroidToJS");

    }

    private void showPicDialog() {
        new IOSDialog(this).builder()
                .setCancelable(true)
                .setTitle("朋友圈", v -> {
                    friends();
                })
                .setMsg("好友", v -> {
                    friend();
                })
                .setMsgSize(R.dimen.dialog_msg_size)
                .setMsgColor("#333333")
                .setNegativeButton("取消", null)
                .show();
    }

    private void friend(){
        myShareSDK = new ShareSDK();
        myShareSDK.initSDK(this);
        //WechatMoments.ShareParams sp=new WechatMoments.ShareParams();
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        url += "&hd=1";
        sp.setUrl(url);
        sp.setTitleUrl(url);
        sp.setImageUrl("http://api.diao-diao.com/images/logo.png");
        sp.setText("我分享了来自调调居服的"+title+"信息，快来看看吧！");
        title = "调调居服分享信息";
        sp.setTitle(title);

        Platform wx = myShareSDK.getPlatform (Wechat.NAME);
        wx.share(sp);
    }

    private void friends(){
        myShareSDK = new ShareSDK();
        myShareSDK.initSDK(this);
        WechatMoments.ShareParams sp=new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        url += "&hd=1";
        sp.setUrl(url);
        sp.setTitleUrl(url);
        sp.setImageUrl("http://api.diao-diao.com/images/logo.png");
        sp.setText("我分享了来自调调居服的"+title+"信息，快来看看吧！");
        title = "调调居服分享信息";
        sp.setTitle(title);

        Platform wx = myShareSDK.getPlatform (WechatMoments.NAME);
        wx.share(sp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (who == Constants.COMPANY_RELEASE_PROJECT_WAIT) {
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
        }else if(who == COMPANY_RELEASE_WEIBAO_WAIT){
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
            menu.findItem(R.id.action_contact).setTitle("取消维保");
        }else if(who == COMPANY_RELEASE_JIANLI_WAIT){
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
            menu.findItem(R.id.action_contact).setTitle("取消监理");
        }else if(who ==SKILL_RELEASE_WEIBAO_WAIT){
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
            menu.findItem(R.id.action_contact).setTitle("取消维保");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                if(who == COMPANY_RELEASE_WEIBAO_WAIT){
                    //取消维保
                    presenter.myProjectCancel(this, project.getMaintenance_id(), who);
                }else if(who == COMPANY_RELEASE_JIANLI_WAIT) {
                    //取消监理
                    presenter.myProjectCancel(this, project.getSupervisor_id(), who);
                }else if(who == SKILL_RELEASE_WEIBAO_WAIT) {
                    //取消维保
                    presenter.myProjectCancel(this, skillWBBean.getMaintenance_id(), who);
                }else {
                    //取消项目
                    presenter.myProjectCancel(this, project.getProject_id(), who);
                }

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
        ToastUtil.show("取消成功");
        finish();
    }

    @Override
    public void getData(FindProjDetailRes res) {
        //拿到项目详情
        projectBean = res;
    }

    @Override
    public void getData(WeiBaoDetailRes res) {
        //维保详情
        weiBaoBean = res;
    }

    public class AndroidtoJs extends Object{
        @JavascriptInterface
        public void goToMap(String url) {
            WebViewOutActivity.startActivity(WebViewDetailActivity.this, url);
        }
    }
}
