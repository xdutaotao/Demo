package com.xunao.diaodiao.Activity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.HelpDetailView;
import com.xunao.diaodiao.Present.HelpDetailPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class HelpDetailActivity extends BaseActivity implements HelpDetailView {

    @Inject
    HelpDetailPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);
        getActivityComponent().inject(this);
        presenter.attachView(this);
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
