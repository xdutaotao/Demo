package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.demo.cworker.R;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class TypeActivity extends BaseActivity {
    private static final String[] typeList = {"复合材料", "金属", "塑料", "电池", "磁", "火药", "油品", "溶剂",
                                "电器", "橡胶", "玻璃", "石棉", "陶瓷", "海绵", "纸", "光盘", "虚拟"};

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<String> adapter;
    private String selectString;

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, TypeActivity.class);
        intent.putExtra(INTENT_KEY, s);
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

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.type_list_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.type_item_txt, s);
                baseViewHolder.setVisible(R.id.select, TextUtils.equals(s, selectString));
            }
        };

        adapter.addAll(typeList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener((view, i) -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_KEY, typeList[i]);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
