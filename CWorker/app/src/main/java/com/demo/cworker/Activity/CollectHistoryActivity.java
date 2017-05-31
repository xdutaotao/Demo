package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.demo.cworker.Bean.CollectBean;
import com.demo.cworker.Bean.SearchBean;
import com.demo.cworker.Common.Constants;
import com.demo.cworker.R;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.ShareUtils;
import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.COLLECT_LIST;
import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class CollectHistoryActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<CollectBean> adapter;
    private List<CollectBean> list = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_history);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "上传记录");

        if (ShareUtils.getValue(COLLECT_LIST, null) != null){
            list.addAll(JsonUtils.getInstance()
                    .JsonToCollectList(ShareUtils.getValue(COLLECT_LIST, null)));
        }

        adapter = new RecyclerArrayAdapter<CollectBean>(this, R.layout.collect_history_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, CollectBean bean) {
                baseViewHolder.setText(R.id.item_txt, bean.getPartCode());
                baseViewHolder.setText(R.id.item_right_txt, bean.getTime());
                baseViewHolder.setTextColor(R.id.item_txt, bean.getIsSuccess() ? R.color.red : R.color.black);
                baseViewHolder.setTextColor(R.id.item_right_txt, bean.getIsSuccess() ? R.color.red : R.color.black);
            }
        };

        if (ShareUtils.getValue(Constants.COLLECT_LIST, null) != null){
            list.addAll(JsonUtils.getInstance().JsonToCollectList(ShareUtils.getValue(Constants.COLLECT_LIST, null)));
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.addAll(list);
    }
}
