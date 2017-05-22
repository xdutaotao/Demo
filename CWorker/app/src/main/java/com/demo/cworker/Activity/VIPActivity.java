package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_vip_btn:
                BuyActivity.startActivity(this);
                break;
        }
    }
}
