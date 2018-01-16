package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.DocRes;
import com.xunao.diaodiao.Present.DocDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.DocDetailView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class DocDetailActivity extends BaseActivity implements DocDetailView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    DocDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;


    private RecyclerArrayAdapter<DocRes> adapter;
    private int page = 1;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, DocDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_deatil);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "");

        adapter = new RecyclerArrayAdapter<DocRes>(this, R.layout.doc_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, DocRes homeBean) {
                baseViewHolder.setText(R.id.doc_type, homeBean.getTitle());
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            PDFActivity.startActivity(this, adapter.getAllData().get(i).getTitle(),
                    adapter.getAllData().get(i).getFile());
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
    }

    @Override
    public void getData(List<DocRes> list) {
        if (page == 1)
            adapter.clear();


        if (list.size() != 10) {
            adapter.stopMore();
        }

        adapter.addAll(list);


    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.database(this, getIntent().getIntExtra(INTENT_KEY, 0), page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.database(this, getIntent().getIntExtra(INTENT_KEY, 0), page);
    }


    @Override
    public void onFailure() {
        if (adapter.getAllData().size() > 0) {
            adapter.stopMore();
        } else {
            recyclerView.showEmpty();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
