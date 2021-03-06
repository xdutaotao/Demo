package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Present.SkillProjReceiveProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.SkillProjReceiveProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_PROJECT;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * 项目进度
 * create by
 */
public class SkillProjReceiveProgressActivity extends BaseActivity implements SkillProjReceiveProgressView {

    @Inject
    SkillProjReceiveProgressPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sign)
    Button sign;
//    @BindView(R.id.swipe)
//    com.gzfgeh.swipeheader.SwipeRefreshLayout swipe;

    private RecyclerArrayAdapter<MyAcceptProjectWorkRes.WorkBean> adapter;
    private String phone;
    private int who;
    private int applyID;
    private int id;

    public static void startActivity(Context context, int id, int who) {
        Intent intent = new Intent(context, SkillProjReceiveProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("WHO", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_proj_progress);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "工作进度");

        who = getIntent().getIntExtra("WHO", 0);
        if (who == COMPANY_RELEASE_PROJECT_DONE || who == COMPANY_RELEASE_PROJECT_DOING) {
            sign.setText("签到详情");
        }

        adapter = new RecyclerArrayAdapter<MyAcceptProjectWorkRes.WorkBean>(this, R.layout.skill_proj_receive_proj_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyAcceptProjectWorkRes.WorkBean workBean) {
                baseViewHolder.setText(R.id.title, workBean.getTitle());
                if (workBean.getStage1() == 0) {
                    baseViewHolder.setText(R.id.progress, "第一阶段未开始");
                    baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                    baseViewHolder.setText(R.id.middle_text, "未开始");
                    baseViewHolder.setVisible(R.id.middle_btn, false);
                    baseViewHolder.setVisible(R.id.middle, false);
                    baseViewHolder.setVisible(R.id.middle_text, true);

                    baseViewHolder.setVisible(R.id.status_btn, false);
                    baseViewHolder.setVisible(R.id.status_text, true);
                    baseViewHolder.setVisible(R.id.status, false);
                } else if (workBean.getStage1() == 1) {
                    baseViewHolder.setText(R.id.progress, "第一阶段进行中");
                    baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                    baseViewHolder.setVisible(R.id.status_btn, false);
                    baseViewHolder.setVisible(R.id.status_text, true);
                    baseViewHolder.setVisible(R.id.status, false);
                    baseViewHolder.setVisible(R.id.middle_text, false);
                    if (who == SKILL_RECIEVE_PROJECT) {
                        baseViewHolder.setVisible(R.id.middle_btn, true);
                        baseViewHolder.setVisible(R.id.middle, false);


                    } else if (who == COMPANY_RELEASE_PROJECT_DOING) {
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle, true);

                    } else if (who == COMPANY_RELEASE_PROJECT_DONE) {
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                    }else{
                        baseViewHolder.setVisible(R.id.middle_btn, true);
                        baseViewHolder.setVisible(R.id.middle, true);
                    }

                } else if (workBean.getStage1() == 2) {
                    //第一阶段完成了
                    if (workBean.getStage2() == 0) {
                        baseViewHolder.setText(R.id.progress, "第一阶段已完成");
                        baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle_text, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                        baseViewHolder.setText(R.id.middle, "已完成");

                        baseViewHolder.setVisible(R.id.status_btn, false);
                        baseViewHolder.setVisible(R.id.status_text, true);
                        baseViewHolder.setVisible(R.id.status, false);
                    } else if (workBean.getStage2() == 1) {
                        baseViewHolder.setText(R.id.progress, "第二阶段进行中");
                        baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle_text, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                        baseViewHolder.setText(R.id.middle, "已完成");
                        baseViewHolder.setVisible(R.id.status_text, false);
                        if (who == SKILL_RECIEVE_PROJECT) {
                            baseViewHolder.setVisible(R.id.status_btn, true);
                            baseViewHolder.setVisible(R.id.status, false);
                        } else if (who == COMPANY_RELEASE_PROJECT_DOING) {
                            baseViewHolder.setVisible(R.id.status_btn, false);
                            baseViewHolder.setVisible(R.id.status, true);
                        } else if (who == COMPANY_RELEASE_PROJECT_DONE) {
                            baseViewHolder.setVisible(R.id.status_btn, false);
                            baseViewHolder.setVisible(R.id.status, true);
                        }else{
                            baseViewHolder.setVisible(R.id.status_btn, true);
                            baseViewHolder.setVisible(R.id.status, true);
                        }


                    } else if (workBean.getStage2() == 2) {
                        //第二阶段已完成
                        baseViewHolder.setText(R.id.progress, "第二阶段已完成");
                        baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle_text, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                        baseViewHolder.setText(R.id.middle, "已完成");

                        baseViewHolder.setVisible(R.id.status_btn, false);
                        baseViewHolder.setVisible(R.id.status_text, false);
                        baseViewHolder.setVisible(R.id.status, true);
                        baseViewHolder.setText(R.id.status, "已完成");
                    } else if (workBean.getStage2() == 3) {
                        //第二阶段审核
                        baseViewHolder.setText(R.id.progress, "第二阶段审核中");
                        baseViewHolder.setTextColorRes(R.id.progress, R.color.colorAccent);
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle_text, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                        baseViewHolder.setText(R.id.middle, "已完成");

                        baseViewHolder.setVisible(R.id.status_btn, false);
                        baseViewHolder.setVisible(R.id.status_text, false);
                        baseViewHolder.setVisible(R.id.status, true);
                        baseViewHolder.setText(R.id.status, "审核中");
                    } else if (workBean.getStage2() == 4) {
                        //审核失败
                        baseViewHolder.setText(R.id.progress, "第二阶段审核不通过");
                        baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                        baseViewHolder.setVisible(R.id.middle_btn, false);
                        baseViewHolder.setVisible(R.id.middle_text, false);
                        baseViewHolder.setVisible(R.id.middle, true);
                        baseViewHolder.setText(R.id.middle, "已完成");

                        baseViewHolder.setVisible(R.id.status_btn, false);
                        baseViewHolder.setVisible(R.id.status_text, false);
                        baseViewHolder.setVisible(R.id.status, true);
                        baseViewHolder.setText(R.id.status, "审核不通过");
                    }

                } else if (workBean.getStage1() == 3) {
                    //第一阶段审核中
                    baseViewHolder.setText(R.id.progress, "第一阶段审核中");
                    baseViewHolder.setTextColorRes(R.id.progress, R.color.colorAccent);
                    baseViewHolder.setVisible(R.id.middle_btn, false);
                    baseViewHolder.setVisible(R.id.middle_text, false);
                    baseViewHolder.setVisible(R.id.middle, true);
                    baseViewHolder.setText(R.id.middle, "审核中");

                    baseViewHolder.setVisible(R.id.status_btn, false);
                    baseViewHolder.setVisible(R.id.status_text, true);
                    baseViewHolder.setVisible(R.id.status, false);
                } else if (workBean.getStage1() == 4) {
                    baseViewHolder.setText(R.id.progress, "第一阶段审核不通过");
                    baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                    baseViewHolder.setVisible(R.id.middle_btn, false);
                    baseViewHolder.setVisible(R.id.middle_text, false);
                    baseViewHolder.setVisible(R.id.middle, true);
                    baseViewHolder.setText(R.id.middle, "审核不通过");

                    baseViewHolder.setVisible(R.id.status_btn, false);
                    baseViewHolder.setVisible(R.id.status_text, true);
                    baseViewHolder.setVisible(R.id.status, false);
                }else{
                    baseViewHolder.setText(R.id.progress, "第一阶段审核不通过");
                    baseViewHolder.setTextColorRes(R.id.progress, R.color.accept_btn_default);
                    baseViewHolder.setVisible(R.id.middle_btn, true);
                    baseViewHolder.setVisible(R.id.middle_text, true);
                    baseViewHolder.setVisible(R.id.middle, true);
                    baseViewHolder.setText(R.id.middle, "审核不通过");

                    baseViewHolder.setVisible(R.id.status_btn, true);
                    baseViewHolder.setVisible(R.id.status_text, true);
                    baseViewHolder.setVisible(R.id.status, true);
                }

                baseViewHolder.setOnClickListener(R.id.middle_btn, v -> {
                    SkillProjProgressActivity.startActivity(SkillProjReceiveProgressActivity.this,
                            getIntent().getIntExtra(INTENT_KEY, 0), workBean.getWorks_id(),
                            workBean.getStage1() == 2 ? 2 : 1, who);
                });

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            SkillProjProgressActivity.startActivity(SkillProjReceiveProgressActivity.this,
                    getIntent().getIntExtra(INTENT_KEY, 0),
                    adapter.getAllData().get(i).getWorks_id(),
                    adapter.getAllData().get(i).getStage1() == 2 ? 2 : 1, who);
        });

        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //swipe.setOnRefreshListener(this);


        sign.setOnClickListener(v -> {
            SignDetailActivity.startActivity(SkillProjReceiveProgressActivity.this,
                    getIntent().getIntExtra(INTENT_KEY, 0), who);

        });
    }

