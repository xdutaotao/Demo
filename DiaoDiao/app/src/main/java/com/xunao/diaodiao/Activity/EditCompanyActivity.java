package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xunao.diaodiao.Present.EditCompanyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.EditCompanyView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;

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
    private int SELECT_TYPE = 0;

    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();


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
        firstPic.setOnClickListener(this);
        secondPic.setOnClickListener(this);
        thirdPic.setOnClickListener(this);
        onePic.setOnClickListener(this);

        checkBoxSingle.setChecked(true);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_pic:
                SELECT_TYPE = 1;
                break;

            case R.id.second_pic:
                SELECT_TYPE = 2;
                break;

            case R.id.third_pic:
                SELECT_TYPE = 3;
                break;

            case R.id.one_pic:
                SELECT_TYPE = 0;
                break;
        }

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
                firstPic.setVisibility(View.VISIBLE);
                secondPic.setVisibility(View.VISIBLE);
                thirdPic.setVisibility(View.VISIBLE);
                onePic.setVisibility(View.GONE);
                oneIv.setVisibility(View.GONE);
                firstIv.setVisibility(View.VISIBLE);
                secondIv.setVisibility(View.VISIBLE);
                thirdIv.setVisibility(View.VISIBLE);
                break;

            case R.id.check_box_single:
                checkBoxAll.setChecked(!isChecked);
                checkBoxSingle.setChecked(isChecked);
                firstPic.setVisibility(View.GONE);
                secondPic.setVisibility(View.GONE);
                thirdPic.setVisibility(View.GONE);
                onePic.setVisibility(View.VISIBLE);
                oneIv.setVisibility(View.VISIBLE);
                firstIv.setVisibility(View.GONE);
                secondIv.setVisibility(View.GONE);
                thirdIv.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                //scanPhotoPath(images.get(0).path);
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
