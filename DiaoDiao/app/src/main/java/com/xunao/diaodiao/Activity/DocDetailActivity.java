package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.back_icon)
    ImageView backIcon;

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

        backIcon.setOnClickListener(v -> {
            finish();
        });
        back.setVisibility(View.GONE);

        adapter = new RecyclerArrayAdapter<DocRes>(this, R.layout.doc_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, DocRes homeBean) {
                baseViewHolder.setText(R.id.name, homeBean.getTitle());
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
