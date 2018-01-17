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
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.AddBankPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.AddBankView;

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

/**
 * create by
 */
public class AddBankActivity extends BaseActivity implements AddBankView, View.OnClickListener {

    @Inject
    AddBankPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.select_bank)
    RelativeLayout selectBank;
    @BindView(R.id.card)
    EditText card;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.phone_code)
    EditText phoneCode;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.person_code)
    EditText personCode;
    @BindView(R.id.get_code)
    TextView getCode;
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

    private RecyclerArrayAdapter<BankListRes.ListBean> adapter;
    private List<String> selectNames = new ArrayList<>();
    private BottomSheetDialog dialog;
    private BankListRes.ListBean bankCard;
    private SinglePicker singlePicker;

    private String trade_no;
    private Subscription subscriber;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "绑定银行卡");
        selectBank.setOnClickListener(this);
        post.setOnClickListener(this);

        adapter = new RecyclerArrayAdapter<BankListRes.ListBean>(this, R.layout.add_bank_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, BankListRes.ListBean s) {
                baseViewHolder.setText(R.id.bank_text, s.getName());
                baseViewHolder.setImageUrl(R.id.bank_icon, s.getLogo());
                if (selectNames.size() > 0)
                    baseViewHolder.setVisible(R.id.select, TextUtils.equals(s.getName(), selectNames.get(0)));
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            bankCard = adapter.getAllData().get(i);
            String select = bankCard.getName();
            bankName.setText(select);
            if (selectNames.contains(select)) {
                ((ImageView) view1.findViewById(R.id.select)).setVisibility(View.GONE);
                selectNames.clear();
            } else {
                ((ImageView) view1.findViewById(R.id.select)).setVisibility(View.VISIBLE);
                selectNames.clear();
                selectNames.add(select);
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog = new BottomSheetDialog(this);
        presenter.getBankList(this);
        getCode.setOnClickListener(this);

        List<String> data = new ArrayList<>();
        data.add("借记卡");
        //data.add("信用卡");
        bankType.setText("借记卡");

        selectCardType.setOnClickListener(v -> {
            singlePicker = new SinglePicker<>(this, data);
            singlePicker.setCanceledOnTouchOutside(false);
            singlePicker.setSelectedIndex(1);
            singlePicker.setCycleDisable(true);
            singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
                @Override
                public void onItemPicked(int index, String item) {
                    //ToastUtil.show(item);
                    bankType.setText(item);
                }
            });
            singlePicker.show();
        });

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
            case R.id.select_bank:
                showBottomSheetDialog();
                break;

            case R.id.post:
                postData();
                break;

            case R.id.get_code:
                getCode();
                break;
        }
    }

    private void getCode() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("开户人不能为空");
            return;
        }

        if (!Utils.isIDCardValidate(personCode.getText().toString())) {
            ToastUtil.show("身份证输入错误");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (!Utils.isPhone(phone.getText().toString())) {
            ToastUtil.show("手机号输入错误");
            return;
        }

        if (TextUtils.isEmpty(card.getText().toString())) {
            ToastUtil.show("银行卡号不能为空");
            return;
        }

        if (TextUtils.isEmpty(getCode.getText().toString())) {
            ToastUtil.show("身份证不能为空");
            return;
        }

        BindBankReq req = new BindBankReq();
        req.setName(name.getText().toString());
        req.setBank_name(selectNames.get(0));
        req.setMobile(phone.getText().toString());
        req.setCard(card.getText().toString());
        req.setIdentity_card(personCode.getText().toString());

//        BindBankReq req = new BindBankReq();
//        req.setName("顾振福");
//        req.setBank_name("工商银行");
//        req.setMobile("18513212904");
//        req.setCard("6222021001096916918");
//        req.setIdentity_card("41142219910613003X");

        //区分标志
        req.setTrade_no("");
//        if(TextUtils.equals("信用卡", bankType.getText())){
//            req.setCard_type(102);
//        }else{
//            req.setCard_type(101);
//        }
        req.setCard_type(101);
        presenter.bindingCardGetVerify(this, req);

    }

    private void postData() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (TextUtils.isEmpty(card.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phoneCode.getText().toString())) {
            ToastUtil.show("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(bankAddr.getText().toString())) {
            ToastUtil.show("银行卡所在地不能为空");
            return;
        }

        if (TextUtils.isEmpty(bankItem.getText().toString())) {
            ToastUtil.show("银行卡所属支行不能为空");
            return;
        }


        BindBankReq req = new BindBankReq();
        req.setName(name.getText().toString());
        req.setBank_name(selectNames.get(0));
        req.setMobile(phone.getText().toString());
        req.setCard(card.getText().toString());
        req.setCode(phoneCode.getText().toString());
        req.setIdentity_card(personCode.getText().toString());
        req.setTrade_no(trade_no);
        if (TextUtils.equals("信用卡", bankType.getText())) {
            req.setCard_type(102);
        } else {
            req.setCard_type(101);
        }
        req.setBank_branch(bankItem.getText().toString().trim());
        req.setProvince(provinceId);
        req.setCity(cityId);
        req.setDistrict(districtId);
        presenter.bindingCard(this, req);
    }


    private void showBottomSheetDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageView cancle = (ImageView) view.findViewById(R.id.cancle_action);
        cancle.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void getData(AddBankRes s) {
        trade_no = s.getTrade_no();

        subscriber = Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .subscribe(aLong -> {
                    if (subscriber != null) {
                        if (aLong >= 60) {
                            stopTime();
                        } else {
                            getCode.setText((60 - aLong) + " s");
                        }
                    }

                });
    }

    private void stopTime() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
            subscriber = null;
        }
        getCode.setText("获取验证码");
    }

    @Override
    public void getData(Object s) {
        ToastUtil.show("绑定成功");
        finish();
    }

    @Override
    public void getBankList(BankListRes res) {
        adapter.addAll(res.getList());
    }

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            if(Constants.addressResult.size() == 0)
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
