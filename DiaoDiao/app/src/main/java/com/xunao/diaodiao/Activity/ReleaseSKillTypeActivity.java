package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Fragment.ProjectFragment;
import com.xunao.diaodiao.Present.ReleaseSKillTypePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseSKillTypeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseSKillTypeActivity extends BaseActivity implements ReleaseSKillTypeView {

    @Inject
    ReleaseSKillTypePresenter presenter;
    @BindView(R.id.my_release)
    RelativeLayout myRelease;
    @BindView(R.id.my_get)
    RelativeLayout myGet;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseSKillTypeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_type);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "零工信息");

        myRelease.setOnClickListener(v -> {
            // 1 零工  0 我发布的
            OrderCompProjActivity.startActivity(ReleaseSKillTypeActivity.this, Constants.SKILL_RELEASE_LINGGONG);
        });

        myGet.setOnClickListener(v -> {
            //        1 我接的
            OrderCompProjActivity.startActivity(ReleaseSKillTypeActivity.this, Constants.SKILL_RELEASE_LINGGONG);
        });
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