//    @Override
//    public void onRefresh() {
//        presenter.myAcceptProjectWork(getIntent().getIntExtra(INTENT_KEY, 0), who);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //onRefresh();
        presenter.myAcceptProjectWork(getIntent().getIntExtra(INTENT_KEY, 0), who);
    }

    @Override
    public void getData(MyAcceptProjectWorkRes res) {
        //swipe.setRefreshing(false);
        if (res.getWorks() != null) {
            MyAcceptProjectWorkRes.WorksBean worksBean = res.getWorks();
            title.setText(worksBean.getTitle());
            addressDetail.setText(worksBean.getAddress());
            address.setText(worksBean.getRegion());
            adapter.clear();
            adapter.addAll(worksBean.getWork());
            phone = worksBean.getContact_mobile();
            applyID = worksBean.getApply_id();
            id = worksBean.getId();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_collect, menu);
//        menu.findItem(R.id.action_contact).setIcon(R.drawable.phone);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:


                if (ShareUtils.getValue(TYPE_KEY, 0) == 1) {
                    //技术员
                    ApplyPassReq applyPassReq = new ApplyPassReq();
                    applyPassReq.setTechnician_id(applyID);
                    applyPassReq.setProject_id(1000);
                    ApplyDetailActivity.startActivity(this, applyPassReq);

                } else {
                    //公司详情
                    JoinDetailActivity.startActivity(this, id, phone);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
