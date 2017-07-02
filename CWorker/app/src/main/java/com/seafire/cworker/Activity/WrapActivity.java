package com.seafire.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.seafire.cworker.Bean.PackageBean;
import com.seafire.cworker.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seafire.cworker.Common.Constants.INTENT_KEY;

public class WrapActivity extends BaseActivity {
    private static final String LIST_KEY = "LIST_KEY";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private RecyclerArrayAdapter<PackageBean.ResultBean.PssBean> adapter;
    private String intentData;
    private List<PackageBean.ResultBean.PssBean> listBean;

    public static void startActivityForResult(Activity context, List<PackageBean.ResultBean.PssBean> list, String s) {
        Intent intent = new Intent(context, WrapActivity.class);
        intent.putExtra(INTENT_KEY, s);
        intent.putExtra(LIST_KEY, (Serializable) list);
        context.startActivityForResult(intent, CollectActivity.REQUEST_WRAP_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, " ");

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            intentData = getIntent().getStringExtra(INTENT_KEY);
        }

        if (getIntent().getSerializableExtra(LIST_KEY) != null){
            listBean = (List<PackageBean.ResultBean.PssBean>) getIntent().getSerializableExtra(LIST_KEY);
        }

        adapter = new RecyclerArrayAdapter<PackageBean.ResultBean.PssBean>(this, R.layout.wrap_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, PackageBean.ResultBean.PssBean s) {
                baseViewHolder.setText(R.id.top_name, s.getName());
                baseViewHolder.setText(R.id.description, s.getDescription());
                baseViewHolder.setVisible(R.id.wrap_select, TextUtils.equals(s.getName(), intentData));
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY, listBean.get(i));
            setResult(RESULT_OK, intent);
            finish();
        });

        adapter.addAll(listBean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}
