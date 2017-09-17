package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ApplyProjRes;
import com.xunao.diaodiao.Present.ApplyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ApplyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ApplyActivity extends BaseActivity implements ApplyView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

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

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, ApplyActivity.class);
        intent.putExtra(INTENT_KEY, id);
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

        adapter = new RecyclerArrayAdapter<ApplyProjRes.ApplicantBean>(this, R.layout.apply_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ApplyProjRes.ApplicantBean s) {

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            ApplyDetailActivity.startActivity(ApplyActivity.this, 0);
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
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
    public void onRefresh() {
        page = 1;
        presenter.myProjectWait(getIntent().getIntExtra(INTENT_KEY, 0), 1);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.myProjectWait(getIntent().getIntExtra(INTENT_KEY, 0), 1);
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
