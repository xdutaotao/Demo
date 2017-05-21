package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class WrapActivity extends BaseActivity {
    public static final String[] NAMES = {
            "需包装的零件_PDC&$PDC:该种零件到库时未进行有效的包装，或者因特殊情况需要变更包装。",
            "箱类包装_GH&$GH:该种零件到库时已进行有效的包装，且可进行安全有效的码托，一般工艺为：纸箱、木箱、钙塑箱等。",
            "袋类包装_GS&$GS:该种零件到库时已进行有效的包装，一般工艺为：平口袋、气泡袋(片)、防绣袋(膜、纸)、纸袋(含纸箱夹扁)等。",
            "管类包装_GG&$GG:该种零件到库时已进行有效的包装，一般工艺为：纸管、三角管、PVC管等。",
            "裸包装_GW&$GW:该种零件到库时已进行有效的包装，到库时为裸件，一般工艺为：栓挂、直接贴。",
            "供应商_箱类包装&$GH:该种零件到库时已进行有效的包装，且可进行安全有效的码托，一般工艺为：纸箱、木箱、钙塑箱等。",
            "供应商_袋类包装&$GS:该种零件到库时已进行有效的包装，一般工艺为：平口袋、气泡袋(片)、防绣袋(膜、纸)、纸袋(含纸箱夹扁)等。",
            "供应商_管类包装&$GG:该种零件到库时已进行有效的包装，一般工艺为：纸管、三角管、PVC管等。",
            "供应商_裸包装&$GW:该种零件到库时已进行有效的包装，到库时为裸件，一般工艺为：栓挂、直接贴。"};

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private RecyclerArrayAdapter<String> adapter;
    private String intentData;

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, WrapActivity.class);
        intent.getStringExtra(INTENT_KEY);
        context.startActivityForResult(intent, CollectActivity.REQUEST_WRAP_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, " ");

        if (TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            intentData = getIntent().getStringExtra(INTENT_KEY);
        }

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.wrap_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                String[] data = s.split("&$");
                baseViewHolder.setText(R.id.top_name, data[0]);
                baseViewHolder.setText(R.id.description, data[1]);
                baseViewHolder.setVisible(R.id.wrap_select, TextUtils.equals(data[0], intentData));

                baseViewHolder.setOnClickListener(R.id.top_layout, v -> {
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_KEY, data[0]);
                    setResult(RESULT_OK);
                    finish();
                });
            }
        };

        adapter.addAll(NAMES);
        recyclerView.setAdapter(adapter);
    }
}
