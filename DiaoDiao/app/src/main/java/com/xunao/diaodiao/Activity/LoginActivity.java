package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunao.diaodiao.Present.LoginPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.LoginView;
import com.xunao.diaodiao.wxapi.WXEntryActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    @Inject
    LoginPresenter presenter;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.forget_pwd)
    TextView registerTxt;
    @BindView(R.id.code_login)
    TextView forgetTxt;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.wx_login)
    TextView wxLogin;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        name.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        login.setOnClickListener(this);
        registerTxt.setOnClickListener(this);
        forgetTxt.setOnClickListener(this);
        wxLogin.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loginAction();
                break;

            case R.id.forget_pwd:
                actionRegister();
                break;

            case R.id.code_login:
                CheckPhoneActivity.startActivity(this, true);
                break;

            case R.id.wx_login:
                WXEntryActivity.startActivity(this);
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 登录操作
     */
    private void loginAction() {
        SelectMemoryActivity.startActivity(this);

//        int nameLength = name.getText().toString().trim().length();
//        if (nameLength == 0) {
//            ToastUtil.show("请输入手机号");
//            return;
//        }
//        if (nameLength != 11) {
//            ToastUtil.show("请输入正确的手机号");
//            return;
//        }
//
//        int pwdLength = pwd.getText().toString().trim().length();
//        if (pwdLength == 0) {
//            ToastUtil.show("请输入密码");
//            return;
//        }
//
//        if (pwdLength < 6) {
//            ToastUtil.show("密码长度必须不少于6位");
//            return;
//        }
//
//        presenter.login(this, name.getText().toString().trim(), pwd.getText().toString().trim(),
//                Utils.getIMIEStatus(this) == null ?  "" : Utils.getIMIEStatus(this));
    }

    @Override
    public void getData(String data) {
        loginJMessage();
    }

    private void loginJMessage() {
        ToastUtil.show("登录成功");
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                actionRegister();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actionRegister() {
        CheckPhoneActivity.startActivity(this);
        //CheckEmailActivity.startActivity(this, "18513212904");
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