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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.CollectRes;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Fragment.MyFragment;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ProjectDetailView;
import com.xunao.diaodiao.Widget.CustomWebView;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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
    @BindView(R.id.share)
    FloatingActionButton share;

    private int type;
    private boolean isCollect = false;
    private boolean isApply = false;
    private int id;
    private String btnType;
    private String url;

    public static final String LG_DETAIL = "release_lg_detail";
    public static final String WEIBAO_DETAIL = "release_wb_detail";
    public static final String RECEIVE_LG_DETAIL = "receive_release_lg_detail";
    public static final String RECEIVE_PROJ_DETAIL = "receive_release_proj_detail";
    public static final String RECEIVE_WEIBAO_DETAIL = "receive_release_wb_detail";
    public static final String RECEIVE_JIANLI_DETAIL = "receive_release_jl_detail";
    public static final String HOME_DETAIL = "home_detail";
    public static final String SKILL_RECIEVE_PROJECT_DOING = "SKILL_RECIEVE_PROJECT_DOING";
    public static final String SKILL_RECIEVE_WEIBAO_DOING = "SKILL_RECIEVE_WEIBAO_DOING";
    public static final String SKILL_RECIEVE_JIANLI_DOING = "SKILL_RECIEVE_JIANLI_DOING";
    public static final String HOME_SKILL_DETAIL = "home_skill_detail";
    public static final String HOME_WEIBAO_DETAIL = "home_weibao_detail";
    public static final String HOME_JIANLI_DETAIL = "home_jianli_detail";
    public static final String COMPANY_PROJ = "company_proj";
    /**
     * 1项目2监理3零工4维保
     */
    private int project_type;

    private FindProjDetailRes projectBean;
    private FindLingGongRes oddBean;

    private FindProjectRes.FindProject findProject;

    private ShareSDK myShareSDK;
    private String title;
    private int collectID;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        context.startActivity(intent);
    }

    //首页轮播
    public static void startActivity(Context context, String url, String id, int type) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(TITLE_KEY, id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    //底部没有button
    public static void startActivity(Context context, String url, int id) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        context.startActivity(intent);
    }

    //是否收藏
    public static void startActivity(Context context, String url, int id, String btnType, int isCollect) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, url);
        intent.putExtra(ID_KEY, id);
        intent.putExtra("BTN_TYPE", btnType);
        intent.putExtra("isCollect", isCollect);
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


    //首页进来
    public static void startActivity(Context context, FindProjectRes.FindProject projectBean, String btnType) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("project_bean", projectBean);
        intent.putExtra("BTN_TYPE", btnType);
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


        findProject = (FindProjectRes.FindProject) getIntent().getSerializableExtra("project_bean");
        if (findProject != null) {
            id = findProject.getId();
            url = findProject.getUrl();
            isCollect = findProject.getCollected() == 1 ? true : false;
            isApply = findProject.getApply() == 1 ? true : false;
            bottomBtnLayout.setVisibility(View.GONE);
        }else{
            isCollect = getIntent().getIntExtra("isCollect", 0) == 1? true: false;
        }


        webView.loadUrl(url)
                .setWebViewClient(webView.new GWebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        super.shouldOverrideUrlLoading(view, url);
                        if (url.contains("action=1")) {
                            //项目
                            JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
                        } else if (url.contains("action=2")) {
                            //零工
                            JoinDetailActivity.startActivity(WebViewActivity.this, getIntent().getIntExtra(INTENT_KEY, 0), 2);
                        }

                        //collectID =

                        return true;
                    }
                })
                .setWebChromeClient(webView.new GWebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        WebViewActivity.this.title = title;
                    }
                });

        try {
            collectID = Integer.valueOf(Utils.getValueByName(url, "collect_id"));
        }catch (Exception e){
            e.printStackTrace();
        }


        apply.setOnClickListener(v -> {
            if (TextUtils.equals(btnType, HOME_DETAIL)) {
                if (ShareUtils.getValue(TYPE_KEY, 0) == 0) {
                    ToastUtil.show("请完善个人信息");
                    return;
                }

                if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                    ToastUtil.show("请登录");
                    return;
                }

                presenter.postProject(this, id, type);
            } else if (TextUtils.equals(btnType, HOME_SKILL_DETAIL)) {
                //零工申请
                presenter.postProject(this, id, 1);
            } else if(TextUtils.equals(btnType, HOME_WEIBAO_DETAIL)) {
                //维保申请
                presenter.postProject(this, id, 2);

            }else if(TextUtils.equals(btnType, HOME_JIANLI_DETAIL)) {
                presenter.postProject(this, id, 4);
            }else {
                if (project_type == 0) {
                    //联系发布人

                    new IOSDialog(this).builder()
                            .setMsg(Constants.tel)
                            .setNegativeButton("取消", null)
                            .setNegativeBtnColor(R.color.accept_btn_default)
                            .setPositiveBtnColor(R.color.accept_btn_default)
                            .setPositiveButton("呼叫", v1 -> {
                                Utils.startCallActivity(this, Constants.tel);
                            })
                            .show();


                } else if (project_type == 1) {
                    // 1 项目
                    RecommandActivity.startActivity(this, id, project_type);
                }

            }

        });

        againPost.setOnClickListener(v -> {
            //再次快捷发布
            if (TextUtils.equals(LG_DETAIL, btnType)) {
                ReleaseSkillActivity.startActivity(this);
            } else if (TextUtils.equals(btnType, HOME_DETAIL)) {
                ReleaseProjActivity.startActivity(this, false);
            }else if(TextUtils.equals(btnType, WEIBAO_DETAIL)){
                ReleaseHelpActivity.startActivity(this);
            }

        });

        changeInfo.setOnClickListener(v -> {
            //修改项目信息
            if (TextUtils.equals(HOME_DETAIL, btnType)) {
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

                    req.setRegion(projectBean.getProject().getRegion());
                    req.setProject_type_class(bean.getProject_type_class());
                    req.setProject_type_name(bean.getProject_type_name());
                    req.setProject_id(id);
                    ReleaseProjSecondActivity.startActivity(this, req, true);
                }
            } else if (TextUtils.equals(LG_DETAIL, btnType)) {
                //零工详情
                if (oddBean != null) {
                    FindLingGongRes.DetailBean bean = oddBean.getDetail();
                    ReleaseSkillReq req = new ReleaseSkillReq();
                    req.setTitle(bean.getTitle());
                    req.setProvince(bean.getProvince());
                    req.setCity(bean.getCity());
                    req.setDistrict(bean.getDistrict());
                    req.setAddress(bean.getAddress());
                    req.setProject_type(bean.getType());
                    req.setContact(bean.getContact());
                    req.setContact_mobile(bean.getContact_mobile());
                    req.setDaily_wage(bean.getDaily_wage());
                    req.setTotal_day(bean.getTotal_day());
                    req.setBuild_time(bean.getBuild_time());
                    req.setDescribe(bean.getDescribe());
                    req.setService_fee(bean.getService_fee());
                    req.setOdd_fee(bean.getOdd_fee());
                    req.setTotal_fee(bean.getTotal_fee());
                    req.setImages(bean.getImages());
                    req.setRegion(bean.getRegion());
                    req.setPeople_numbers(bean.getPeople_numbers());
                    ReleaseSkillActivity.startActivity(this, req);
                }
            }else if(TextUtils.equals(WEIBAO_DETAIL, btnType)){
                //维保

            }
        });

        if (TextUtils.equals(HOME_DETAIL, btnType) ||
                TextUtils.equals(HOME_WEIBAO_DETAIL, btnType) ||
                    TextUtils.equals(HOME_JIANLI_DETAIL, btnType)) {
            //首页
            bottomBtnLayout.setVisibility(View.GONE);
            if (isApply) {
                apply.setVisibility(View.GONE);
            } else {
                apply.setVisibility(View.VISIBLE);
            }

        } else if (TextUtils.equals(HOME_SKILL_DETAIL, btnType)) {
            //首页 找零工详情
            bottomBtnLayout.setVisibility(View.GONE);
            if (isApply) {
                apply.setVisibility(View.GONE);
            } else {
                apply.setVisibility(View.VISIBLE);
            }
        } else if (TextUtils.equals(LG_DETAIL, btnType)) {
            //零工详情
            bottomBtnLayout.setVisibility(View.VISIBLE);
            apply.setVisibility(View.GONE);
            changeInfo.setText("修改零工信息");
            presenter.getFindLingGongDetail(this, id);

        }else if(TextUtils.equals(WEIBAO_DETAIL, btnType)) {
            //维保详情
            bottomBtnLayout.setVisibility(View.VISIBLE);
            apply.setVisibility(View.GONE);
            changeInfo.setText("修改维保信息");
            presenter.getFindWBDetail(this, id);

        }else if (TextUtils.equals(btnType, RECEIVE_LG_DETAIL)
                || TextUtils.equals(btnType, RECEIVE_WEIBAO_DETAIL)
                || TextUtils.equals(btnType, RECEIVE_JIANLI_DETAIL)) {
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setText("联系发布人");
            apply.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(btnType, RECEIVE_PROJ_DETAIL)) {
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setText("联系发布人");
            apply.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(btnType, COMPANY_PROJ)) {
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(btnType, SKILL_RECIEVE_PROJECT_DOING)
                || TextUtils.equals(btnType, SKILL_RECIEVE_JIANLI_DOING)
                || TextUtils.equals(btnType, SKILL_RECIEVE_WEIBAO_DOING)) {
            //技术人员 发布项目 进行中
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.GONE);
        } else {
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
        }

        if (project_type != 0) {
            bottomBtnLayout.setVisibility(View.GONE);
            apply.setVisibility(View.VISIBLE);
            apply.setText("去评价");
        }

