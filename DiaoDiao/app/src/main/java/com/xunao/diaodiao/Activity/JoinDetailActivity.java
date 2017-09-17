package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.GetOddInfoRes;
import com.xunao.diaodiao.Bean.JoinDetailRes;
import com.xunao.diaodiao.Present.JoinDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.JoinDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class JoinDetailActivity extends BaseActivity implements JoinDetailView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    JoinDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<JoinDetailRes.EvaluateInfoBean> adapter;
    private int type;
    private int page= 1;

    public static void startActivity(Context context, int id, int type) {
        Intent intent = new Intent(context, JoinDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "公司介绍");

        adapter = new RecyclerArrayAdapter<JoinDetailRes.EvaluateInfoBean>(this, R.layout.join_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, JoinDetailRes.EvaluateInfoBean homeBean) {
                baseViewHolder.setText(R.id.content, homeBean.getContent());
                baseViewHolder.setText(R.id.head_info, homeBean.getName());
                baseViewHolder.setImageUrl(R.id.head_icon, homeBean.getHead_img());
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            ProjectDetailActivity.startActivity(JoinDetailActivity.this, 0, 0);
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);

        type = getIntent().getIntExtra("TYPE", 0);
        onRefresh();


    }

    @Override
    public void onRefresh() {
        page = 1;
        if (type == 0){
            presenter.getCompanyInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        }else if(type == 1){
            presenter.getOddInfo(getIntent().getIntExtra(INTENT_KEY, 0));
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        if (type == 0){
            presenter.getCompanyInfo(getIntent().getIntExtra(INTENT_KEY, 0), page);
        }else if(type == 1){
            presenter.getOddInfo(getIntent().getIntExtra(INTENT_KEY, 0));
        }
    }

    @Override
    public void getData(JoinDetailRes s) {
        if (page == 1){
            adapter.clear();
        }

        if (s.getEvaluate_Info().size() == 0){
            adapter.stopMore();
        }else{
            adapter.addAll(s.getEvaluate_Info());
        }
    }

    @Override
    public void getOddInfo(GetOddInfoRes res) {

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
