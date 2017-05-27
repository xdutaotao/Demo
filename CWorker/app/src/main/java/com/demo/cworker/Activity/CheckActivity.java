package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class CheckActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String selectString;
    private static final String LIST_KEY = "LIST_KEY";

    private RecyclerArrayAdapter<PackageBean.ResultBean.AtsBean> adapter;
    private List<PackageBean.ResultBean.AtsBean> listBean;

    public static void startActivityForResult(Activity context, List<PackageBean.ResultBean.AtsBean> list , String s) {
        Intent intent = new Intent(context, CheckActivity.class);
        intent.putExtra(INTENT_KEY, s);
        intent.putExtra(LIST_KEY, (Serializable) list);
        context.startActivityForResult(intent, CollectActivity.REQUEST_CHECK_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "核查");

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))){
            selectString = getIntent().getStringExtra(INTENT_KEY);
        }

        if (getIntent().getSerializableExtra(LIST_KEY) != null){
            listBean = (List<PackageBean.ResultBean.AtsBean>) getIntent().getSerializableExtra(LIST_KEY);
        }

        adapter = new RecyclerArrayAdapter<PackageBean.ResultBean.AtsBean>(this, R.layout.check_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, PackageBean.ResultBean.AtsBean s) {
                baseViewHolder.setText(R.id.type_item_txt, s.getName());
                baseViewHolder.setVisible(R.id.select, TextUtils.equals(s.getName(), selectString));
            }
        };

        adapter.addAll(listBean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener((view, i) -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY, listBean.get(i));
            setResult(RESULT_OK, intent);
            finish();
        });
    }

}
