package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.GetMoneyRes;
import com.xunao.diaodiao.Present.MoneyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MoneyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MoneyActivity extends BaseActivity implements MoneyView {

    @Inject
    MoneyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.get_money)
    TextView getMoney;

    private RecyclerArrayAdapter<GetMoneyRes.MoneyDetail> adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MoneyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的余额");

        adapter = new RecyclerArrayAdapter<GetMoneyRes.MoneyDetail>(this, R.layout.money_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, GetMoneyRes.MoneyDetail s) {
                baseViewHolder.setText(R.id.type, s.getType());
                baseViewHolder.setText(R.id.change_money, s.getChange());
                if (TextUtils.equals(s.getChange().subSequence(0, 1), "+")){
                    baseViewHolder.setTextColorRes(R.id.change_money, R.color.bank_add);
                }else{
                    baseViewHolder.setTextColorRes(R.id.change_money, R.color.colorAccent);
                }
                baseViewHolder.setText(R.id.time, s.getTime());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        getMoney.setOnClickListener(v -> {
            GetMoneyActivity.startActivity(MoneyActivity.this, money.getText().toString());
        });
        presenter.getMoney(this);
    }

    @Override
    public void getData(GetMoneyRes res) {
        money.setText(res.getBalance());
        adapter.clear();
        adapter.addAll(res.getChanges());
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
