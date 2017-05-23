package com.demo.cworker.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cworker.R;
import com.demo.cworker.View.SuggestView;
import com.demo.cworker.Present.SuggestPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class SuggestActivity extends BaseActivity implements SuggestView {

    @Inject
    SuggestPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                actionSubmit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actionSubmit() {

    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getData(String s) {

    }
}
