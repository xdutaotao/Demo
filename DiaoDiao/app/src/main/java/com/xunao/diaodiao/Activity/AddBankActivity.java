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
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.BindBankReq;
import com.xunao.diaodiao.Present.AddBankPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddBankView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private RecyclerArrayAdapter<BankListRes.BankCard> adapter;
    private List<String> selectNames = new ArrayList<>();
    private BottomSheetDialog dialog;
    private BankListRes.BankCard bankCard;

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

        adapter = new RecyclerArrayAdapter<BankListRes.BankCard>(this, R.layout.add_bank_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, BankListRes.BankCard s) {
                baseViewHolder.setText(R.id.bank_text, s.getCard_name());
                baseViewHolder.setText(R.id.bank_icon, s.getImg());
                if (selectNames.size() > 0)
                    baseViewHolder.setVisible(R.id.select, TextUtils.equals(s.getCard_name(), selectNames.get(0)));
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            bankCard = adapter.getAllData().get(i);
            String select = bankCard.getCard_name();
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

    private void getCode(){
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("开户人不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("手机号不能为空");
            return;
        }

        if (TextUtils.isEmpty(card.getText().toString())) {
            ToastUtil.show("银行卡号不能为空");
            return;
        }

        if (TextUtils.isEmpty(getCode.getText().toString())){
            ToastUtil.show("身份证不能为空");
            return;
        }

        BindBankReq req = new BindBankReq();
        req.setName(name.getText().toString());
        req.setMobile(phone.getText().toString());
        req.setCard(bankCard.getCard());
        req.setIdentity_card(getCode.getText().toString());
        //区分标志
        req.setTrade_no("");
        req.setType(101);
        presenter.bindingCard(this, req);

    }

    private void postData() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(card.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phoneCode.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }


        BindBankReq req = new BindBankReq();
        req.setName(name.getText().toString());
        req.setMobile(phone.getText().toString());
        req.setCard(bankCard.getCard());
        req.setCode(phoneCode.getText().toString());
        req.setIdentity_card(getCode.getText().toString());
        req.setTrade_no("123");
        req.setType(101);
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
    public void getData(String s) {

    }

    @Override
    public void getBankList(BankListRes res) {
        adapter.addAll(res.getBankCard());
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
