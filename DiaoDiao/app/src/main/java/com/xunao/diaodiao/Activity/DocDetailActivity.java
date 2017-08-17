package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Present.DocDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.DocDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class DocDetailActivity extends BaseActivity implements DocDetailView {

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
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<String> adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DocDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_deatil);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.doc_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String homeBean) {
            }
        };

        adapter.setOnItemClickListener((view, i) -> {

        });

        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        adapter.addAll(list);
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
