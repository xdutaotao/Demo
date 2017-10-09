package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Present.LoginPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.LoginView;
import com.xunao.diaodiao.wxapi.WXEntryActivity;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener, Handler.Callback, PlatformActionListener {

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

//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(s) && TextUtils.isEmpty(pwd.getText())){
//                    login.setAlpha(0.2f);
//                }else{
//                    login.setAlpha(1.0f);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
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
                //忘记密码
                CheckPhoneActivity.startActivity(this, 1);
                break;

            case R.id.wx_login:
                //WXEntryActivity.startActivity(this);
                weixinLogin();
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    private void weixinLogin(){
        ShareSDK.initSDK(this);
        authorize(new Wechat(LoginActivity.this),true);
    }

    // 授权登录
    private void authorize(Platform plat,Boolean isSSO) {
        // 判断指定平台是否已经完成授权
        if (plat.isValid()) {
            // 已经完成授权，直接读取本地授权信息，执行相关逻辑操作（如登录操作）
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        // 是否使用SSO授权：true不使用，false使用
        plat.SSOSetting(isSSO);
        // 获取用户资料
        plat.showUser(null);
    }

    // 取消授权
    private void cancleAuth(){
        Platform wxPlatform = ShareSDK.getPlatform(Wechat.NAME);
        wxPlatform.removeAccount();
        ToastUtil.show("取消授权成功!");
    }

    // 回调：授权成功
    public void onComplete(Platform platform, int action,HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            // 业务逻辑处理：比如登录操作
            String userName = platform.getDb().getUserName(); // 用户昵称
            String userId	= platform.getDb().getUserId();	  // 用户Id
            String platName = platform.getName();			  // 平台名称

            login(platName, userName, res);
        }
    }
    // 回调：授权失败
    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }
    // 回调：授权取消
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }
    // 业务逻辑：登录处理
    private void login(String plat, String userId,HashMap<String, Object> userInfo) {
        Toast.makeText(this, "用户ID:"+userId, Toast.LENGTH_SHORT).show();
        Message msg = new Message();
        msg.what    = MSG_LOGIN;
        msg.obj     = plat;
        UIHandler.sendMessage(msg, this);
    }

    // 统一消息处理
    private static final int MSG_USERID_FOUND 	= 1; // 用户信息已存在
    private static final int MSG_LOGIN 			= 2; // 登录操作
    private static final int MSG_AUTH_CANCEL 	= 3; // 授权取消
    private static final int MSG_AUTH_ERROR 	= 4; // 授权错误
    private static final int MSG_AUTH_COMPLETE 	= 5; // 授权完成

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case MSG_USERID_FOUND:
                Toast.makeText(this, "用户信息已存在，正在跳转登录操作......", 	Toast.LENGTH_SHORT).show();
                break;
            case MSG_LOGIN:
                Toast.makeText(this, "使用微信帐号登录中...", 					Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_CANCEL:
                Toast.makeText(this, "授权操作已取消", 						Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_ERROR:
                Toast.makeText(this, "授权操作遇到错误，请阅读Logcat输出", 		Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_COMPLETE:
                Toast.makeText(this,"授权成功，正在跳转登录操作…", 				Toast.LENGTH_SHORT).show();
                // 执行相关业务逻辑操作，比如登录操作
                String userName = new Wechat(LoginActivity.this).getDb().getUserName(); // 用户昵称
                String userId	= new Wechat(LoginActivity.this).getDb().getUserId();   // 用户Id
                String platName = new Wechat(LoginActivity.this).getName();			   // 平台名称

                //login(platName, userId, null);
                AddPhoneActivity.startActivity(this, userId);
                break;
        }
        return false;
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

        presenter.login(this, name.getText().toString().trim(), pwd.getText().toString().trim());
    }

    @Override
    public void getData(LoginResBean data) {
        if (data.getType() == 0){
            SelectMemoryActivity.startActivity(this);
        }else{
            ShareUtils.putValue(TYPE_KEY, data.getType());
        }

        ToastUtil.show("登录成功");
        RxBus.getInstance().post(LOGIN_AGAIN);
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
        CheckPhoneActivity.startActivity(this, 0);
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
