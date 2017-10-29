package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.MyAppealDetailRes;
import com.xunao.diaodiao.Present.RecordDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.RecordDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class RecordDetailActivity extends BaseActivity implements RecordDetailView {

    @Inject
    RecordDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.project_detail)
    TextView projectDetail;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.detail_layout)
    RelativeLayout detailLayout;

    private RecyclerArrayAdapter<String> adapter;

    public static void startActivity(Context context, int id, String url) {
        Intent intent = new Intent(context, RecordDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "申诉详情");

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
            }
        };

        recyclerView.setAdapter(adapter);


        presenter.myAppealDetail(getIntent().getIntExtra(INTENT_KEY, 0));

        detailLayout.setOnClickListener(v -> {
            WebViewDetailActivity.startActivity(this, getIntent().getStringExtra("url"));
        });
    }

    @Override
    public void getData(MyAppealDetailRes res) {
        MyAppealDetailRes.AppealBean bean = res.getAppeal();
        type.setText(bean.getAppeal_operate());
        content.setText(bean.getContent());
        adapter.addAll(bean.getImages());
        result.setText(bean.getRemark());
        if (bean.getStatus() == 1) {
            status.setText("已处理");
        } else {
            status.setText("处理中");
        }

    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
