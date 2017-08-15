package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectMemoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectMemoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_memory);
        ButterKnife.bind(this);

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

        switch (buttonView.getId()){
            case R.id.company_box:
                companyBox.setChecked(isChecked);
                skillBox.setChecked(!isChecked);
                cumstomBox.setChecked(!isChecked);

                new IOSDialog(this).builder()
                        .setMsg(Html.fromHtml("选择角色后不可跟换，确定选择<font color='red'>暖通公司</font>"+" 吗？").toString())
                        .setNegativeButton("取消", null)
                        .setNegativeBtnColor(R.color.actionbar_sel_color)
                        .setPositiveBtnColor(R.color.black)
                        .setPositiveButton("确认", v -> {

                        })
                        .show();
                break;

            case R.id.skill_box:
                companyBox.setChecked(!isChecked);
                skillBox.setChecked(isChecked);
                cumstomBox.setChecked(!isChecked);
                break;

            case R.id.cumstom_box:
                companyBox.setChecked(!isChecked);
                skillBox.setChecked(!isChecked);
                cumstomBox.setChecked(isChecked);
                break;
        }
    }
}
