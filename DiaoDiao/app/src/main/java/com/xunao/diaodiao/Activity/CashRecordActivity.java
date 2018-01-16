package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Present.CashRecordPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.CashRecordView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * create by
 */
public class CashRecordActivity extends BaseActivity implements CashRecordView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    CashRecordPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<CashRecordRes.Record> adapter;
    private int page = 1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CashRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cash_record);
        setContentView(R.layout.single_recycler_view);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "交易记录");
        adapter = new RecyclerArrayAdapter<CashRecordRes.Record>(this, R.layout.cash_record_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, CashRecordRes.Record record) {
                baseViewHolder.setText(R.id.money, "￥ "+record.getCash());
                if (record.getStatus() == 1){
                    baseViewHolder.setText(R.id.status, "提现成功");
                    baseViewHolder.setTextColorRes(R.id.status, R.color.accept_btn_default);

                }else if (record.getStatus() == 2){
                    baseViewHolder.setText(R.id.status, "提现中");
                }else if (record.getStatus() == 3){
                    baseViewHolder.setText(R.id.status, "提现失败");
                    baseViewHolder.setTextColorRes(R.id.status, R.color.colorAccent);
                }

                baseViewHolder.setText(R.id.time, record.getDate());

            }
        };

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.cashRecord(this, page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.cashRecord(this, page);
    }


    @Override
    public void onFailure() {
        adapter.stopMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getData(CashRecordRes res) {
        if (page == 1)
            adapter.clear();

        if (res.getRecord() == null){
            adapter.stopMore();
        }else{
            adapter.addAll(res.getRecord());
        }


    }


}
