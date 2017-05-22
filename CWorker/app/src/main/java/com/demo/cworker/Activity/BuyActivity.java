package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.cworker.Model.User;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.Present.BuyPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.BuyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyActivity extends BaseActivity implements View.OnClickListener, BuyView {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.level)
    ImageView level;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.glod)
    TextView glod;
    @BindView(R.id.year_vip)
    TextView yearVip;
    @BindView(R.id.year_layout)
    RelativeLayout yearLayout;
    @BindView(R.id.half_year_vip)
    TextView halfYearVip;
    @BindView(R.id.half_year_layout)
    RelativeLayout halfYearLayout;
    @BindView(R.id.quarter_vip)
    TextView quarterVip;
    @BindView(R.id.quarter_layout)
    RelativeLayout quarterLayout;
    @BindView(R.id.month_vip)
    TextView monthVip;
    @BindView(R.id.month_layout)
    RelativeLayout monthLayout;
    @BindView(R.id.pay_tv)
    TextView payTv;
    @BindView(R.id.pay_btn)
    Button payBtn;

    @Inject
    BuyPresenter presenter;
    int money = 0;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, BuyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "会员中心");
        getActivityComponent().inject(this);
        presenter.attachView(this);

        yearLayout.setOnClickListener(this);
        halfYearLayout.setOnClickListener(this);
        quarterLayout.setOnClickListener(this);
        monthLayout.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        glod.setText(User.getInstance().getUserInfo().getPerson().getGold()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.year_layout:
                clearUI();
                yearLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                payTv.setText("共需支付金币: 1800");
                money = 1800;
                break;

            case R.id.half_year_layout:
                clearUI();
                halfYearLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                payTv.setText("共需支付金币: 1190");
                money = 1190;
                break;

            case R.id.quarter_layout:
                clearUI();
                quarterLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                payTv.setText("共需支付金币: 540");
                money = 540;
                break;

            case R.id.month_layout:
                clearUI();
                monthLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                payTv.setText("共需支付金币: 200");
                money = 200;
                break;

            case R.id.pay_btn:
                if (money > User.getInstance().getUserInfo().getPerson().getGold()){
                    ToastUtil.show("金币不够");
                    return;
                }
                presenter.addVipDuration(this, getTime(money), money);
                break;
        }
    }

    private long getTime(int money){
        switch (money){
            case 200:
                return 30*24*60*60;

            case 540:
                return 30*24*60*60*3;

            case 1190:
                return 30*24*60*60*6;

            case 1800:
                return 30*24*60*60*12;
        }
        return 0;
    }

    private void clearUI(){
        yearLayout.setBackgroundColor(getResources().getColor(R.color.divider));
        halfYearLayout.setBackgroundColor(getResources().getColor(R.color.divider));
        quarterLayout.setBackgroundColor(getResources().getColor(R.color.divider));
        monthLayout.setBackgroundColor(getResources().getColor(R.color.divider));
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(UserInfo.PersonBean s) {
        glod.setText(s.getGold()+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
