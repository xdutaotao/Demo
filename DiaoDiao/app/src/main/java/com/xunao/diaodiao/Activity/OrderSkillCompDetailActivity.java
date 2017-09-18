package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.xunao.diaodiao.Present.OrderSkillCompDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.OrderSkillCompDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class OrderSkillCompDetailActivity extends BaseActivity implements OrderSkillCompDetailView {

    @Inject
    OrderSkillCompDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.days)
    TextView days;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, OrderSkillCompDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_skill_comp_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        presenter.mySkillProjDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
    }

    @Override
    public void getData(String s) {

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
