package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Present.ReleaseProjSecondPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseProjSecondView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseProjSecondActivity extends BaseActivity implements ReleaseProjSecondView, View.OnClickListener {

    @Inject
    ReleaseProjSecondPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.next)
    Button next;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseProjSecondActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_proj_second);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布项目信息");
        next.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                break;
        }
    }
}
