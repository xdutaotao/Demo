package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Present.ReleaseCompanyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseCompanyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseCompanyActivity extends BaseActivity implements ReleaseCompanyView, View.OnClickListener {

    @Inject
    ReleaseCompanyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.release_proj_info)
    LinearLayout releaseProjInfo;
    @BindView(R.id.release_skill_info)
    LinearLayout releaseSkillInfo;
    @BindView(R.id.release_help_info)
    LinearLayout releaseHelpInfo;
    @BindView(R.id.release_find_people)
    LinearLayout releaseFindPeople;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseCompanyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_company);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布");

        releaseFindPeople.setOnClickListener(this);
        releaseHelpInfo.setOnClickListener(this);
        releaseProjInfo.setOnClickListener(this);
        releaseSkillInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.release_find_people:
                //
                ReleaseProjActivity.startActivity(this);
                break;

            case R.id.release_help_info:
                //
                ReleaseHelpActivity.startActivity(this);
                break;

            case R.id.release_proj_info:
                ReleaseProjActivity.startActivity(this);
                break;

            case R.id.release_skill_info:
                ReleaseHelpActivity.startActivity(this);
                break;
        }
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
