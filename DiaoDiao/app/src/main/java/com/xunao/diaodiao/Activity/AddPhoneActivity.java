package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.WeiXinReq;
import com.xunao.diaodiao.Bean.WeiXinRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Model.UserInfo;
import com.xunao.diaodiao.Present.AddPhonePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddPhoneView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class AddPhoneActivity extends BaseActivity implements AddPhoneView {

    @Inject
    AddPhonePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code_input)
    EditText codeInput;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.addPhoneBtn)
    Button addPhoneBtn;

    public static void startActivity(Context context, String openID) {
        Intent intent = new Intent(context, AddPhoneActivity.class);
        intent.putExtra(INTENT_KEY, openID);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "绑定手机号");

        addPhoneBtn.setOnClickListener(v -> {
            if(TextUtils.isEmpty(phone.getText())){
                ToastUtil.show("请输入手机号");
                return;
            }

            if (TextUtils.isEmpty(codeInput.getText())){
                ToastUtil.show("请获取验证码");
                return;
            }

            WeiXinReq req = new WeiXinReq();
            req.setCode(codeInput.getText().toString());
            req.setMobile(phone.getText().toString());
            req.setOpenID(getIntent().getStringExtra(INTENT_KEY));
            presenter.wxPhone(this, req);
        });

        getCode.setOnClickListener(v -> {
            if(TextUtils.isEmpty(phone.getText())){
                ToastUtil.show("请输入手机号");
                return;
            }

            getCode.setBackgroundResource(R.drawable.btn_code_not);

            presenter.checkPhone(this, phone.getText().toString());
        });
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getData(String s) {
        codeInput.setText(s);
    }

    @Override
    public void getRes(UserInfo s) {
        ShareUtils.putValue(TYPE_KEY, s.getType());
    }
}
