package com.demo.step.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.step.Present.RegisterPresenter;
import com.demo.step.R;
import com.demo.step.Utils.RxUtils;
import com.demo.step.Utils.ToastUtil;
import com.demo.step.View.RegisterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;

import static com.demo.step.Common.Constants.INTENT_KEY;

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
    private boolean isChangePwd;

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


        if (getIntent().getBooleanExtra(INTENT_KEY, false)){
            isChangePwd = true;
        }
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
    }


}
