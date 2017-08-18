package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Present.ReleaseProjThirdPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseProjThirdView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseProjThirdActivity extends BaseActivity implements ReleaseProjThirdView, View.OnClickListener {

    @Inject
    ReleaseProjThirdPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pay)
    TextView pay;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseProjThirdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_proj_third);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "确认项目信息");
        pay.setOnClickListener(this);
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
            case R.id.pay:
                View view = LayoutInflater.from(this)
                        .inflate(R.layout.release_proj_dialog, null);
                view.findViewById(R.id.sure).setOnClickListener(v1 -> {
                    PayActivity.startActivity(this);
                });
                new IOSDialog(this).builder()
                        .setContentView(view)
                        .show();


                break;
        }
    }
}
