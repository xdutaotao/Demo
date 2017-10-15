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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.EditSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.EditSkillView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

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
    TextView address;
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
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;

    private RecyclerArrayAdapter<TypeInfoRes.Type_Info> adapter;
    private List<TypeInfoRes.Type_Info> listData = new ArrayList<>();

    private List<String> skillsName = new ArrayList<>();


    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;
    private int SELECT_TYPE = 1;
    private String codeUrl;
    private String codeReverseUrl;
    private String certifyUrl;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;

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

        adapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info s) {
                baseViewHolder.setText(R.id.skill_text, s.getTitle());

                if (skillsName.toString().contains(s.getId())) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    if (skillsName.toString().contains(s.getId())) {
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
                        skillsName.remove(s.getId());
                    } else {
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView) v).setTextColor(Color.WHITE);
                        skillsName.add(s.getId());
                    }
                });
            }
        };


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
                inforNum.setText(len + "/200");
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

        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            PersonalRes.TechnicianInfo info = (PersonalRes.TechnicianInfo) getIntent().getSerializableExtra(INTENT_KEY);
            name.setText(info.getName());
            phone.setText(info.getMobile());
            personCode.setText(info.getCard());
            address.setText(info.getRegion());
            addressDetail.setText(info.getAddress());
            year.setText(info.getExperience());
            personNum.setText(info.getTeam_number() + "");

            skillsName.clear();

            List<String> types = Arrays.asList(info.getProject_type().split(","));
            for(String item: types){
                skillsName.add(item.trim());
            }

            information.setText(info.getEvaluate());
            inforNum.setText(info.getEvaluate().length() + " / 200");
            if (!TextUtils.isEmpty(info.getCard_front())) {
                codeUrl = info.getCard_front();
                Glide.with(this).load(codeUrl).into(code);
                codeDelete.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this).load(R.drawable.shangchuan_zheng).into(code);
                codeDelete.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(info.getCard_back())) {
                codeReverseUrl = info.getCard_back();
                Glide.with(this).load(codeReverseUrl).into(codeReverse);
                codeReverseDelete.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this).load(R.drawable.shangchuan_fan).into(codeReverse);
                codeDelete.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(info.getCertificate())) {
                certifyUrl = info.getCertificate();
                Glide.with(this).load(certifyUrl).into(certification);
                certiDelete.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this).load(R.drawable.shangchuan).into(certification);
                certiDelete.setVisibility(View.GONE);
            }

        }

        initImagePicker();

        addressLayout.setOnClickListener(v -> {
            if (picker != null){
                if (TextUtils.isEmpty(address.getText())){
                    picker.setSelectedItem("上海", "上海", "长宁");
                }else{
                    String[] addresss = address.getText().toString().split("-");
                    if (addresss.length == 3){
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                    }else if(addresss.length > 0){
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[1]);
                    }

                }
                picker.show();
            }
        });
        presenter.getAddressData(this);
        presenter.getTypeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

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


    private void goInAppAction() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(personCode.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(addressDetail.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(year.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(personNum.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(skillsName.toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(information.getText().toString())) {
            ToastUtil.show("不能为空");
            return;
        }

        FillSkillReq req = new FillSkillReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setName(name.getText().toString());
        req.setMobile(phone.getText().toString());
        req.setProvince(provinceId);
        req.setCity(cityId);
        req.setDistrict(districtId);
        req.setCard(personCode.getText().toString());
        req.setAddress(addressDetail.getText().toString());
        req.setExperience(year.getText().toString());
        req.setTeam_number(Integer.valueOf(personNum.getText().toString()));
        req.setEvaluate(information.getText().toString());
        String projectType = skillsName.toString();
        projectType = projectType.substring(1, projectType.length() - 1);
        req.setProject_type(projectType);
        if (!TextUtils.isEmpty(codeUrl) && !codeUrl.contains("http"))
            req.setCard_front(Utils.Bitmap2StrByBase64(codeUrl));
        if (!TextUtils.isEmpty(codeReverseUrl) && !codeReverseUrl.contains("http"))
            req.setCard_back(Utils.Bitmap2StrByBase64(codeReverseUrl));
        if (!TextUtils.isEmpty(certifyUrl) && !certifyUrl.contains("http"))
            req.setCertificate(Utils.Bitmap2StrByBase64(certifyUrl));

        presenter.fillSkillInfor(this, req);

    }

    @Override
    public void getData(LoginResBean bean) {
        finish();
        ToastUtil.show("完善完成");
    }

    @Override
    public void getData(TypeInfoRes bean) {
        for (TypeInfoRes.Type_Info typeInfo : bean.getType_info()){
            if (Integer.valueOf(typeInfo.getParent_id()) == 0){
                listData.add(typeInfo);
            }
        }
        adapter.addAll(listData);
    }

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            picker = new AddressPicker(this, result);
            picker.setHideProvince(false);
            picker.setHideCounty(false);
            picker.setColumnWeight(0.8f, 1.0f, 1.0f);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    if (county == null) {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        address.setText(province.getAreaName() + "-" + city.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName());
                    } else {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        districtId = Integer.valueOf(county.getAreaId());
                        address.setText(province.getAreaName() + "-"
                                + city.getAreaName() + "-"
                                + county.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                }
            });
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
