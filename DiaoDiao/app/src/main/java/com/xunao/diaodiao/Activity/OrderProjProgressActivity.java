package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Present.OrderProjProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderProjProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DONE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.JIA_TYPE;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_DONE;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_NO_PASS;

/**
 * 我发布的零工信息
 * create by
 */
public class OrderProjProgressActivity extends BaseActivity implements OrderProjProgressView {

    @Inject
    OrderProjProgressPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.give_money)
    TextView giveMoney;
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;

    //审核中的 work
    private MyPublishOddWorkRes.WorkBean workBeanDoing;

    private RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;
    private LinearLayoutManager manager;
    private int apply_id, who;
    GetMoneyReq req;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, OrderProjProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int id, int who) {
        Intent intent = new Intent(context, OrderProjProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("who", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_proj_progress);
        setContentView(R.layout.skill_project_progress);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "工作进度");
        who = getIntent().getIntExtra("who", 0);
        adapter = new RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean>(this, R.layout.project_progress_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyPublishOddWorkRes.WorkBean workBean) {

                baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " 工作拍照");
                baseViewHolder.setText(R.id.address, workBean.getLocation());
                baseViewHolder.setText(R.id.content, Utils.strToDateLong(workBean.getSign_time()));

                if(workBean.getApply() == 2){
                    //未申请打款
                    baseViewHolder.setVisible(R.id.item_bottom, false);
                    baseViewHolder.setVisible(R.id.image_layout, true);
                    baseViewHolder.setText(R.id.remark, workBean.getRemark());
                }else{
                    //申请打款
                    baseViewHolder.setVisible(R.id.item_bottom, true);
                    baseViewHolder.setVisible(R.id.image_layout, false);
                    baseViewHolder.setText(R.id.content, Utils.millToDateString(workBean.getSign_time()));
                    if (workBean.getPass() == 3) {
                        //审核中
                        baseViewHolder.setText(R.id.content_time, "申请打款");

                    } else if (workBean.getPass() == 2) {
                        //审核未通过
                        baseViewHolder.setText(R.id.content_time, "审核未通过未打款");
                    } else {
                        //审核通过

                        baseViewHolder.setText(R.id.content_time, "审核通过");

                    }

                }

                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);

                    imageAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, String s) {
                            baseViewHolder.setImageUrl(R.id.image, s, R.drawable.zhanwei);
                        }
                    };

                    imageAdapter.setOnItemClickListener((view, i) -> {
                        PhotoActivity.startActivity(OrderProjProgressActivity.this, workBean.getImages().get(i),
                                workBean.getImages().get(i).contains("http"));
                    });

                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.recycler_view_item, false);
                }

            }
        };


        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        presenter.myPublishOddWorkProgress(getIntent().getIntExtra(INTENT_KEY, 0));

        pass.setOnClickListener(v -> {
            GetMoneyReq req = new GetMoneyReq();
            req.setWork_id(workBeanDoing.getWork_id());
            AppealActivity.startActivity(this, req, SKILL_RELEASE_LINGGONG_NO_PASS);
        });

        giveMoney.setOnClickListener(v -> {

            new IOSDialog(this).builder()
                    .setTitle("点击确认打款给对方")
                    .setMsg("打款给对方了")
                    .setPositiveButton("确认", v1 -> {
                        presenter.myPublishOddSuccess(this, workBeanDoing.getWork_id());
                    })
                    .setNegativeButton("取消", v1 -> {

                    })
                    .show();



        });
        req = new GetMoneyReq();
        req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));

    }

    @Override
    public void getData(MyPublishOddWorkRes res) {
        if (res.getWork() != null && res.getWork().size() > 0) {
            adapter.addAll(res.getWork());

            workBeanDoing = res.getWork().get(res.getWork().size() - 1);
            apply_id = workBeanDoing.getApply_id();
            if(workBeanDoing.getApply() == 2){
                //未申请打款
                bottomBtnLayout.setVisibility(View.GONE);
            }else if(workBeanDoing.getApply() == 1){
                //申请打款
                if (workBeanDoing.getPass() == 1) {
                    //已确认打款
                    bottomBtnLayout.setVisibility(View.GONE);
                }else if(workBeanDoing.getPass() == 2){
                    bottomBtnLayout.setVisibility(View.GONE);
                }
            }


        } else {
            recyclerView.showEmpty();
            bottomBtnLayout.setVisibility(View.GONE);
        }

        if(who == SKILL_RELEASE_LINGGONG_DONE){
            bottomBtnLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void passData(Object s) {
        ToastUtil.show("打款成功");
        finish();
    }

    @Override
    public void giveMoney(Object s) {
        ToastUtil.show("审核通过");
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        menu.findItem(R.id.action_contact).setTitle("申诉");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:

                req.setProject_type(3);
                AppealActivity.startActivity(this,
                        req, JIA_TYPE);

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
