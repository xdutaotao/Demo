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
import com.demo.cworker.Utils.Utils;
import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.COLLECT_LIST;
import static com.demo.cworker.Common.Constants.INTENT_KEY;
import static com.demo.cworker.Common.Constants.POST_COLLECT_TIME;

public class CollectHistoryActivity extends BaseActivity {
    public static final int REQUEST_CODE = 999;

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



        adapter = new RecyclerArrayAdapter<CollectBean>(this, R.layout.collect_history_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, CollectBean bean) {
                baseViewHolder.setText(R.id.item_txt, bean.getPartCode());
                baseViewHolder.setText(R.id.item_right_txt, bean.getTime());
                baseViewHolder.setTextColor(R.id.item_txt, getResources().getColor(!bean.getIsSuccess() ? R.color.red : android.R.color.black));
                baseViewHolder.setTextColor(R.id.item_right_txt, getResources().getColor(!bean.getIsSuccess() ? R.color.red : android.R.color.black));
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if(!list.get(i).getIsSuccess())
                CollectActivity.startActivityForResult(this, list.get(i));
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        setData();
    }

    private void setData(){
        if (ShareUtils.getValue(COLLECT_LIST, null) != null){
            list.clear();
            list.addAll(JsonUtils.getInstance()
                    .JsonToCollectList(ShareUtils.getValue(COLLECT_LIST, null)));

            if(Utils.isZeroTime()){
                List<CollectBean> collectBeanList = new ArrayList<>();
                for(CollectBean bean : list){
                    if (!bean.getIsSuccess()){
                        collectBeanList.add(bean);
                    }
                }
                adapter.clear();
                adapter.addAll(collectBeanList);
                ShareUtils.putValue(COLLECT_LIST, JsonUtils.getInstance().CollectListToJson(collectBeanList));
                ShareUtils.putValue(POST_COLLECT_TIME, collectBeanList.size());
            }else{
                adapter.clear();
                adapter.addAll(list);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE){
                setData();
            }
        }
    }
}
