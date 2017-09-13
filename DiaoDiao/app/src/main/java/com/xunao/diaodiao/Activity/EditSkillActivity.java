package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xunao.diaodiao.Bean.FillSkillReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.EditSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.EditSkillView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class EditSkillActivity extends BaseActivity implements EditSkillView, View.OnClickListener {

    @Inject
    EditSkillPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.person_code)
    EditText personCode;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.person_num)
    EditText personNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.infor_num)
    TextView inforNum;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.code_delete)
    ImageView codeDelete;
    @BindView(R.id.code_reverse)
    ImageView codeReverse;
    @BindView(R.id.code_reverse_delete)
    ImageView codeReverseDelete;
    @BindView(R.id.certification)
    ImageView certification;
    @BindView(R.id.certi_delete)
    ImageView certiDelete;
    @BindView(R.id.login)
    Button login;

    private RecyclerArrayAdapter<String> adapter;

    private String[] skills = {"家电维修", "水泥回填", "家睡维修", "水点回填", "国电维修", "水我回填"};
    private List<String> skillsName = new ArrayList<>();
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;
    private int SELECT_TYPE = 1;
    private String codeUrl;
    private String codeReverseUrl;
    private String certifyUrl;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditSkillActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, PersonalRes.TechnicianInfo technicianInfo) {
        Intent intent = new Intent(context, EditSkillActivity.class);
        intent.putExtra(INTENT_KEY, technicianInfo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_skill);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "完善资料");

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsName.toString().contains(s)){
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                }else{
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    if (skillsName.toString().contains(s)){
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView)v).setTextColor(getResources().getColor(R.color.gray));
                        skillsName.remove(s);
                    }else{
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView)v).setTextColor(Color.WHITE);
                        skillsName.add(s);
                    }
                });
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            String skillItem = adapter.getAllData().get(i);
            if (skillsName.toString().contains(skillItem)){
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView)view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsName.remove(skillItem);
            }else{
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView)view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsName.add(skillItem);
            }

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        information.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.toString().length();
                inforNum.setText(len+"/200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login.setOnClickListener(v -> {
            goInAppAction();
        });

        code.setOnClickListener(this);
        codeReverse.setOnClickListener(this);
        codeDelete.setOnClickListener(this);
        codeReverseDelete.setOnClickListener(this);
        certification.setOnClickListener(this);
        certiDelete.setOnClickListener(this);

        if (getIntent().getSerializableExtra(INTENT_KEY) != null){
            PersonalRes.TechnicianInfo info = (PersonalRes.TechnicianInfo) getIntent().getSerializableExtra(INTENT_KEY);
            name.setText(info.getName());
            phone.setText(info.getMobile());
            personCode.setText(info.getCard());
            address.setText(info.getAddress());
            addressDetail.setText(info.getProvince()+info.getCity()+info.getDistrict()+"");
            year.setText(info.getExperience());
            personNum.setText(info.getTeam_number()+"");
            skillsName.addAll(Arrays.asList(info.getProject_type().split(",")));

            information.setText(info.getEvaluate());
            inforNum.setText(info.getEvaluate().length()+" / 200");
            if (!TextUtils.isEmpty(info.getCard_front())){
                codeUrl = info.getCard_front();
                Glide.with(this).load(codeUrl).into(code);
                codeDelete.setVisibility(View.VISIBLE);
            }else{
                Glide.with(this).load(R.drawable.shangchuan_zheng).into(code);
                codeDelete.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(info.getCard_back())){
                codeReverseUrl = info.getCard_back();
                Glide.with(this).load(codeReverseUrl).into(codeReverse);
                codeReverseDelete.setVisibility(View.VISIBLE);
            }else{
                Glide.with(this).load(R.drawable.shangchuan_fan).into(codeReverse);
                codeDelete.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(info.getCertificate())){
                certifyUrl = info.getCertificate();
                Glide.with(this).load(certifyUrl).into(certification);
                certiDelete.setVisibility(View.VISIBLE);
            }else{
                Glide.with(this).load(R.drawable.shangchuan).into(certification);
                certiDelete.setVisibility(View.GONE);
            }

        }

        adapter.addAll(skills);
        initImagePicker();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(false);
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

        if (TextUtils.isEmpty(personCode.getText().toString())){
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

        if (TextUtils.isEmpty(personNum.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(skillsName.toString())){
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
        req.setCard(personCode.getText().toString());
        req.setAddress(address.getText().toString());
        req.setExperience(year.getText().toString());
        req.setTeam_number(Integer.valueOf(personNum.getText().toString()));
        req.setEvaluate(information.getText().toString());
        String projectType = skillsName.toString();
        projectType = projectType.substring(1, projectType.length()-1);
        req.setProject_type(projectType);
        req.setCard_front(Utils.Bitmap2StrByBase64(codeUrl));
        req.setCard_back(Utils.Bitmap2StrByBase64(codeReverseUrl));
        req.setCertificate(Utils.Bitmap2StrByBase64(certifyUrl));

        presenter.fillSkillInfor(this, req);

    }

    @Override
    public void getData(LoginResBean bean) {
        finish();
        ToastUtil.show("完善完成");
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.code:
                selectPhoto();
                SELECT_TYPE = 4;
                break;

            case R.id.code_reverse:
                selectPhoto();
                SELECT_TYPE = 5;
                break;

            case R.id.code_delete:
                Glide.with(this).load(R.drawable.shangchuan_zheng).into(code);
                codeDelete.setVisibility(View.GONE);
                break;

            case R.id.code_reverse_delete:
                Glide.with(this).load(R.drawable.shangchuan_fan).into(codeReverse);
                codeReverseDelete.setVisibility(View.GONE);
                break;

            case R.id.certification:
                selectPhoto();
                SELECT_TYPE = 3;
                break;

            case R.id.certi_delete:
                Glide.with(this).load(R.drawable.shangchuan).into(certification);
                certiDelete.setVisibility(View.GONE);
                break;
        }
    }


    private void setImagePath(String path) {
        switch (SELECT_TYPE) {
            case 3:
                Glide.with(this).load(path).placeholder(getResources().getDrawable(R.drawable.shangchuan)).into(certification);
                certiDelete.setVisibility(View.VISIBLE);
                certifyUrl = path;
                break;

            case 4:
                Glide.with(this).load(path).placeholder(getResources().getDrawable(R.drawable.shangchuan_zheng)).into(code);
                codeDelete.setVisibility(View.VISIBLE);
                codeUrl = path;
                break;

            case 5:
                Glide.with(this).load(path).placeholder(getResources().getDrawable(R.drawable.shangchuan_fan)).into(codeReverse);
                codeReverseDelete.setVisibility(View.VISIBLE);
                codeReverseUrl = path;
                break;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                setImagePath(images.get(0).path);
            } else {
                ToastUtil.show("没有数据");
            }
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
