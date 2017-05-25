package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.cworker.Present.AddPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.AddView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class AddressActivity extends BaseActivity implements AddView {

    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @Inject
    AddPresenter presenter;

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra(INTENT_KEY, s);
        context.startActivityForResult(intent, PersonalActivity.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "联系地址");
        presenter.attachView(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            address.setText(getIntent().getStringExtra(INTENT_KEY));
        }

        save.setOnClickListener(v -> {
            if (TextUtils.isEmpty(address.getText().toString())) {
                ToastUtil.show("地址不能为空");
                return;
            }
            presenter.changeAddress(AddressActivity.this, address.getText().toString());
        });
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(String s) {
        ToastUtil.show(s);
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY, address.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
