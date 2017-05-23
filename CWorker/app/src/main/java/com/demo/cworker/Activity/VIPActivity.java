package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Model.User;
import com.demo.cworker.R;
import com.demo.cworker.Widget.CustomDialog;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
        Glide.with(this)
                .load(User.getInstance().getUserInfo().getPerson().getFace())
                .placeholder(R.drawable.ic_launcher_round)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headIcon);
        if (User.getInstance().getUserInfo().getPerson().getVIP() != 0){
            level.setImageResource(R.drawable.vip);
        }
        name.setText(User.getInstance().getUserInfo().getPerson().getName());

        long days = (User.getInstance().getUserInfo().getPerson().getVipDateline() - System.currentTimeMillis()/1000)/(60*60*24);
        if (days == 0) {
            vipDays.setText("您的会员已过期");
        } else {
            vipDays.setText("尊敬的VIP用户,您的会员还有:" + days + "天");
        }

        vipOne.setOnClickListener(this);
        vipTwo.setOnClickListener(this);
        vipThree.setOnClickListener(this);
        vipFour.setOnClickListener(this);
        vipFive.setOnClickListener(this);
        vipSix.setOnClickListener(this);
        vipSeven.setOnClickListener(this);
        vipEight.setOnClickListener(this);
        vipNine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_vip_btn:
                BuyActivity.startActivity(this);
                break;

            case R.id.vip_one:
                CustomDialog.showReboundPop(this, R.drawable.vip_one, "点亮会员尊贵无限");
                break;

            case R.id.vip_two:
                CustomDialog.showReboundPop(this, R.drawable.vip_two, "千兆宽带快人一步");
                break;

            case R.id.vip_three:
                CustomDialog.showReboundPop(this, R.drawable.vip_three, "海量资料随意翻阅");
                break;

            case R.id.vip_four:
                CustomDialog.showReboundPop(this, R.drawable.vip_four, "加速通道极速下载");
                break;

            case R.id.vip_five:
                CustomDialog.showReboundPop(this, R.drawable.vip_five, "私人客服告别等待");
                break;

            case R.id.vip_six:
                CustomDialog.showReboundPop(this, R.drawable.vip_six, "每日签到更多奖励");
                break;

            case R.id.vip_seven:
                CustomDialog.showReboundPop(this, R.drawable.vip_seven, "会员生日神秘礼物");
                break;

            case R.id.vip_eight:
                CustomDialog.showReboundPop(this, R.drawable.vip_eight, "新版功能优先体验");
                break;

            case R.id.vip_nine:
                CustomDialog.showReboundPop(this, R.drawable.vip_nine, "更多功能敬请期待");
                break;
        }
    }

}
