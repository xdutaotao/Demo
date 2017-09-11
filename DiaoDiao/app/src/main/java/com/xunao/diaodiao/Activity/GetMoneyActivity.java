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
import com.xunao.diaodiao.Bean.GetCashRes;
import com.xunao.diaodiao.Present.GetMoneyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
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

    private RecyclerArrayAdapter<String> adapter;
    private List<String> selectNames = new ArrayList<>();

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

        chooseBank.setOnClickListener(this);
        getAllMoney.setOnClickListener(this);
        getMoney.setOnClickListener(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))){
            moneyUser.setText("可提现金额 "+
            new DecimalFormat("#.00").format(Float.valueOf(getIntent().getStringExtra(INTENT_KEY)))+"元");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_bank:
                showBottomSheetDialog();
                break;

            case R.id.get_all_money:
                postMoney(getIntent().getStringExtra(INTENT_KEY));
                break;

            case R.id.get_money:
                postMoney(inputMoney.getText().toString());
                break;
        }
    }

    private void postMoney(String money){
        GetCashRes res = new GetCashRes();
        res.setCard(money);
        res.setCard("1111111111111");
        presenter.applyCash(this, res);

    }

    @Override
    public void getData(String s) {
        finish();
    }

    private void showBottomSheetDialog(){

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerArrayAdapter<String>(this, R.layout.add_bank_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.bank_text, s);
                if (selectNames.size() > 0)
                    baseViewHolder.setVisible(R.id.select, TextUtils.equals(s, selectNames.get(0)));
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            String select = adapter.getAllData().get(i);
            ToastUtil.show(select);
            if (selectNames.contains(select)){
                ((ImageView)view1.findViewById(R.id.select)).setVisibility(View.GONE);
                selectNames.clear();
            }else{
                ((ImageView)view1.findViewById(R.id.select)).setVisibility(View.VISIBLE);
                selectNames.clear();
                selectNames.add(select);
            }
            adapter.notifyDataSetChanged();

        });

        recyclerView.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        adapter.addAll(list);

        ImageView cancle = (ImageView) view.findViewById(R.id.cancle_action);
        cancle.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.setContentView(view);

        dialog.show();
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
