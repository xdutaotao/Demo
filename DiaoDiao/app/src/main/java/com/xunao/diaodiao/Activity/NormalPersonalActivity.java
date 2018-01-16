package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.NormalPersonalView;
import com.xunao.diaodiao.Present.NormalPersonalPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class NormalPersonalActivity extends BaseActivity implements NormalPersonalView {

    @Inject
    NormalPersonalPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NormalPersonalActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_personal);
        getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_company_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                EditPersonalActivity.startActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
