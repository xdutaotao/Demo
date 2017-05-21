package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Model.User;
import com.demo.cworker.Model.UserInfo;
import com.demo.cworker.R;
import com.demo.cworker.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8888;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.head_layout)
    RelativeLayout headLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.level)
    TextView level;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.project)
    TextView project;
    @BindView(R.id.photo)
    TextView photo;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.address)
    TextView address;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonalActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "个人资料");

        UserInfo userInfo= User.getInstance().getUserInfo();
        Glide.with(this).load(userInfo.getPerson().getFace()).into(headIcon);
        name.setText(userInfo.getPerson().getName());
        sex.setText(userInfo.getPerson().getSex());
        level.setText("LV"+userInfo.getPerson().getGold());
        createTime.setText(Utils.formatDate(userInfo.getPerson().getDateline()+""));
        project.setText(userInfo.getPerson().getProject());
        photo.setText(userInfo.getPerson().getMobile());
        email.setText(userInfo.getPerson().getEmail());
        address.setText(userInfo.getPerson().getAddress());
        address.setOnClickListener(v -> {

        });
    }
}
