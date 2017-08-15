package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.FindProjectView;
import com.xunao.diaodiao.Present.FindProjectPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class FindProjectActivity extends BaseActivity implements FindProjectView {

    @Inject
    FindProjectPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FindProjectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_project);
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
