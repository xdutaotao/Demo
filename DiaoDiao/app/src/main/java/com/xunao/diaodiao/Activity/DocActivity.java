package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Present.DocPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.DocView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocActivity extends BaseActivity implements DocView {

    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @Inject
    DocPresenter presenter;

    private RecyclerArrayAdapter<DocRes> adapter;
    private int page = 1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DocActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "资料库");

        adapter = new RecyclerArrayAdapter<DocRes>(this, R.layout.doc_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, DocRes homeBean) {
                baseViewHolder.setText(R.id.doc_type, homeBean.getName());
                baseViewHolder.setText(R.id.num, "共"+homeBean.getCount()+"篇");
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            DocDetailActivity.startActivity(DocActivity.this, adapter.getAllData().get(i).getId());
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter.getDataBase(this);
    }

    @Override
    public void onFailure() {
        adapter.stopMore();
    }

    @Override
    public void getData(List<DocRes> list) {
        adapter.clear();
        adapter.addAll(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
