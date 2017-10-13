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
import com.xunao.diaodiao.Bean.PayFeeReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Present.PayPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.PayView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, PayView {

    @Inject
    PayPresenter presenter;
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
    @BindView(R.id.balance)
    TextView balance;

    private ReleaseProjRes req;
    private int projType = 0;
    private PayFeeReq payFeeReq = new PayFeeReq();

    public static void startActivity(Context context, ReleaseProjRes req, int projType) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("projType", projType);
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

        req = (ReleaseProjRes) getIntent().getSerializableExtra(INTENT_KEY);
        projType = getIntent().getIntExtra("projType", 0);

        fee.setText(req.getTotal_fee());
        balance.setText("当前余额："+req.getBalance()+"元");
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
                payFeeReq.setOrder_no(req.getOrder_no());
                payFeeReq.setPay_fee(req.getTotal_fee());
                //1项目2监理3零工4维保
                payFeeReq.setProject_type(projType);
                presenter.balancePay(this, payFeeReq);

                break;
        }
    }

    @Override
    public void getData(Object s) {
        presenter.paySuccess(this, payFeeReq);
    }

    @Override
    public void paySuccess(Object s) {
        new IOSDialog(this).builder()
                .setContentView(R.layout.pay_dialog)
                .setNegativeBtnColor(R.color.light_gray)
                .setNegativeButton("再次发布", v1 -> {
                    finish();
                })
                .setPositiveBtnColor(R.color.light_blank)
                .setPositiveButton("查看订单", v1 -> {
                    RxBus.getInstance().post(Constants.DESTORY);
                    WebViewActivity.startActivity(this, req.getUrl());
                    finish();
                })
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }


        switch (buttonView.getId()) {
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
        ToastUtil.show("支付失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
