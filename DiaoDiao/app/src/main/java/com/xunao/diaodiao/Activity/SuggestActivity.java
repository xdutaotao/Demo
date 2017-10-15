package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Present.SuggestPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SuggestView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class SuggestActivity extends BaseActivity implements SuggestView {

    @Inject
    SuggestPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.login)
    Button login;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SuggestActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "意见反馈");

        login.setOnClickListener(v -> {
            if (content.getText().toString().length() == 0){
                ToastUtil.show("请输入意见");
                return;
            }

            presenter.submitSuggest(SuggestActivity.this, content.getText().toString());
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentNum.setText(s.length() + " / 200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
        ToastUtil.show("发送成功!");
        finish();
    }

    @Override
    public void getData(TypeInfoRes s) {

    }
}
