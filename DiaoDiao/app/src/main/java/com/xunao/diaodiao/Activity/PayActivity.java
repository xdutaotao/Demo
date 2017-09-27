package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Present.ReleaseProjThirdPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ReleaseProjThirdView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, ReleaseProjThirdView, CompoundButton.OnCheckedChangeListener {

    @Inject
    ReleaseProjThirdPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.current)
    CheckBox current;
    @BindView(R.id.zhifubao)
    CheckBox zhifubao;
    @BindView(R.id.wechat)
    CheckBox wechat;

    private ReleaseProjReq req;

    public static void startActivity(Context context, ReleaseProjReq req) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(INTENT_KEY, req);
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
        req = (ReleaseProjReq) getIntent().getSerializableExtra(INTENT_KEY);
        fee.setText("￥" + req.getTotal_price());
        pay.setOnClickListener(this);
        current.setOnCheckedChangeListener(this);
        zhifubao.setOnCheckedChangeListener(this);
        wechat.setOnCheckedChangeListener(this);
        current.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                presenter.publishProject(this, req);
                break;
        }
    }

    @Override
    public void getData(String s) {
        new IOSDialog(this).builder()
                .setContentView(R.layout.pay_dialog)
                .setNegativeBtnColor(R.color.light_gray)
                .setNegativeButton("再次发布", v1 -> {
                    finish();
                })
                .setPositiveBtnColor(R.color.light_blank)
                .setPositiveButton("查看订单", v1 -> {

                })
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked){
            return;
        }


        switch (buttonView.getId()){
            case R.id.current:
                current.setChecked(isChecked);
                zhifubao.setChecked(!isChecked);
                wechat.setChecked(!isChecked);
                break;

            case R.id.zhifubao:
                current.setChecked(!isChecked);
                zhifubao.setChecked(isChecked);
                wechat.setChecked(!isChecked);
                break;

            case R.id.wechat:
                current.setChecked(!isChecked);
                zhifubao.setChecked(!isChecked);
                wechat.setChecked(isChecked);
                break;
        }
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
