package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Present.ApplyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ApplyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ApplyActivity extends BaseActivity implements ApplyView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ApplyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.people_num)
    TextView peopleNum;

    private RecyclerArrayAdapter<ApplyProjRes.ApplicantBean> adapter;
    private int page = 1;

    private int projectType = 0;

    public static void startActivity(Context context, int id, int projectType) {
        Intent intent = new Intent(context, ApplyActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("projectType", projectType);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "申请人员");

        projectType = getIntent().getIntExtra("projectType", 0);

        adapter = new RecyclerArrayAdapter<ApplyProjRes.ApplicantBean>(this, R.layout.apply_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ApplyProjRes.ApplicantBean s) {
                baseViewHolder.setText(R.id.address, s.getName());
                baseViewHolder.setText(R.id.percent, s.getPoint());
                baseViewHolder.setText(R.id.days, s.getExperience()+"年工作经验");

                RatingBar bar = ((RatingBar)baseViewHolder.getConvertView().findViewById(R.id.rating_star));
                try {
                    bar.setRating(Float.valueOf(s.getPoint()));
                }catch (Exception e){
                    e.printStackTrace();
                }

                bar.setIsIndicator(true);
                baseViewHolder.setVisible(R.id.request, false);

//                baseViewHolder.setOnClickListener(R.id.request, v -> {
//                    ApplyPassReq req = new ApplyPassReq();
//                    req.setTechnician_id(s.getTechnician_id());
//                    req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
//                    req.setProject_type(projectType);
//                    presenter.getApplyPass(req);
//                });
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            ApplyPassReq req = new ApplyPassReq();
            req.setTechnician_id(adapter.getAllData().get(i).getTechnician_id());
            req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
            req.setProject_type(projectType);
            ApplyDetailActivity.startActivity(ApplyActivity.this, req);
        });

        recyclerView.setAdapterDefaultConfig(adapter, this);
        onRefresh();
    }

    @Override
    public void getData(ApplyProjRes res) {
        if (page == 1)
            adapter.clear();

        adapter.addAll(res.getApplicants());
        peopleNum.setText("共 "+res.getApplyCount()+" 人申请");

    }

    @Override
    public void getPass(Object res) {
        ToastUtil.show("申请成功");
        finish();
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.myProjectWait(getIntent().getIntExtra(INTENT_KEY, 0), projectType);
    }

//    @Override
//    public void onLoadMore() {
//        page++;
//        presenter.myProjectWait(getIntent().getIntExtra(INTENT_KEY, 0), projectType);
//    }


    @Override
    public void onFailure() {
        adapter.stopMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
