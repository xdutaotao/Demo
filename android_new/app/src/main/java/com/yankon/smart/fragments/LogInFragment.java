package com.yankon.smart.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.yankon.smart.R;
import com.yankon.smart.utils.Constants;
import com.yankon.smart.utils.KiiSync;
import com.yankon.smart.utils.Settings;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link android.app.Fragment} subclass.
 * create an instance of this fragment.
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_RESET_PASSWORD = 0x8001;

    @Bind(R.id.email_edit)
    EditText emailEdit;

    @Bind(R.id.password_edit)
    EditText passwordEdit;

    @Bind(R.id.forgot_password)
    TextView forgetPasswordButton;

    @Bind(R.id.button_register)
    Button registerButton;

    @Bind(R.id.button_log_in)
    Button logInButton;

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance(int sectionNumber) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        ButterKnife.bind(this, view);
        forgetPasswordButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_password:
                showResetPasswordDialog();
                break;
            case R.id.button_register:
                if (isInputValid()) {
                    String email = emailEdit.getText().toString();
                    String username = email.replace("@", "_");
                    KiiUser.builderWithEmail(email).withName(username).build()
                            .register(mUserCallBack, passwordEdit.getText().toString());
                    showProgress();
                }
                break;
            case R.id.button_log_in:
                if (isInputValid()) {
                    KiiUser.logIn(mUserCallBack, emailEdit.getText().toString(),
                            passwordEdit.getText().toString());
                    showProgress();
                }
                break;
            default:
                break;
        }
    }

    private void showResetPasswordDialog() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        InputDialogFragment newFragment = InputDialogFragment.newInstance(
                getActivity().getString(R.string.reset_password_title),
                emailEdit.getText().toString(),
                getActivity().getString(R.string.reset_password_hint),
                getActivity().getString(R.string.reset_password_text),
                InputDialogFragment.TYPE_RESET_PASSWORD);
        newFragment.setTargetFragment(this, REQUEST_RESET_PASSWORD);
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
                Toast.makeText(getActivity(), R.string.log_in_success, Toast.LENGTH_SHORT).show();
                Settings.saveToken(user.getAccessToken());
                Settings.saveEmail(user.getEmail());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_IN));
                KiiSync.asyncFullSync();
            } else {
                Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onRegisterCompleted(int token, KiiUser user, Exception exception) {
            super.onRegisterCompleted(token, user, exception);
            dismissProgress();
            if (exception == null) {
                Toast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_SHORT).show();
                long expires_at = user.getAccessTokenBundle().getLong("expires_at");
                Settings.saveToken(user.getAccessToken());
                Settings.saveEmail(user.getEmail());
                Settings.saveExp(expires_at);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(
                        Constants.INTENT_LOGGED_IN));
                KiiSync.asyncFullSync();
            } else {
                Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onResetPasswordCompleted(int token, Exception exception) {
            super.onResetPasswordCompleted(token, exception);
            dismissProgress();
            if (exception == null) {
                Toast.makeText(getActivity(), R.string.reset_password_success, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_RESET_PASSWORD:
                String email = data.getStringExtra(InputDialogFragment.ARG_TEXT);
                KiiUser.resetPassword(mUserCallBack, email);
                break;
        }
    }
}