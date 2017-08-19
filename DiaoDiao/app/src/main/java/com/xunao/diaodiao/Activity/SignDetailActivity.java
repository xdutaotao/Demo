package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Adapter;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Present.SignDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SignDetailView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SignDetailActivity extends BaseActivity implements SignDetailView {

    @Inject
    SignDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<String> itemAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "签到详情");

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.sign_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                RecyclerView recyclerView = (RecyclerView) baseViewHolder.getConvertView().findViewById(R.id.recycler_view_item);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
                recyclerView.setAdapter(itemAdapter);
            }
        };

        itemAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_photo_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        };



        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        adapter.addAll(list);

        List<String> itemList = new ArrayList<>();
        itemList.add("1");
        itemList.add("1");
        itemList.add("1");
        itemList.add("1");
        itemAdapter.addAll(list);
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
