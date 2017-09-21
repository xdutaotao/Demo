package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.SkillRecieveProjDetailRes;
import com.xunao.diaodiao.Present.SkillRecieveProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SkillRecieveProjectDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class SkillRecieveProjectDetailActivity extends BaseActivity implements SkillRecieveProjectDetailView {

    @Inject
    SkillRecieveProjectDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.proj_type)
    TextView projType;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.project_detail)
    RelativeLayout projectDetail;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.project_fee)
    RelativeLayout projectFee;
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.pic_layout)
    RelativeLayout picLayout;
    @BindView(R.id.post)
    Button post;

    public static void startActivity(Context context, int projID) {
        Intent intent = new Intent(context, SkillRecieveProjectDetailActivity.class);
        intent.putExtra(INTENT_KEY, projID);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_recieve_project_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目详情");

        presenter.myAcceptProjectDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
    }

    @Override
    public void getData(SkillRecieveProjDetailRes res) {
        
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
