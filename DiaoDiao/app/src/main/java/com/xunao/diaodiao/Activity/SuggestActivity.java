package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.qq)
    EditText qq;

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
        showToolbarBack(toolBar, titleText, "用户反馈");
        qq.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                actionSubmit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actionSubmit() {
        if(TextUtils.isEmpty(information.getText().toString())){
            ToastUtil.show("内容不能为空");
            return;
        }

        if(TextUtils.isEmpty(qq.getText().toString())){
            ToastUtil.show("手机号或者QQ不能为空");
            return;
        }

        presenter.submitSuggest(this, qq.getText().toString(), information.getText().toString());
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
        setResult(RESULT_OK);
        finish();
    }
}
