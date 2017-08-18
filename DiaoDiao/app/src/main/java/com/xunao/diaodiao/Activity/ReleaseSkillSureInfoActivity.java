package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.xunao.diaodiao.Present.ReleaseSkillSureInfoPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseSkillSureInfoView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseSkillSureInfoActivity extends BaseActivity implements ReleaseSkillSureInfoView {

    @Inject
    ReleaseSkillSureInfoPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseSkillSureInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_sure_info);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布维修信息");
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
