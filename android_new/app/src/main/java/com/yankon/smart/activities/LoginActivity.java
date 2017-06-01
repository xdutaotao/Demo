package com.yankon.smart.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.InputDialogFragment;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Settings;
import com.yankon.smart.utils.Utils;

/**
 * Created by guzhenfu on 2015/8/19.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, InputDialogFragment.InputDialogInterface {
    private EditText emailEdit, passwordEdit;
    protected ProgressDialog progressDialog;
    private int mDownX, mDownY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_log_in);

        initView();
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        View backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.log_in));
        TextView forgetPasswordButton = (TextView) findViewById(R.id.forgot_password);
        forgetPasswordButton.setOnClickListener(this);
        Button registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setOnClickListener(this);
        Button logInButton = (Button) findViewById(R.id.button_log_in);
        logInButton.setOnClickListener(this);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                LogUtils.i("MOVE", mDownX + "----" + event.getX() + "MOVE---------" + mDownY + "----" + event.getY());
                if (event.getX() - mDownX > Global.X_DISTANCE && Math.abs(event.getY() - mDownY) < Global.Y_DISTANCE)
                    finish();
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
            case R.id.back_layout:
                finish();
                break;

            case R.id.forgot_password:
                showResetPasswordDialog();
                break;

            case R.id.button_register:
                if (Utils.isNetworkConnected()) {
                    if (isInputValid()) {
                        String email = emailEdit.getText().toString();
                        String username = email.replace("@", "_");
                        KiiUser.builderWithEmail(email).withName(username).build()
                                .register(mUserCallBack, passwordEdit.getText().toString());
                        showProgress();
                    }
                }else{
                    Toast.makeText(this, getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_log_in:
                if (Utils.isNetworkConnected()) {
                    if (isInputValid()) {
                        KiiUser.logIn(mUserCallBack, emailEdit.getText().toString(),
                                passwordEdit.getText().toString());
                        showProgress();
                    }
                }else{
                    Toast.makeText(this, getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void showResetPasswordDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getString(R.string.reset_password_title),
                emailEdit.getText().toString(),
                getString(R.string.reset_password_hint),
                getString(R.string.reset_password_text),
                InputDialogFragment.TYPE_RESET_PASSWORD);
        newFragment.setInputDialogInterface(this);
        newFragment.show(getFragmentManager(), "dialog");
    }

    private boolean isInputValid() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.requestFocus();
            emailEdit.setError(getString(R.string.error_email_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.requestFocus();
            emailEdit.setError(getString(R.string.error_invalid_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            passwordEdit.requestFocus();
            passwordEdit.setError(getString(R.string.error_password_empty));
            return false;
        } else if (password.length() < 4) {
            passwordEdit.requestFocus();
            passwordEdit.setError(getString(R.string.error_password_too_short));
            return false;
        }
        return true;
    }


    private KiiUserCallBack mUserCallBack = new KiiUserCallBack() {
        @Override
        public void onTaskCancel(int token) {
            super.onTaskCancel(token);
        }

        @Override
        public void onLoginCompleted(int token, KiiUser user, Exception exception) {
            super.onLoginCompleted(token, user, exception);
            dismissProgress();
            if (exception == null) {
                Toast.makeText(LoginActivity.this, R.string.log_in_success, Toast.LENGTH_SHORT).show();
                Settings.saveToken(user.getAccessToken());
                Settings.saveEmail(user.getEmail());
                LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_IN));
                KiiSync.asyncFullSync();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onRegisterCompleted(int token, KiiUser user, Exception exception) {
            super.onRegisterCompleted(token, user, exception);
            dismissProgress();
            if (exception == null) {
                Toast.makeText(LoginActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                long expires_at = user.getAccessTokenBundle().getLong("expires_at");
                Settings.saveToken(user.getAccessToken());
                Settings.saveEmail(user.getEmail());
                Settings.saveExp(expires_at);
                LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_IN));
                KiiSync.asyncFullSync();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onResetPasswordCompleted(int token, Exception exception) {
            super.onResetPasswordCompleted(token, exception);
            dismissProgress();
            if (exception == null) {
                Toast.makeText(LoginActivity.this, R.string.reset_password_success, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(LoginActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    private void initProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setCancelable(true);
    }

    protected void showProgress() {
        initProgressDialog();
        progressDialog.show();
    }

    protected void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    public void onInputDialogTextDone(String text) {
        KiiUser.resetPassword(mUserCallBack, text);
    }
}
