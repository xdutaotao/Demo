package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.AddBankRes;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Bean.EditBankReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.AddBankPresenter;
import com.xunao.diaodiao.Present.EditBankPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.AddBankView;
import com.xunao.diaodiao.View.EditBankView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import rx.Observable;
import rx.Subscription;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class EditBankActivity extends BaseActivity implements EditBankView, View.OnClickListener {

    @Inject
    EditBankPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.select_bank)
    RelativeLayout selectBank;
    @BindView(R.id.card)
    TextView card;
    @BindView(R.id.bank_type)
    TextView bankType;
    @BindView(R.id.select_card_type)
    RelativeLayout selectCardType;
    @BindView(R.id.bank_addr)
    TextView bankAddr;
    @BindView(R.id.select_card_addr)
    RelativeLayout selectCardAddr;
    @BindView(R.id.bank_item)
    EditText bankItem;
    @BindView(R.id.select_card_item)
    RelativeLayout selectCardItem;
    @BindView(R.id.post)
    Button post;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;
    private BankListRes.BankCard item;

    public static void startActivity(Context context, BankListRes.BankCard item) {
        Intent intent = new Intent(context, EditBankActivity.class);
        intent.putExtra(INTENT_KEY, item);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bank);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "银行卡信息");

        item = (BankListRes.BankCard) getIntent().getSerializableExtra(INTENT_KEY);
        bankName.setText(item.getCard_name());
        card.setText(item.getCard_num());
        name.setText(item.getName());
        bankAddr.setText(item.getBank_info());
        bankItem.setText(item.getBank_branch());

        provinceId = item.getProvince();
        cityId = item.getCity();
        districtId = item.getDistrict();

        post.setOnClickListener(this);


        selectCardAddr.setOnClickListener(v -> {

            if (picker != null){
                if (TextUtils.isEmpty(bankAddr.getText())){
                    picker.setSelectedItem("上海", "上海", "长宁");
                }else{
                    String[] addresss = bankAddr.getText().toString().split("-");
                    if (addresss.length == 3){
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                    }else if(addresss.length > 0){
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

            case R.id.post:
                postData();
                break;

        }
    }


    private void postData() {

        if (TextUtils.isEmpty(bankAddr.getText().toString())) {
            ToastUtil.show("银行卡所在地不能为空");
            return;
        }

        if (TextUtils.isEmpty(bankItem.getText().toString())) {
            ToastUtil.show("银行卡所属支行不能为空");
            return;
        }


        EditBankReq req = new EditBankReq();
        req.setCity(cityId);
        req.setProvince(provinceId);
        req.setDistrict(districtId);
        req.setCard(item.getCard_num());
        req.setBank_branch(bankItem.getText().toString().trim());
        presenter.bindingCard(this, req);
    }

    @Override
    public void getData(Object s) {
        ToastUtil.show("修改成功");
        finish();
    }

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            if (Constants.addressResult.size() == 0)
                Constants.addressResult.addAll(result);
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
                        bankAddr.setText(province.getAreaName() + "-" + city.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName());
                    } else {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        districtId = Integer.valueOf(county.getAreaId());
                        bankAddr.setText(province.getAreaName() + "-"
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
