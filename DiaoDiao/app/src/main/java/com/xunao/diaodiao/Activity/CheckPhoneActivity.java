package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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

        registerBtn.setText(type==0? "注册" : "修改");

        goLogin.setVisibility(type == 0? View.VISIBLE : View.GONE);

        phoneInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        codeInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        getCode.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

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

        getCode.setBackgroundResource(R.drawable.btn_code_not);

        presenter.checkPhone(this, phoneInput.getText().toString());


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

        presenter.register(this, phoneInput.getText().toString(),
                codeInput.getText().toString(), codeInput.getText().toString());
    }

    @Override
    public void getData(String result) {
        if (result.length() == 4){
            getCode.setBackgroundResource(R.drawable.btn_code);
            codeInput.setText(result);
        }else{
            SelectMemoryActivity.startActivity(this);
            finish();
        }

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
