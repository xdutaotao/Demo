package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Model.User;
import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VIPActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.vip_one)
    ImageView vipOne;
    @BindView(R.id.vip_two)
    ImageView vipTwo;
    @BindView(R.id.vip_three)
    ImageView vipThree;
    @BindView(R.id.vip_four)
    ImageView vipFour;
    @BindView(R.id.vip_five)
    ImageView vipFive;
    @BindView(R.id.vip_six)
    ImageView vipSix;
    @BindView(R.id.vip_seven)
    ImageView vipSeven;
    @BindView(R.id.vip_eight)
    ImageView vipEight;
    @BindView(R.id.vip_nine)
    ImageView vipNine;
    @BindView(R.id.get_vip_btn)
    Button getVipBtn;
    @BindView(R.id.vip_days)
    TextView vipDays;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, VIPActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "会员中心");
        getVipBtn.setOnClickListener(this);
        Glide.with(this).load(User.getInstance().getUserInfo().getPerson().getFace()).into(headIcon);
        int days = User.getInstance().getUserInfo().getPerson().getVipDateline();
        if (days == 0) {
            vipDays.setText("您的会员已过期");
        } else {
            vipDays.setText("尊敬的VIP用户,您的会员还有:"+days+"天");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_vip_btn:
                BuyActivity.startActivity(this);
                break;
        }
    }
}
