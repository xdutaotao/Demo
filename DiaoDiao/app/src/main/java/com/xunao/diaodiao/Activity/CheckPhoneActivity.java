package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.RegisterPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.RegisterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class CheckPhoneActivity extends BaseActivity implements RegisterView, View.OnClickListener {

    @Inject
    RegisterPresenter presenter;
    @BindView(R.id.phone_input)
    EditText phoneInput;
    @BindView(R.id.code_input)
    EditText codeInput;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.pwd_input)
    EditText pwdInput;
    @BindView(R.id.pwd_sure_input)
    EditText pwdSureInput;
    @BindView(R.id.go_login)
    LinearLayout goLogin;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.agree)
    CheckBox agree;
    @BindView(R.id.agree_content)
    TextView agreeContent;

    private Subscription subscriber;

    private int type = 0;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, CheckPhoneActivity.class);
        intent.putExtra(INTENT_KEY, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        type = getIntent().getIntExtra(INTENT_KEY, 0);
        showToolbarBack(toolBar, titleText, type == 0 ? "注册" : (type == 1 ? "忘记密码" : "修改密码"));

        registerBtn.setText(type == 0 ? "注册" : "修改");

        goLogin.setVisibility(type == 0 ? View.VISIBLE : View.GONE);

        agree.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        agreeContent.setVisibility(type == 0 ? View.VISIBLE : View.GONE);

        phoneInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        codeInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        getCode.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        login.setOnClickListener(this);

        agreeContent.setOnClickListener(v -> {
            PDFActivity.startActivity(this, "协议", "协议");
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                getCodeAction();
                break;

            case R.id.register_btn:
                setCodeAction();
                break;

            case R.id.login:
                LoginActivity.startActivity(this);
                finish();
                break;
        }
    }

    private void getCodeAction() {
        if (phoneInput.getText().length() == 0) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (phoneInput.getText().length() != 11) {
            ToastUtil.show("手机号填写错误");
            return;
        }

        //getCode.setBackgroundResource(R.drawable.btn_code_not);

        if (TextUtils.equals(getCode.getText().toString(), "获取验证码")) {
            presenter.checkPhone(this, phoneInput.getText().toString());
        } else {

            ToastUtil.show("一分钟内不能重复发送");
        }


    }

    private void setCodeAction() {
        if (phoneInput.getText().length() == 0) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (phoneInput.getText().length() != 11) {
            ToastUtil.show("手机号填写错误");
            return;
        }

        if (pwdInput.getText().length() == 0) {
            ToastUtil.show("密码不能为空");
            return;
        }

        if (pwdInput.getText().length() < 6) {
            ToastUtil.show("密码至少6位");
            return;
        }

        if (type == 0 && !agree.isChecked()) {
            ToastUtil.show("请同意协议");
            return;
        }

        if (type == 1) {
            //忘记密码
            presenter.forgetPwd(this, phoneInput.getText().toString(),
                    pwdInput.getText().toString(), codeInput.getText().toString(), type);
        } else {
            //注册
            presenter.register(this, phoneInput.getText().toString(),
                    pwdInput.getText().toString(), codeInput.getText().toString(), type);
        }

    }

    @Override
    public void getData(String result) {
        if (TextUtils.isEmpty(User.getInstance().getUserId()))
            LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void getData(Object result) {
        ToastUtil.show("获取验证码成功");
        subscriber = Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(aLong -> {
                    if (subscriber != null) {
                        if (aLong >= 60) {
                            stopTime();
                        } else {
                            getCode.setText((60 - aLong) + " s");
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
        stopTime();
    }


}
