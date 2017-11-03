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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.FindLingGongRes;
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ProjectDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ProjectDetailActivity extends BaseActivity {

    @Inject
    ProjectDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.proj_type)
    TextView projType;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.build_time)
    TextView buildTime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pic_layout)
    RelativeLayout picLayout;
    @BindView(R.id.time_text)
    TextView timeText;
    @BindView(R.id.project_detail)
    RelativeLayout projectDetail;
    @BindView(R.id.project_fee)
    RelativeLayout projectFee;
    @BindView(R.id.getFell)
    TextView getFell;
    @BindView(R.id.pei_fee)
    TextView peiFee;
    @BindView(R.id.ling_gong_layout)
    LinearLayout lingGongLayout;
    @BindView(R.id.apply)
    TextView apply;
    @BindView(R.id.detail_recycler_view)
    RecyclerView detailRecyclerView;
    @BindView(R.id.proj_detail)
    TextView projDetail;

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<FindProjDetailRes.ProjectExpensesBean> adapterDetail;
    private int type;
    private boolean isCollect = false;

    public static void startActivity(Context context, int id, int type) {
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        //presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目详情");

        type = getIntent().getIntExtra("TYPE", 0);
        if (type == 0) {

        } else if (type == 1) {
            picLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            timeText.setText("上门时间");
            picLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            lingGongLayout.setVisibility(View.VISIBLE);
            projectDetail.setVisibility(View.VISIBLE);
        }

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
            }
        };

        adapterDetail = new RecyclerArrayAdapter<FindProjDetailRes.ProjectExpensesBean>(this, R.layout.project_detail_text_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjDetailRes.ProjectExpensesBean s) {
                baseViewHolder.setText(R.id.text, s.getAmount());
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        detailRecyclerView.setLayoutManager(linearLayoutManager);
        detailRecyclerView.setAdapter(adapterDetail);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (type == 0) {
            presenter.getFindProjDetail(this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
        } else if (type == 1) {
            presenter.getFindLingGongDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        } else if (type == 2) {
            presenter.getFindLingGongDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        }

        companyName.setOnClickListener(v -> {
            JoinDetailActivity.startActivity(this, getIntent().getIntExtra(INTENT_KEY, 0), 0);
        });

        post.setOnClickListener(v -> {
            //申请
            presenter.postProject(ProjectDetailActivity.this,
                    getIntent().getIntExtra(INTENT_KEY, 0), type);

        });

    }

//    @Override
//    public void getData(FindProjDetailRes res) {
//        title.setText(res.getProject_info().getTitle());
//        companyName.setText(res.getProject_info().getPublish_name());
//        time.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getCreate_time())));
//        projType.setText(res.getProject_info().getType());
//        address.setText(res.getProject_info().getDistrict());
//        addressDetail.setText(res.getProject_info().getAddress());
//        buildTime.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getBuild_time())));
//        projDetail.setText(res.getProject_info().getDescribe());
//        price.setText("￥ " + res.getProject_info().getProject_fee());
//        //detail.setText(res.getProject_info().getDescribe());
//        adapter.addAll(res.getProject_drawing());
//
//        //项目
//        picLayout.setVisibility(View.VISIBLE);
//        if (res.getIs_apply() == 1) {
//            //申请成功
//            apply.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            post.setVisibility(View.GONE);
//        } else {
//            apply.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        }
//
//        adapterDetail.addAll(res.getProject_expenses());
//
//    }
//
//    @Override
//    public void getLingGongData(FindLingGongRes res) {
//        title.setText(res.getOdd_info().getTitle());
//        companyName.setText(res.getOdd_info().getPublish_name());
//        time.setText(Utils.strToDateLong(Long.valueOf(res.getOdd_info().getCreate_time())));
//        projType.setText(res.getOdd_info().getType());
//        address.setText(res.getOdd_info().getDistrict());
//        addressDetail.setText(res.getOdd_info().getAddress());
//        buildTime.setText(Utils.strToDateLong(Long.valueOf(res.getOdd_info().getBuild_time())));
//        projDetail.setText(res.getOdd_info().getDescribe());
//        price.setText("￥ " + res.getOdd_info().getService_fee());
//        //detail.setText(res.getOdd_info().getDescribe());
//        //adapterDetail.addAll(res.getProject_expenses());
//
//        adapter.addAll(res.getOdd_drawing());
//    }
//
//    @Override
//    public void postProject(String res) {
//        ToastUtil.show("申请成功");
//        finish();
//    }
//
//    @Override
//    public void collectWork(String s) {
//        ToastUtil.show("收藏成功");
//        isCollect = true;
//        invalidateOptionsMenu();
//    }

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
                    presenter.collectWork(this, getIntent().getIntExtra(INTENT_KEY, 0), types);
                } else {
                    presenter.collectWork(this, getIntent().getIntExtra(INTENT_KEY, 0), types);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onFailure() {
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
