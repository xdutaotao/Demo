package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.LoginPresenter;
import com.xunao.diaodiao.Present.RegisterPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.LoginView;
import com.xunao.diaodiao.View.RegisterView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.CUSTOM_TYPE;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

public class SelectMemoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, RegisterView {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.company_box)
    CheckBox companyBox;
    @BindView(R.id.skill_box)
    CheckBox skillBox;
    @BindView(R.id.cumstom_box)
    CheckBox cumstomBox;

    @Inject
    RegisterPresenter presenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectMemoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_memory);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "选择角色");

        companyBox.setOnCheckedChangeListener(this);
        skillBox.setOnCheckedChangeListener(this);
        cumstomBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked){
            companyBox.setChecked(isChecked);
            skillBox.setChecked(isChecked);
            cumstomBox.setChecked(isChecked);
            return;
        }

        SpannableStringBuilder builder;
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));

        switch (buttonView.getId()){
            case R.id.company_box:
                companyBox.setChecked(isChecked);
                skillBox.setChecked(!isChecked);
                cumstomBox.setChecked(!isChecked);

                builder = new SpannableStringBuilder("选择角色后不可跟换，确定选择 暖通公司 吗？");
                builder.setSpan(span, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                new IOSDialog(this, 0.9f).builder()
                        .setMsg(builder)
                        .setMsgSize(R.dimen.small_font_size)
                        .setNegativeButton("取消", v -> {
                            companyBox.setChecked(false);
                        })
                        .setNegativeBtnColor(R.color.actionbar_sel_color)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("确认", v -> {
                            presenter.select(SelectMemoryActivity.this, COMPANY_TYPE);

                        })
                        .show();
                break;

            case R.id.skill_box:
                companyBox.setChecked(!isChecked);
                skillBox.setChecked(isChecked);
                cumstomBox.setChecked(!isChecked);

                builder = new SpannableStringBuilder("选择角色后不可跟换，确定选择 技术人员 吗？");
                builder.setSpan(span, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                new IOSDialog(this, 0.9f).builder()
                        .setMsg(builder)
                        .setMsgSize(R.dimen.small_font_size)
                        .setNegativeButton("取消", v -> {
                            skillBox.setChecked(false);
                        })
                        .setNegativeBtnColor(R.color.actionbar_sel_color)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("确认", v -> {
                            presenter.select(SelectMemoryActivity.this, SKILL_TYPE);

                        })
                        .show();
                break;

            case R.id.cumstom_box:
                companyBox.setChecked(!isChecked);
                skillBox.setChecked(!isChecked);
                cumstomBox.setChecked(isChecked);

                builder = new SpannableStringBuilder("选择角色后不可跟换，确定选择 家庭用户 吗？");
                builder.setSpan(span, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                new IOSDialog(this, 0.9f).builder()
                        .setMsg(builder)
                        .setMsgSize(R.dimen.small_font_size)
                        .setNegativeButton("取消", v -> {
                            cumstomBox.setChecked(false);
                        })
                        .setNegativeBtnColor(R.color.actionbar_sel_color)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("确认", v -> {
                            presenter.select(SelectMemoryActivity.this, CUSTOM_TYPE);

                        })
                        .show();

                break;
        }
    }

    @Override
    public void getData(String result) {
        int type = Integer.valueOf(result);
        ShareUtils.putValue(Constants.TYPE_KEY, type);
//        switch (type){
//            case COMPANY_TYPE:
//                SelectCompanyActivity.startActivity(SelectMemoryActivity.this);
//                break;
//
//            case SKILL_TYPE:
//                SelectSkillActivity.startActivity(SelectMemoryActivity.this);
//                break;
//
//            case CUSTOM_TYPE:
//                SelectNormalActivity.startActivity(SelectMemoryActivity.this);
//                break;
//        }
        finish();
    }

    @Override
    public void onFailure() {

    }


}
