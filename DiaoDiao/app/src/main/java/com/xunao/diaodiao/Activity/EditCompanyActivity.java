package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xunao.diaodiao.Bean.CheckFinishRes;
import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.LoginResBean;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.EditCompanyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.EditCompanyView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class EditCompanyActivity extends BaseActivity implements EditCompanyView, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @Inject
    EditCompanyPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.check_box_all)
    CheckBox checkBoxAll;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.check_box_single)
    CheckBox checkBoxSingle;
    @BindView(R.id.first_pic)
    TextView firstPic;
    @BindView(R.id.second_pic)
    TextView secondPic;
    @BindView(R.id.third_pic)
    TextView thirdPic;
    @BindView(R.id.one_pic)
    TextView onePic;

    private static final int IMAGE_PICKER = 8888;
    @BindView(R.id.first_iv)
    ImageView firstIv;
    @BindView(R.id.second_iv)
    ImageView secondIv;
    @BindView(R.id.third_iv)
    ImageView thirdIv;
    @BindView(R.id.one_iv)
    ImageView oneIv;
    @BindView(R.id.all_layout)
    LinearLayout allLayout;
    @BindView(R.id.one_layout)
    RelativeLayout oneLayout;
    @BindView(R.id.first_delete)
    ImageView firstDelete;
    @BindView(R.id.second_delete)
    ImageView secondDelete;
    @BindView(R.id.third_delete)
    ImageView thirdDelete;
    @BindView(R.id.one_delete)
    ImageView oneDelete;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.code_delete)
    ImageView codeDelete;
    @BindView(R.id.code_reverse)
    ImageView codeReverse;
    @BindView(R.id.code_reverse_delete)
    ImageView codeReverseDelete;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.contact_name)
    EditText contactName;
    @BindView(R.id.contact_code)
    EditText contactCode;
    @BindView(R.id.contact_phone)
    EditText contactPhone;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;
    @BindView(R.id.build_time)
    TextView buildTime;
    @BindView(R.id.build_time_layout)
    RelativeLayout buildTimeLayout;
    private int SELECT_TYPE = 1;
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private String firstUrl;
    private String secondUrl;
    private String thirdUrl;
    private String oneUrl;
    private String codeUrl;
    private String codeReverseUrl;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;
    private StringBuilder timeLong;

    FillCompanyReq req = new FillCompanyReq();
    private boolean canChange = false;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditCompanyActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, PersonalRes.CompanyInfo companyInfo) {
        Intent intent = new Intent(context, EditCompanyActivity.class);
        intent.putExtra(INTENT_KEY, companyInfo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "完善资料");

        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            PersonalRes.CompanyInfo info = (PersonalRes.CompanyInfo) getIntent().getSerializableExtra(INTENT_KEY);
            name.setText(info.getName());
            contactPhone.setText(info.getContact_mobile());
            contactCode.setText(info.getContact_card());
            address.setText(info.getRegion());
            addressDetail.setText(info.getAddress());
            contactName.setText(info.getContact());

            if(!TextUtils.isEmpty(contactName.getText())){
                contactName.setClickable(false);
                contactName.setFocusable(false);
                contactName.setFocusableInTouchMode(false);
            }


            phone.setText(info.getTel());
            buildTime.setText(Utils.millToYearString(info.getEstablish_time()));
            provinceId = info.getProvince();
            cityId = info.getCity();
            districtId = info.getDistrict();

            codeUrl = info.getCard_front();
            if (!TextUtils.isEmpty(codeUrl)) {
                Glide.with(this).load(info.getCard_front()).placeholder(R.drawable.shangchuan_zheng).into(code);
                codeDelete.setVisibility(View.VISIBLE);
            } else {
                codeDelete.setVisibility(View.GONE);
            }

            codeReverseUrl = info.getCard_back();
            if (!TextUtils.isEmpty(codeReverseUrl)) {
                Glide.with(this).load(info.getCard_front()).placeholder(R.drawable.shangchuan_fan).into(codeReverse);
                codeReverseDelete.setVisibility(View.VISIBLE);
            } else {
                codeReverseDelete.setVisibility(View.GONE);
            }


            if (info.getPictures() != null && info.getPictures().size() == 3) {
                oneLayout.setVisibility(View.GONE);
                allLayout.setVisibility(View.VISIBLE);
                checkBoxAll.setChecked(false);
                checkBoxSingle.setChecked(true);

                firstUrl = info.getPictures().get(0);
                secondUrl = info.getPictures().get(1);
                thirdUrl = info.getPictures().get(2);
                if (!TextUtils.isEmpty(firstUrl)) {
                    Glide.with(this).load(firstUrl).into(firstIv);
                    firstDelete.setVisibility(View.VISIBLE);
                    firstPic.setVisibility(View.GONE);
                    firstIv.setVisibility(View.VISIBLE);
                } else {
                    firstPic.setVisibility(View.VISIBLE);
                    firstDelete.setVisibility(View.GONE);
                    firstIv.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(secondUrl)) {
                    Glide.with(this).load(secondUrl).into(secondIv);
                    secondIv.setVisibility(View.VISIBLE);
                    secondDelete.setVisibility(View.VISIBLE);
                    secondPic.setVisibility(View.GONE);
                } else {
                    secondIv.setVisibility(View.GONE);
                    secondDelete.setVisibility(View.GONE);
                    secondPic.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(thirdUrl)) {
                    Glide.with(this).load(thirdUrl).into(thirdIv);
                    thirdIv.setVisibility(View.VISIBLE);
                    thirdPic.setVisibility(View.GONE);
                    thirdDelete.setVisibility(View.VISIBLE);
                } else {
                    thirdIv.setVisibility(View.GONE);
                    thirdPic.setVisibility(View.VISIBLE);
                    thirdDelete.setVisibility(View.GONE);
                }


            } else {
                oneLayout.setVisibility(View.VISIBLE);
                allLayout.setVisibility(View.GONE);
                if(info.getPictures() != null){
                    oneUrl = info.getPictures().get(0);
                }

                if (!TextUtils.isEmpty(oneUrl)) {
                    Glide.with(this).load(oneUrl).into(oneIv);
                    onePic.setVisibility(View.GONE);
                    oneIv.setVisibility(View.VISIBLE);
                    oneDelete.setVisibility(View.VISIBLE);
                } else {
                    onePic.setVisibility(View.VISIBLE);
                    oneIv.setVisibility(View.GONE);
                    oneDelete.setVisibility(View.GONE);
                }

                checkBoxAll.setChecked(true);
                checkBoxSingle.setChecked(false);
                SELECT_TYPE = 0;
            }

        } else {
            allLayout.setVisibility(View.VISIBLE);
            oneLayout.setVisibility(View.GONE);

            codeDelete.setVisibility(View.GONE);
            codeReverseDelete.setVisibility(View.GONE);
            checkBoxSingle.setChecked(true);
        }

        firstPic.setOnClickListener(this);
        secondPic.setOnClickListener(this);
        thirdPic.setOnClickListener(this);
        onePic.setOnClickListener(this);

        firstDelete.setOnClickListener(this);
        secondDelete.setOnClickListener(this);
        thirdDelete.setOnClickListener(this);
        oneDelete.setOnClickListener(this);


        code.setOnClickListener(this);
        codeReverse.setOnClickListener(this);
        codeDelete.setOnClickListener(this);
        codeReverseDelete.setOnClickListener(this);

        login.setOnClickListener(this);
        initImagePicker();

        addressLayout.setOnClickListener(v -> {
            if (picker != null) {
                if (TextUtils.isEmpty(address.getText())) {
                    picker.setSelectedItem("上海", "上海", "长宁");
                } else {
                    String[] addresss = address.getText().toString().split("-");
                    if (addresss.length == 3) {
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                    } else if (addresss.length > 0) {
                        picker.setSelectedItem(addresss[0], addresss[1], addresss[1]);
                    }

                }
                picker.show();
            }
        });

        buildTimeLayout.setOnClickListener(v -> {
            DatePicker datePicker = new DatePicker(this);

            datePicker.setRangeStart(1900, 1, 1);
            String[] dates = Utils.getNowDate().split("-");
            datePicker.setRangeEnd(Integer.valueOf(dates[0]),
                    Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));

            timeLong = new StringBuilder();
            datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    //ToastUtil.show(year+month+day);
                    timeLong.append(year + "-")
                            .append(month + "-")
                            .append(day);
                    buildTime.setText(timeLong.toString());

                }
            });

            datePicker.setOnWheelListener(new DatePicker.OnWheelListener() {
                @Override
                public void onYearWheeled(int index, String year) {
                    datePicker.setTitleText(year + "-" + datePicker.getSelectedMonth() + "-" + datePicker.getSelectedDay());
                }

                @Override
                public void onMonthWheeled(int index, String month) {
                    datePicker.setTitleText(datePicker.getSelectedYear() + "-" + month + "-" + datePicker.getSelectedDay());
                }

                @Override
                public void onDayWheeled(int index, String day) {
                    datePicker.setTitleText(datePicker.getSelectedYear() + "-" + datePicker.getSelectedMonth() + "-" + day);
                }
            });
            datePicker.show();


        });
        if(Constants.addressResult.size() == 0)
            presenter.getAddressData(this);
        else
            getAddressData(Constants.addressResult);

        oneIv.setOnClickListener(this);
        firstIv.setOnClickListener(this);
        secondIv.setOnClickListener(this);
        thirdIv.setOnClickListener(this);
        code.setOnClickListener(this);
        codeReverse.setOnClickListener(this);

        presenter.checkFinish();
    }

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            if(Constants.addressResult.size() == 0)
                Constants.addressResult.addAll(result);
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

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_pic:
                SELECT_TYPE = 1;
                selectPhoto();
                break;

            case R.id.second_pic:
                SELECT_TYPE = 2;
                selectPhoto();
                break;

            case R.id.third_pic:
                SELECT_TYPE = 3;
                selectPhoto();
                break;

            case R.id.one_pic:
                SELECT_TYPE = 0;
                selectPhoto();
                break;

            case R.id.first_delete:
                if(canChange){
                    firstDelete.setVisibility(View.GONE);
                    firstIv.setVisibility(View.GONE);
                    firstPic.setVisibility(View.VISIBLE);
                    firstUrl="";
                }

                break;

            case R.id.second_delete:
                if(canChange){
                    secondDelete.setVisibility(View.GONE);
                    secondIv.setVisibility(View.GONE);
                    secondPic.setVisibility(View.VISIBLE);
                    secondUrl="";
                }

                break;

            case R.id.third_delete:
                if(canChange){
                    thirdDelete.setVisibility(View.GONE);
                    thirdIv.setVisibility(View.GONE);
                    thirdPic.setVisibility(View.VISIBLE);
                    thirdUrl="";
                }

                break;

            case R.id.one_delete:
                if(canChange){
                    oneDelete.setVisibility(View.GONE);
                    oneIv.setVisibility(View.GONE);
                    onePic.setVisibility(View.VISIBLE);
                    oneUrl="";
                }

                break;

            case R.id.code_delete:
                if(canChange){
                    Glide.with(this).load(R.drawable.shangchuan_zheng).into(code);
                    codeDelete.setVisibility(View.GONE);
                    codeUrl="";
                }

                break;

            case R.id.code_reverse_delete:
                if(canChange){
                    Glide.with(this).load(R.drawable.shangchuan_fan).into(codeReverse);
                    codeReverseDelete.setVisibility(View.GONE);
                    codeReverseUrl="";
                }

                break;

            case R.id.code:
                if (codeDelete.getVisibility() == View.GONE) {
                    //getPicPath();
                    selectPhoto();
                    SELECT_TYPE = 4;
                }else{
                    if(!TextUtils.isEmpty(codeUrl))
                    PhotoActivity.startActivity(this, codeUrl, codeUrl.contains("http"));
                }

                break;

            case R.id.code_reverse:
                if (codeReverseDelete.getVisibility() == View.GONE) {
                    //getPicPath();
                    selectPhoto();
                    SELECT_TYPE = 5;
                }else{
                    if(!TextUtils.isEmpty(codeUrl))
                    PhotoActivity.startActivity(this, codeReverseUrl, codeReverseUrl.contains("http"));
                }

                break;

            case R.id.login:
                postAction();
                break;

            case R.id.one_iv:
                if(!TextUtils.isEmpty(oneUrl))
                PhotoActivity.startActivity(this, oneUrl, oneUrl.contains("http"));
                break;

            case R.id.first_iv:
                if(!TextUtils.isEmpty(firstUrl))
                PhotoActivity.startActivity(this, firstUrl, firstUrl.contains("http"));
                break;

            case R.id.second_iv:
                if(!TextUtils.isEmpty(secondUrl))
                PhotoActivity.startActivity(this, secondUrl, secondUrl.contains("http"));
                break;

            case R.id.third_iv:
                if(!TextUtils.isEmpty(thirdUrl))
                PhotoActivity.startActivity(this, thirdUrl, thirdUrl.contains("http"));
                break;
        }


    }

    private void postAction() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.show("公司名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())) {
            ToastUtil.show("公司地区不能为空");
            return;
        }


        if (TextUtils.isEmpty(addressDetail.getText().toString())) {
            ToastUtil.show("公司地址不能为空");
            return;
        }


        if (TextUtils.isEmpty(phone.getText().toString())) {
            ToastUtil.show("公司电话不能为空");
            return;
        }

        if(!Utils.isPhone(phone.getText().toString())){
            ToastUtil.show("手机号输入错误");
            return;
        }

        if (TextUtils.isEmpty(buildTime.getText().toString())) {
            ToastUtil.show("成立时间不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactName.getText().toString())) {
            ToastUtil.show("联系人不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactCode.getText().toString())) {
            ToastUtil.show("联系人身份证号码不能为空");
            return;
        }

        if(!Utils.isIDCardValidate(contactCode.getText().toString())){
            ToastUtil.show("身份证输入错误");
            return;
        }

//        String codeStr = contactCode.getText().toString();
//        if (contactCode.getText().toString().length() != 18) {
//            ToastUtil.show("联系人身份证号码不合法");
//            return;
//        }
//
//        String str = contactCode.getText().toString().substring(0, 17);
//        try{
//            long num = Long.valueOf(str);
//        }catch (Exception e){
//            ToastUtil.show("联系人身份证号码不合法");
//            return;
//        }
//
//        try{
//            long num = Long.valueOf(codeStr.substring(17, 18));
//        }catch (Exception e){
//            if(!TextUtils.equals("x", codeStr.substring(17, 18))
//                    && !TextUtils.equals("X", codeStr.substring(17, 18))){
//                ToastUtil.show("联系人身份证号码不合法");
//                return;
//            }
//
//        }


        if (TextUtils.isEmpty(contactPhone.getText().toString())) {
            ToastUtil.show("联系人电话不能为空");
            return;
        }

        if (TextUtils.isEmpty(codeUrl)) {
            ToastUtil.show("身份证正面不能为空");
            return;
        }

        if (TextUtils.isEmpty(codeReverseUrl)) {
            ToastUtil.show("身份证反面不能为空");
            return;
        }

        if (SELECT_TYPE == 0) {
            if (TextUtils.isEmpty(oneUrl)) {
                ToastUtil.show("公司认证不能为空");
                return;
            }
        }

        if (SELECT_TYPE == 3) {
            if (TextUtils.isEmpty(firstUrl) || TextUtils.isEmpty(secondUrl)
                    || TextUtils.isEmpty(thirdUrl)) {
                ToastUtil.show("公司认证不能为空");
                return;
            }
        }


        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setName(name.getText().toString());
        req.setProvince(provinceId);
        req.setCity(cityId);
        req.setDistrict(districtId);
        req.setAddress(addressDetail.getText().toString());
        req.setTel(phone.getText().toString());
        req.setContact(contactName.getText().toString());
        req.setContact_mobile(contactPhone.getText().toString());
        req.setContact_card(contactCode.getText().toString());
        req.setEstablish_time(Utils.convert2long(buildTime.getText().toString()));

        if (!TextUtils.isEmpty(codeUrl) && !codeUrl.contains("http")){
            req.setCard_front(Utils.Bitmap2StrByBase64(codeUrl));
        }

        if (!TextUtils.isEmpty(codeReverseUrl) && !codeReverseUrl.contains("http")){
            req.setCard_back(Utils.Bitmap2StrByBase64(codeReverseUrl));
        }

        req.setEstablish_time(Utils.convert2long(buildTime.getText().toString()));
        List<String> remarkList = new ArrayList<>();
        if (oneLayout.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(oneUrl) && !oneUrl.contains("http")){
                remarkList.add(Utils.Bitmap2StrByBase64(oneUrl));
            }

        } else {
            if (!TextUtils.isEmpty(firstUrl) && !firstUrl.contains("http")){
                remarkList.add(Utils.Bitmap2StrByBase64(firstUrl));
            }
            if (!TextUtils.isEmpty(secondUrl) && !secondUrl.contains("http")){
                remarkList.add(Utils.Bitmap2StrByBase64(secondUrl));
            }
            if (!TextUtils.isEmpty(thirdUrl) && !thirdUrl.contains("http")){
                remarkList.add(Utils.Bitmap2StrByBase64(thirdUrl));
            }

        }

        req.setAuthentication(remarkList);
        req.setYears(System.currentTimeMillis()/1000);
        presenter.fillInfor(this, req);
    }

    @Override
    public void getData(LoginResBean bean) {
        ToastUtil.show("完善成功");
        MainActivity.startActivity(this);
    }

    @Override
    public void getData(CheckFinishRes bean) {
        canChange = bean.getStatus() == 1 ? false : true;
        if(canChange){
            checkBoxAll.setOnCheckedChangeListener(this);
            checkBoxSingle.setOnCheckedChangeListener(this);
        }else{
            checkBoxAll.setChecked(checkBoxAll.isChecked());
            checkBoxSingle.setChecked(checkBoxSingle.isChecked());
            checkBoxSingle.setClickable(false);
            checkBoxAll.setClickable(false);
            contactCode.setFocusable(false);
        }
    }

    private void getPicPath() {
        new IOSDialog(this).builder()
                .setCancelable(true)
                .setTitle("拍照", v -> {
                    selectCamera();
                })
                .setMsg("相册", v -> {
                    selectPhoto();
                })
                .setMsgSize(R.dimen.dialog_msg_size)
                .setMsgColor("#333333")
                .setNegativeButton("取消", null)
                .show();
    }

    private void selectCamera() {
        CameraActivity.startActivityForResult(this, imageItems.size());
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, IMAGE_PICKER);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_box_all:
                checkBoxAll.setChecked(isChecked);
                checkBoxSingle.setChecked(!isChecked);
                allLayout.setVisibility(View.GONE);
                oneLayout.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(firstUrl)) {
                    firstPic.setVisibility(View.VISIBLE);
                    firstIv.setVisibility(View.GONE);
                    firstDelete.setVisibility(View.GONE);
                } else {
                    firstPic.setVisibility(View.GONE);
                    firstIv.setVisibility(View.VISIBLE);
                    firstDelete.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(secondUrl)) {
                    secondPic.setVisibility(View.VISIBLE);
                    secondIv.setVisibility(View.GONE);
                    secondDelete.setVisibility(View.GONE);
                } else {
                    secondPic.setVisibility(View.GONE);
                    secondIv.setVisibility(View.VISIBLE);
                    secondDelete.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(thirdUrl)) {
                    thirdPic.setVisibility(View.VISIBLE);
                    thirdIv.setVisibility(View.GONE);
                    thirdDelete.setVisibility(View.GONE);
                } else {
                    thirdPic.setVisibility(View.GONE);
                    thirdIv.setVisibility(View.VISIBLE);
                    thirdDelete.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.check_box_single:
                checkBoxAll.setChecked(!isChecked);
                checkBoxSingle.setChecked(isChecked);
                allLayout.setVisibility(View.VISIBLE);
                oneLayout.setVisibility(View.GONE);

                if (TextUtils.isEmpty(oneUrl)) {
                    onePic.setVisibility(View.VISIBLE);
                    oneIv.setVisibility(View.GONE);
                    oneDelete.setVisibility(View.GONE);
                } else {
                    onePic.setVisibility(View.GONE);
                    oneIv.setVisibility(View.VISIBLE);
                    oneDelete.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setImagePath(String path) {
        switch (SELECT_TYPE) {
            case 0:
                Glide.with(this).load(path).into(oneIv);
                oneLayout.setVisibility(View.VISIBLE);
                allLayout.setVisibility(View.GONE);
                onePic.setVisibility(View.GONE);
                oneIv.setVisibility(View.VISIBLE);
                oneDelete.setVisibility(View.VISIBLE);
                oneUrl = path;
                break;

            case 1:
                Glide.with(this).load(path).into(firstIv);
                oneLayout.setVisibility(View.GONE);
                allLayout.setVisibility(View.VISIBLE);
                firstPic.setVisibility(View.GONE);
                firstIv.setVisibility(View.VISIBLE);
                firstDelete.setVisibility(View.VISIBLE);
                firstUrl = path;
                break;

            case 2:
                Glide.with(this).load(path).into(secondIv);
                oneLayout.setVisibility(View.GONE);
                allLayout.setVisibility(View.VISIBLE);
                secondIv.setVisibility(View.VISIBLE);
                secondPic.setVisibility(View.GONE);
                secondDelete.setVisibility(View.VISIBLE);
                secondUrl = path;
                break;

            case 3:
                Glide.with(this).load(path).into(thirdIv);
                oneLayout.setVisibility(View.GONE);
                allLayout.setVisibility(View.VISIBLE);
                thirdIv.setVisibility(View.VISIBLE);
                thirdPic.setVisibility(View.GONE);
                thirdDelete.setVisibility(View.VISIBLE);
                thirdUrl = path;
                break;

            case 4:
                Glide.with(this).load(path).placeholder(getResources().getDrawable(R.drawable.shangchuan_zheng)).into(code);
                codeDelete.setVisibility(View.VISIBLE);
                codeUrl = path;
                req.setCard_front(Utils.Bitmap2StrByBase64(codeUrl));
                break;

            case 5:
                Glide.with(this).load(path).placeholder(getResources().getDrawable(R.drawable.shangchuan_fan)).into(codeReverse);
                codeReverseDelete.setVisibility(View.VISIBLE);
                codeReverseUrl = path;
                req.setCard_back(Utils.Bitmap2StrByBase64(codeReverseUrl));
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
