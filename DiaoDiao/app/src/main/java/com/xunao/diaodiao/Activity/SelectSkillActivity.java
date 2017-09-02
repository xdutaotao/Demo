package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SelectSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SelectSkillView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class SelectSkillActivity extends BaseActivity implements SelectSkillView {

    @Inject
    SelectSkillPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.info_num)
    TextView infoNum;
    @BindView(R.id.go_in_app)
    Button goInApp;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.year)
    EditText year;

    private RecyclerArrayAdapter<String> adapter;

    private String[] skills = {"家电维修", "水泥回填", "家电维修", "水泥回填", "家电维修", "水泥回填"};
    private String skillsName ;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectSkillActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skill);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "技术人员");

        goInApp.setOnClickListener(v -> {
            goInAppAction();
        });

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            String skillItem = adapter.getAllData().get(i);
            if (skillsName.toString().contains(skillItem)){
                view.setBackground(getResources().getDrawable(R.drawable.btn_blank_bg));
                ((TextView)view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                String[] temp = skillsName.split(","+skillItem);
                skillsName = temp[0]+temp[1];
            }else{
                view.setBackground(getResources().getDrawable(R.drawable.btn_blue_bg));
                ((TextView)view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsName+=(skillItem+",");
            }

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.addAll(skills);
    }

    private void goInAppAction(){
        if (TextUtils.isEmpty(name.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(addressDetail.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(year.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(skillsName)){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(information.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        FillSkillReq req = new FillSkillReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setName(name.getText().toString());
        req.setMobile(phone.getText().toString());
        req.setProvince(1);
        req.setCity(2);
        req.setDistrict(3);
        req.setAddress(address.getText().toString());
        req.setExperience(year.getText().toString());
        req.setEvaluate(information.getText().toString());
        req.setProject_type(skillsName.substring(0, skillsName.length()-1));

        presenter.fillSkillInfor(this, req);

    }


    @Override
    public void getData(LoginResBean bean) {
        ShareUtils.putValue(TYPE_KEY, SKILL_TYPE);
        finish();
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
