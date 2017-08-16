package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.EditSkillView;
import com.xunao.diaodiao.Present.EditSkillPresenter;

import javax.inject.Inject;

/**
 * create by
 */
public class EditSkillActivity extends BaseActivity implements EditSkillView {

    @Inject
    EditSkillPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditSkillActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_skill);
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
                EditCompanyActivity.startActivity(this);
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
