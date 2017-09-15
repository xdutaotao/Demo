package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Present.JoinDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.JoinDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class JoinDetailActivity extends BaseActivity implements JoinDetailView {

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
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<String> adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinDetailActivity.class);
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

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.join_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String homeBean) {
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            ProjectDetailActivity.startActivity(JoinDetailActivity.this, 0, 0);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        adapter.addAll(list);

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
