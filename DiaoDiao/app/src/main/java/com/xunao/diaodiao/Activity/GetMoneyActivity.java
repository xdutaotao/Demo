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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.BankListRes;
import com.xunao.diaodiao.Bean.CashRecordRes;
import com.xunao.diaodiao.Bean.GetCashRes;
import com.xunao.diaodiao.Present.GetMoneyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.GetMoneyView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class GetMoneyActivity extends BaseActivity implements GetMoneyView, View.OnClickListener {

    @Inject
    GetMoneyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.choose_bank)
    RelativeLayout chooseBank;
    @BindView(R.id.input_money)
    EditText inputMoney;
    @BindView(R.id.get_all_money)
    TextView getAllMoney;
    @BindView(R.id.money_user)
    TextView moneyUser;
    @BindView(R.id.get_money)
    Button getMoney;
    @BindView(R.id.bank_name)
    TextView bankName;

    private RecyclerArrayAdapter<BankListRes.BankCard> adapter;
    private List<String> selectNames = new ArrayList<>();
    private BottomSheetDialog dialog;
    private BankListRes.BankCard bankCard;

    public static void startActivity(Context context, String allMoney) {
        Intent intent = new Intent(context, GetMoneyActivity.class);
        intent.putExtra(INTENT_KEY, allMoney);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "提现");

        dialog = new BottomSheetDialog(this);
        chooseBank.setOnClickListener(this);
        getAllMoney.setOnClickListener(this);
        getMoney.setOnClickListener(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            moneyUser.setText("可提现金额 " +
                    new DecimalFormat("#.00").format(Float.valueOf(getIntent().getStringExtra(INTENT_KEY))) + "元");
        }

        adapter = new RecyclerArrayAdapter<BankListRes.BankCard>(this, R.layout.add_bank_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, BankListRes.BankCard s) {
                baseViewHolder.setText(R.id.bank_text, s.getCard_name());
                baseViewHolder.setImageUrl(R.id.bank_icon, s.getImg());
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

        presenter.getBankList(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_bank:
                showBottomSheetDialog();
                break;

            case R.id.get_all_money:
                //postMoney(getIntent().getStringExtra(INTENT_KEY));
                inputMoney.setText(moneyUser.getText());
                break;

            case R.id.get_money:
                postMoney(inputMoney.getText().toString());
                break;
        }
    }

    private void postMoney(String money) {
        if (TextUtils.isEmpty(money)){
            ToastUtil.show("请输入金额");
            return;
        }

        if (TextUtils.isEmpty(bankName.getText())){
            ToastUtil.show("请选择银行卡");
            return;
        }


        GetCashRes res = new GetCashRes();
        res.setCash(money);
        res.setCard(bankCard.getCard_num());
        presenter.applyCash(this, res);

    }

    @Override
    public void getData(Object s) {
        ToastUtil.show("提现成功");
        finish();
    }

    @Override
    public void getBankList(BankListRes res) {
        adapter.addAll(res.getBankCard());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        MenuItem item = menu.findItem(R.id.action_contact);
        item.setTitle("提现记录");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                CashRecordActivity.startActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
