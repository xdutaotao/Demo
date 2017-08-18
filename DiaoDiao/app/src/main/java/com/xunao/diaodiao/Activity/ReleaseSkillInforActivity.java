package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.xunao.diaodiao.Present.ReleaseSkillInforPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseSkillInforView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseSkillInforActivity extends BaseActivity implements ReleaseSkillInforView {

    @Inject
    ReleaseSkillInforPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.next)
    Button next;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseSkillInforActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_infor);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布保养信息");
        next.setOnClickListener(v -> {
            ReleaseSkillSureInfoActivity.startActivity(ReleaseSkillInforActivity.this);
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
