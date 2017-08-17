package com.xunao.diaodiao.Activity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.AddBankView;
import com.xunao.diaodiao.Present.AddBankPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class AddBankActivity extends BaseActivity implements AddBankView {

    @Inject
    AddBankPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
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
