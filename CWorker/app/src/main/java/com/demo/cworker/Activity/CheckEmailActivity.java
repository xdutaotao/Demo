package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.cworker.Present.CheckEmailActivityPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.CheckEmailActivityView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class CheckEmailActivity extends BaseActivity implements CheckEmailActivityView {
    private static final String INTENT_KEY = "intent_key";
    @Inject
    CheckEmailActivityPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name_input)
    EditText nameInput;
    @BindView(R.id.pwd_input)
    EditText pwdInput;
    @BindView(R.id.sure_pwd)
    EditText surePwd;
    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.register_btn)
    Button registerBtn;

    private String phone;

    public static void startActivity(Context context, String phone) {
        Intent intent = new Intent(context, CheckEmailActivity.class);
        intent.putExtra(INTENT_KEY, phone);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "注册");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (emailInput.getText().length() > 0) {
                    if (!emailInput.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                        emailInput.setError("邮箱格式错误");
                    }
                } else {
                    emailInput.setHint("请输入邮箱");
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAction();
            }
        });

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))){
            phone = getIntent().getStringExtra(INTENT_KEY);
        }
    }

    private void checkEmailAction(){
        if (nameInput.getText().length() == 0) {
            ToastUtil.show("名字不能为空");
            return;
        }

        if (pwdInput.getText().length() == 0) {
            ToastUtil.show("密码不能为空");
            return;
        }

        if (!TextUtils.equals(pwdInput.getText().toString(), surePwd.getText().toString())) {
            ToastUtil.show("两次密码不一致");
            return;
        }

        presenter.checkEmail(this, emailInput.getText().toString(), pwdInput.getText().toString(), phone, nameInput.getText().toString());
    }

    @Override
    public void getData(String data) {
        ToastUtil.show("注册成功");
        finish();
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