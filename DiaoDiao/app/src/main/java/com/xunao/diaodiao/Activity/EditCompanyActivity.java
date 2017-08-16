package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.EditCompanyView;
import com.xunao.diaodiao.Present.EditCompanyPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class EditCompanyActivity extends BaseActivity implements EditCompanyView {

    @Inject
    EditCompanyPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditCompanyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);
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
