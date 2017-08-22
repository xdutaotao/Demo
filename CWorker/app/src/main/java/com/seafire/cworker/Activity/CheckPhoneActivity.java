package com.seafire.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seafire.cworker.Present.RegisterPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.RxUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.RegisterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscription;

import static com.seafire.cworker.Common.Constants.INTENT_KEY;

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
    private boolean isChangePwd;

    Drawable drawable;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CheckPhoneActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, boolean intent_phone) {
        Intent intent = new Intent(context, CheckPhoneActivity.class);
        intent.putExtra(INTENT_KEY, intent_phone);
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
                        if (isChangePwd){
                            CheckEmailActivity.startActivity(CheckPhoneActivity.this, phoneInput.getText().toString(), isChangePwd);
                        }else{
                            CheckEmailActivity.startActivity(CheckPhoneActivity.this, phoneInput.getText().toString());
                        }

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

        if (getIntent().getBooleanExtra(INTENT_KEY, false)){
            isChangePwd = true;
        }

        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    phoneInput.setCompoundDrawables(null, null, drawable, null);
                }else{
                    phoneInput.setCompoundDrawables(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        if (phoneInput.getText().length() == 0) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (phoneInput.getText().length() != 11) {
            ToastUtil.show("手机号填写错误");
            return;
        }
        if(isChangePwd){
            presenter.changePwd(this, phoneInput.getText().toString());
        }else{
            presenter.checkPhone(this, phoneInput.getText().toString());
        }

    }

    private void setCodeAction(){
        if (phoneInput.getText().length() == 0) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (phoneInput.getText().length() != 11) {
            ToastUtil.show("手机号填写错误");
            return;
        }
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
