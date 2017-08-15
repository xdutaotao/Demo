package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Present.SelectSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SelectSkillView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SelectSkillActivity extends BaseActivity implements SelectSkillView {

    @Inject
    SelectSkillPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.info_num)
    TextView infoNum;
    @BindView(R.id.register_btn)
    Button registerBtn;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectSkillActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skill);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "技术人员");
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
