package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Present.EditPersonalPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.EditPersonalView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class EditPersonalActivity extends BaseActivity implements EditPersonalView, View.OnClickListener {

    @Inject
    EditPersonalPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.address_detail)
    EditText addressDetail;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditPersonalActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, PersonalRes.FamilyInfo familyInfo) {
        Intent intent = new Intent(context, EditPersonalActivity.class);
        intent.putExtra(INTENT_KEY, familyInfo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "普通用户");

        login.setOnClickListener(this);

        if (getIntent().getSerializableExtra(INTENT_KEY) != null){
            PersonalRes.FamilyInfo info = new PersonalRes.FamilyInfo();
            name.setText(info.getName());
            phone.setText(info.getMobile());
            address.setText(info.getAddress());
            addressDetail.setText(info.getProvince()+info.getCity()+info.getDistrict()+"");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                if (TextUtils.isEmpty(name.getText())){
                    ToastUtil.show("不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone.getText())){
                    ToastUtil.show("不能为空");
                    return;
                }

                if (TextUtils.isEmpty(address.getText())){
                    ToastUtil.show("不能为空");
                    return;
                }

                if (TextUtils.isEmpty(addressDetail.getText())){
                    ToastUtil.show("不能为空");
                    return;
                }
                FillNormalReq req = new FillNormalReq();
                req.setName(name.getText().toString());
                req.setMobile(phone.getText().toString());
                req.setAddress(address.getText().toString());
                req.setProvince(1);
                req.setCity(2);
                req.setDistrict(3);

                presenter.fillInfor(this, req);

                break;
        }
    }

    @Override
    public void getData(LoginResBean resBean) {
        ToastUtil.show("提交成功");
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
