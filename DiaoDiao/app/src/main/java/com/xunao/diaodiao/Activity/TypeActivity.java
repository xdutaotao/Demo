package com.xunao.diaodiao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class TypeActivity extends BaseActivity {
    private static final String LIST_KEY = "LIST_KEY";

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<PackageBean.ResultBean.MtsBean> adapter;
    private String selectString;
    private List<PackageBean.ResultBean.MtsBean> listBean;

    public static void startActivityForResult(Activity context, List<PackageBean.ResultBean.MtsBean> list, String s) {
        Intent intent = new Intent(context, TypeActivity.class);
        intent.putExtra(INTENT_KEY, s);
        intent.putExtra(LIST_KEY, (Serializable) list);
        context.startActivityForResult(intent, CollectActivity.REQUEST_TYPE_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "类型");

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))){
            selectString = getIntent().getStringExtra(INTENT_KEY);
        }

        if (getIntent().getSerializableExtra(LIST_KEY) != null){
            listBean = (List<PackageBean.ResultBean.MtsBean>) getIntent().getSerializableExtra(LIST_KEY);
        }

        adapter = new RecyclerArrayAdapter<PackageBean.ResultBean.MtsBean>(this, R.layout.type_list_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, PackageBean.ResultBean.MtsBean s) {
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
