package com.seafire.cworker.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seafire.cworker.Common.Constants;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Present.LoginPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Utils.Utils;
import com.seafire.cworker.View.LoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.chatting.utils.DialogCreator;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.database.UserEntry;

/**
 * create by
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    @Inject
    LoginPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
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
    @BindView(R.id.login_cancle)
    ImageView loginCancle;
    @BindView(R.id.pwd_cancle)
    ImageView pwdCancle;


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
        showToolbarBack(toolBar, titleText, "登录");

        name.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        login.setOnClickListener(this);
        registerTxt.setOnClickListener(this);
        forgetTxt.setOnClickListener(this);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    loginCancle.setVisibility(View.VISIBLE);
                } else {
                    loginCancle.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    pwdCancle.setVisibility(View.VISIBLE);
                } else {
                    pwdCancle.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginCancle.setOnClickListener(this);
        pwdCancle.setOnClickListener(this);
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

            case R.id.login_cancle:
                name.setText("");
                break;

            case R.id.pwd_cancle:
                pwd.setText("");
                break;
        }
    }

    /**
     * 登录操作
     */
    private void loginAction() {
        int nameLength = name.getText().toString().trim().length();
        if (nameLength == 0) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (nameLength != 11) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }

        int pwdLength = pwd.getText().toString().trim().length();
        if (pwdLength == 0) {
            ToastUtil.show("请输入密码");
            return;
        }

        if (pwdLength < 6) {
            ToastUtil.show("密码长度必须不少于6位");
            return;
        }

        presenter.login(this, name.getText().toString().trim(), pwd.getText().toString().trim(),
                Utils.getIMIEStatus(this) == null ? "" : Utils.getIMIEStatus(this));
    }

    @Override
    public void getData(String data) {
        loginJMessage();
        Constants.post = false;
    }

    private void loginJMessage() {
        final Dialog dialog = DialogCreator.createLoadingDialog(this, getString(R.string.login_hint));
        dialog.show();
        JMessageClient.login(name.getText().toString(), name.getText().toString() + "1", new BasicCallback() {
            @Override
            public void gotResult(final int status, final String desc) {
                if (status == 0) {
                    finish();
                    dialog.dismiss();

                    String username = JMessageClient.getMyInfo().getUserName();
                    String appKey = JMessageClient.getMyInfo().getAppKey();
                    UserEntry user = UserEntry.getUser(username, appKey);
                    if (null == user) {
                        user = new UserEntry(username, appKey);
                        user.save();
                    }
                    JMessageClient.getMyInfo().setNickname(User.getInstance().getUserInfo().getPerson().getName());
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, JMessageClient.getMyInfo(), new BasicCallback() {
                        @Override
                        public void gotResult(final int status, final String desc) {
                            dialog.dismiss();
                            if (status == 0) {
                                ToastUtil.show("登录成功");
                                finish();
                            } else {
                                ToastUtil.show("登录成功");
                                finish();
                                //HandleResponseCode.onHandle(LoginActivity.this, status, false);
                            }
                        }
                    });

                } else {
                    dialog.dismiss();
//                    ToastUtil.show("登录成功");
//                    finish();
                    HandleResponseCode.onHandle(LoginActivity.this, status, false);
                }
            }
        });
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
