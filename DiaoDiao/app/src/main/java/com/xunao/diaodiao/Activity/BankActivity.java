package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Present.BankPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.BankView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class BankActivity extends BaseActivity implements BankView {

    @Inject
    BankPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<BankListRes.BankCard> adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的银行卡");

        adapter = new RecyclerArrayAdapter<BankListRes.BankCard>(this, R.layout.bank_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, BankListRes.BankCard s) {
                baseViewHolder.setText(R.id.bank_name, s.getCard_name());
                baseViewHolder.setText(R.id.bank_type, s.getCard_type());
                int length = s.getCard().length();
                String cardFoot = s.getCard().substring(length-4, length);
                baseViewHolder.setText(R.id.bank_foot, cardFoot);
            }
        };

        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup viewGroup) {
                View view =  LayoutInflater.from(BankActivity.this)
                        .inflate(R.layout.bank_foot, null);
                view.setOnClickListener(v -> {
                    AddBankActivity.startActivity(BankActivity.this);
                });
                return view;
            }

            @Override
            public void onBindView(View view) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter.getBankList(this);
    }

    @Override
    public void getData(BankListRes data) {
        adapter.clear();
        adapter.addAll(data.getBankCard());
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