//        if (ShareUtils.getValue(TYPE_KEY, 0) == 1) {
//            //公司角色
//            apply.setVisibility(View.GONE);
//            presenter.getFindProjDetail(this, id, 0);
//        }

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "update_project"))
                .subscribe(s -> {
                    finish();
                });

        share.setOnClickListener(v -> {
            showPicDialog();

        });

        if(TextUtils.isEmpty(User.getInstance().getUserId())
                || (ShareUtils.getValue(TYPE_KEY, 0) == 0)){
            apply.setVisibility(View.GONE);
            bottomBtnLayout.setVisibility(View.GONE);
        }

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
        sp.setText(title);
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
        sp.setText(title);
        sp.setTitle(title);

        Platform wx = myShareSDK.getPlatform (WechatMoments.NAME);
        wx.share(sp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (TextUtils.equals(btnType, HOME_DETAIL) ||
                TextUtils.equals(btnType, SKILL_RECIEVE_PROJECT_DOING) ||
                    TextUtils.equals(btnType, HOME_SKILL_DETAIL) ||
                        TextUtils.equals(btnType, SKILL_RECIEVE_WEIBAO_DOING) ||
                            TextUtils.equals(btnType, SKILL_RECIEVE_JIANLI_DOING)) {
            getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
            if (isCollect) {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02_fill);
            } else {
                menu.findItem(R.id.action_like).setIcon(R.drawable.icon_shoucang02);
            }
        } else if (TextUtils.equals(btnType, COMPANY_PROJ)) {
            getMenuInflater().inflate(R.menu.menu_web_view_proj, menu);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (TextUtils.equals(btnType, HOME_DETAIL) ||
                TextUtils.equals(btnType, SKILL_RECIEVE_PROJECT_DOING) ||
                    TextUtils.equals(btnType, HOME_SKILL_DETAIL)||
                        TextUtils.equals(btnType, SKILL_RECIEVE_WEIBAO_DOING) ||
                            TextUtils.equals(btnType, SKILL_RECIEVE_JIANLI_DOING)) {
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
                if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
                    if (TextUtils.equals(btnType, HOME_DETAIL) ||
                            TextUtils.equals(btnType, SKILL_RECIEVE_PROJECT_DOING) ||
                                TextUtils.equals(btnType, HOME_SKILL_DETAIL)||
                                    TextUtils.equals(btnType, SKILL_RECIEVE_WEIBAO_DOING) ||
                                        TextUtils.equals(btnType, SKILL_RECIEVE_JIANLI_DOING)) {
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

                        if(TextUtils.equals(btnType, SKILL_RECIEVE_WEIBAO_DOING)){
                            //维保
                            types = 4;
                        }else if(TextUtils.equals(btnType, SKILL_RECIEVE_JIANLI_DOING)){
                            //监理
                            types = 2;
                        }


                        if (!isCollect) {
                            presenter.collectWork(this, getIntent().getIntExtra(ID_KEY, 0), types);
                        } else {
                            presenter.cancelCollect(this, collectID);
                        }
                    } else if (TextUtils.equals(btnType, COMPANY_PROJ)) {

                    }
                }

                return true;

            case R.id.action_contact:
                //取消项目
                presenter.myProjectCancel(this, id, 0);
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
        //零工详情
        oddBean = res;
    }

    @Override
    public void postProject(Object res) {
        ToastUtil.show("申请成功");
        finish();
    }

    @Override
    public void collectWork(CollectRes s) {
        ToastUtil.show("收藏成功");
        isCollect = true;
        collectID = s.getCollect_id();
        invalidateOptionsMenu();
    }

    @Override
    public void cancleCollect(Object s) {
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
