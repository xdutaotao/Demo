package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FillNormalReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.EditPersonalPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.EditPersonalView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.addressResult;

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
    TextView address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;

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

        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            PersonalRes.FamilyInfo info = (PersonalRes.FamilyInfo) getIntent().getSerializableExtra(INTENT_KEY);
            name.setText(info.getName());
            phone.setText(info.getMobile());
            address.setText(info.getRegion());
            //addressDetail.setText(info.getProvince() + info.getCity() + info.getDistrict() + "");
            addressDetail.setText(info.getAddress());
            provinceId = info.getProvince();
            cityId = info.getCity();
            districtId = info.getDistrict();
        }

        addressLayout.setOnClickListener(v -> {
            if (picker != null){
                if (TextUtils.isEmpty(address.getText())){
                    picker.setSelectedItem("上海", "上海", "长宁");
                }else{
                    String[] addresss = address.getText().toString().split("-");
                    if (addresss.length == 3){
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                    }else{
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[1]);
                    }

                }
                picker.show();
            }
        });


        if(Constants.addressResult.size() == 0)
            presenter.getAddressData(this);
        else
            getAddressData(Constants.addressResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (TextUtils.isEmpty(name.getText())) {
                    ToastUtil.show("用户名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone.getText())) {
                    ToastUtil.show("手机号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(address.getText())) {
                    ToastUtil.show("地区不能为空");
                    return;
                }

                if (TextUtils.isEmpty(addressDetail.getText())) {
                    ToastUtil.show("地址不能为空");
                    return;
                }
                FillNormalReq req = new FillNormalReq();
                req.setName(name.getText().toString());
                req.setMobile(phone.getText().toString());
                req.setAddress(addressDetail.getText().toString());
                req.setProvince(provinceId);
                req.setCity(cityId);
                req.setDistrict(districtId);

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
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            if(addressResult.size() == 0)
                addressResult.addAll(result);
            picker = new AddressPicker(this, result);
            picker.setHideProvince(false);
            picker.setHideCounty(false);
            picker.setColumnWeight(0.8f, 1.0f, 1.0f);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    if (county == null) {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        address.setText(province.getAreaName() + "-" + city.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName());
                    } else {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        districtId = Integer.valueOf(county.getAreaId());
                        address.setText(province.getAreaName() + "-"
                                + city.getAreaName() + "-"
                                + county.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                }
            });
        }

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
