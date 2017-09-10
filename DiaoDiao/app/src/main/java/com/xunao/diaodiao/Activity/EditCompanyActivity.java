package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.xunao.diaodiao.Bean.FillCompanyReq;
import com.xunao.diaodiao.Bean.LoginResBean;
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
    EditText address;
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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditCompanyActivity.class);
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

        checkBoxAll.setOnCheckedChangeListener(this);
        checkBoxSingle.setOnCheckedChangeListener(this);

        initImagePicker();

        firstPic.setOnClickListener(this);
        secondPic.setOnClickListener(this);
        thirdPic.setOnClickListener(this);
        onePic.setOnClickListener(this);

        firstDelete.setOnClickListener(this);
        secondDelete.setOnClickListener(this);
        thirdDelete.setOnClickListener(this);
        oneDelete.setOnClickListener(this);

        allLayout.setVisibility(View.VISIBLE);
        oneLayout.setVisibility(View.GONE);

        codeDelete.setVisibility(View.GONE);
        codeReverseDelete.setVisibility(View.GONE);
        code.setOnClickListener(this);
        codeReverse.setOnClickListener(this);
        codeDelete.setOnClickListener(this);
        codeReverseDelete.setOnClickListener(this);

        login.setOnClickListener(this);
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
                firstDelete.setVisibility(View.GONE);
                firstIv.setVisibility(View.GONE);
                firstPic.setVisibility(View.VISIBLE);
                break;

            case R.id.second_delete:
                secondDelete.setVisibility(View.GONE);
                secondIv.setVisibility(View.GONE);
                secondPic.setVisibility(View.VISIBLE);
                break;

            case R.id.third_delete:
                thirdDelete.setVisibility(View.GONE);
                thirdIv.setVisibility(View.GONE);
                thirdPic.setVisibility(View.VISIBLE);
                break;

            case R.id.one_delete:
                oneDelete.setVisibility(View.GONE);
                oneIv.setVisibility(View.GONE);
                onePic.setVisibility(View.VISIBLE);
                break;

            case R.id.code_delete:
                Glide.with(this).load(R.drawable.shangchuan_zheng).into(code);
                codeDelete.setVisibility(View.GONE);
                break;

            case R.id.code_reverse_delete:
                Glide.with(this).load(R.drawable.shangchuan_fan).into(codeReverse);
                codeReverseDelete.setVisibility(View.GONE);
                break;

            case R.id.code:
                if (codeDelete.getVisibility() == View.GONE) {
                    selectPhoto();
                    SELECT_TYPE = 4;
                }

                break;

            case R.id.code_reverse:
                if (codeReverseDelete.getVisibility() == View.GONE) {
                    selectPhoto();
                    SELECT_TYPE = 5;
                }

                break;

            case R.id.login:
                postAction();
                break;
        }


    }

    private void postAction() {
        if (TextUtils.isEmpty(name.getText().toString())){
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


        if (TextUtils.isEmpty(phone.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactName.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactCode.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(contactPhone.getText().toString())){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(codeUrl)){
            ToastUtil.show("不能为空");
            return;
        }

        if (TextUtils.isEmpty(codeReverseUrl)){
            ToastUtil.show("不能为空");
            return;
        }

        if (SELECT_TYPE == 0){
            if (TextUtils.isEmpty(oneUrl)){
                ToastUtil.show("不能为空");
                return;
            }
        }

        if (SELECT_TYPE <= 3){
            if (TextUtils.isEmpty(firstUrl) || TextUtils.isEmpty(secondUrl)
                    || TextUtils.isEmpty(thirdUrl)){
                ToastUtil.show("不能为空");
                return;
            }
        }

        FillCompanyReq req = new FillCompanyReq();
        req.setUserid(Integer.valueOf(User.getInstance().getUserId()));
        req.setName(name.getText().toString());
        req.setProvince(1);
        req.setCity(2);
        req.setDistrict(3);
        req.setAddress(address.getText().toString());
        req.setTel(phone.getText().toString());
        req.setContact(contactName.getText().toString());
        req.setContact_mobile(contactPhone.getText().toString());
        req.setContact_card(contactCode.getText().toString());
        req.setCard_front(Utils.Bitmap2StrByBase64(codeUrl));
        req.setCard_back(Utils.Bitmap2StrByBase64(codeReverseUrl));
        List<String> remarkList = new ArrayList<>();
        if (oneLayout.getVisibility() == View.VISIBLE){
            remarkList.add(Utils.Bitmap2StrByBase64(oneUrl));
        }else{
            remarkList.add(Utils.Bitmap2StrByBase64(firstUrl));
            remarkList.add(Utils.Bitmap2StrByBase64(secondUrl));
            remarkList.add(Utils.Bitmap2StrByBase64(thirdUrl));
        }

        req.setAuthentication(remarkList);
        presenter.fillInfor(this, req);
    }

    @Override
    public void getData(LoginResBean bean) {
        ToastUtil.show("完善成功");
        finish();
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
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }



}
