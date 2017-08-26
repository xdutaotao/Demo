package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.xunao.diaodiao.Present.SelectCompanyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SelectCompanyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SelectCompanyActivity extends BaseActivity implements SelectCompanyView {

    @Inject
    SelectCompanyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.go_in_app)
    Button goInApp;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectCompanyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "暖通公司");

        goInApp.setOnClickListener(v -> {
            finish();
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
