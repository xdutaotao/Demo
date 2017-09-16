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
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ProjectDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ProjectDetailActivity extends BaseActivity implements ProjectDetailView {

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
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.detail)
    TextView detail;
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

    private RecyclerArrayAdapter<FindLingGongRes.OddDrawingBean> adapter;
    private int type;

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
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目详情");

        post.setOnClickListener(v -> {

        });
        type = getIntent().getIntExtra("TYPE", 0);
        if (type == 0) {
            picLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else if(type == 1){
            picLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else if (type == 2){
            timeText.setText("上门时间");
            picLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            lingGongLayout.setVisibility(View.VISIBLE);
            projectDetail.setVisibility(View.VISIBLE);
        }

        adapter = new RecyclerArrayAdapter<FindLingGongRes.OddDrawingBean>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindLingGongRes.OddDrawingBean s) {
                baseViewHolder.setImageUrl(R.id.image, s.getImage());
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (type == 0) {
            presenter.getFindProjDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        } else if (type == 1){
            presenter.getFindLingGongDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        }else if (type == 2){
            presenter.getFindLingGongDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        }

        companyName.setOnClickListener(v -> {
            JoinDetailActivity.startActivity(this, getIntent().getIntExtra(INTENT_KEY, 0), type);
        });

    }

    @Override
    public void getData(FindProjDetailRes res) {
        title.setText(res.getProject_info().getTitle());
        companyName.setText(res.getProject_info().getCompany_name());
        time.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getCreate_time())));
        projType.setText(res.getProject_info().getType());
        address.setText(res.getProject_info().getDistrict());
        addressDetail.setText(res.getProject_info().getAddress());
        buildTime.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getBuild_time())));
        projDetail.setText(res.getProject_info().getDescribe());
        price.setText("￥ " + res.getProject_info().getProject_fee());
        detail.setText(res.getProject_info().getDescribe());

    }

    @Override
    public void getLingGongData(FindLingGongRes res) {
        title.setText(res.getOdd_info().getTitle());
        companyName.setText(res.getOdd_info().getPublish_name());
        time.setText(Utils.strToDateLong(Long.valueOf(res.getOdd_info().getCreate_time())));
        projType.setText(res.getOdd_info().getType());
        address.setText(res.getOdd_info().getDistrict());
        addressDetail.setText(res.getOdd_info().getAddress());
        buildTime.setText(Utils.strToDateLong(Long.valueOf(res.getOdd_info().getBuild_time())));
        projDetail.setText(res.getOdd_info().getDescribe());
        price.setText("￥ " + res.getOdd_info().getService_fee());
        detail.setText(res.getOdd_info().getDescribe());


        adapter.addAll(res.getOdd_drawing());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:

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
