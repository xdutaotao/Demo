package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.cworker.Present.RegisterPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.RxUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.RegisterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscription;

public class CheckPhoneActivity extends BaseActivity implements RegisterView, View.OnClickListener {

    @Inject
    RegisterPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.phone_input)
    EditText phoneInput;
    @BindView(R.id.code_input)
    EditText codeInput;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.register_btn)
    Button registerBtn;

    private Subscription subscriber;
    private EventHandler eventHandler;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CheckPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "获取验证码");

        phoneInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        codeInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        getCode.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        CheckEmailActivity.startActivity(CheckPhoneActivity.this, phoneInput.getText().toString());
                        finish();

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        ToastUtil.show("发送成功");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_code:
                getCodeAction();
                break;

            case R.id.register_btn:
                setCodeAction();
                break;
        }
    }

    private void getCodeAction(){
        presenter.checkPhone(this, phoneInput.getText().toString());
    }

    private void setCodeAction(){
        SMSSDK.submitVerificationCode("86", phoneInput.getText().toString(), codeInput.getText().toString());
    }

    @Override
    public void getData(String result) {
        subscriber = Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(aLong -> {
                    if (subscriber != null){
                        if (aLong >= 60) {
                            stopTime();
                        } else {
                            getCode.setText((60 - aLong) + "秒后重发");
                        }
                    }

                });
        SMSSDK.getVerificationCode("86", phoneInput.getText().toString());
    }

    private void stopTime() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
            subscriber = null;
        }
        getCode.setText("获取验证码");
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        SMSSDK.unregisterEventHandler(eventHandler);
    }


}
