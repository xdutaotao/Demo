package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SelectNormalPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SelectNormalView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SelectNormalActivity extends BaseActivity implements SelectNormalView {

    @Inject
    SelectNormalPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.go_in_app)
    Button goInApp;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address)
    EditText address;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectNormalActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_normal);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "普通用户");
        goInApp.setOnClickListener(v -> {
            goInAction();
        });
    }

    private void goInAction(){
        if (TextUtils.isEmpty(name.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        FillNormalReq req = new FillNormalReq();
        req.setAddress(address.getText().toString());
        req.setName(name.getText().toString());
        req.setCity(1);
        req.setDistrict(2);
        req.setProvince(3);
        req.setMobile(phone.getText().toString());
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        presenter.fillNormalInfor(this ,req);

    }

    @Override
    public void getData(LoginResBean req) {
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
