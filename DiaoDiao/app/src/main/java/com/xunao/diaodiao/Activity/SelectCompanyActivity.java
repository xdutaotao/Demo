package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SelectCompanyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SelectCompanyView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class SelectCompanyActivity extends BaseActivity implements SelectCompanyView {

    @Inject
    SelectCompanyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.go_in_app)
    Button goInApp;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.contact_phone)
    EditText contactPhone;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectCompanyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "暖通公司");

        goInApp.setOnClickListener(v -> {
            goInAppAction();
        });
    }

    public void goInAppAction(){
        if (TextUtils.isEmpty(name.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(addressDetail.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(contact.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactPhone.getText().toString())){
            ToastUtil.show("公司名称不能为空");
            return;
        }

        FillCompanyReq fillCompanyReq = new FillCompanyReq();
        fillCompanyReq.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        fillCompanyReq.setName(name.getText().toString());
        fillCompanyReq.setProvince(1);
        fillCompanyReq.setCity(2);
        fillCompanyReq.setDistrict(3);
        fillCompanyReq.setAddress(address.getText().toString());
        fillCompanyReq.setTel(phone.getText().toString());
        fillCompanyReq.setContact(contact.getText().toString());
        fillCompanyReq.setContact_mobile(contactPhone.getText().toString());
        presenter.fillInfor(this, fillCompanyReq);

    }


    @Override
    public void getData(LoginResBean bean) {
        ShareUtils.putValue(TYPE_KEY, COMPANY_TYPE);
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
