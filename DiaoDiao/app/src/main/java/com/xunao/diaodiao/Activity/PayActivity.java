package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Present.PayPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.PayView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class PayActivity extends BaseActivity implements PayView, View.OnClickListener {

    @Inject
    PayPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.pay)
    Button pay;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PayActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "支付");
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
                new IOSDialog(this).builder()
                        .setContentView(R.layout.pay_dialog)
                        .setNegativeBtnColor(R.color.light_gray)
                        .setNegativeButton("再次发布", v1 -> {

                        })
                        .setPositiveBtnColor(R.color.light_blank)
                        .setPositiveButton("查看订单", v1 -> {

                        })
                        .show();
                break;
        }
    }
}
