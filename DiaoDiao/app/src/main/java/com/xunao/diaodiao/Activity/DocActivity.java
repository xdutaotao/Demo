package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private RecyclerArrayAdapter<String> adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DocActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "资料库");

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.doc_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String homeBean) {
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            DocDetailActivity.startActivity(DocActivity.this);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        adapter.addAll(list);
    }
}
