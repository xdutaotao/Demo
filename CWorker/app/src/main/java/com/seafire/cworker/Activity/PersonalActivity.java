package com.seafire.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seafire.cworker.Model.User;
import com.seafire.cworker.Model.UserInfo;
import com.seafire.cworker.Present.PersonalPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Utils.Utils;
import com.seafire.cworker.View.PersonalView;
import com.seafire.cworker.Widget.GlideImageLoader;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.chatting.utils.HandleResponseCode;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.seafire.cworker.Common.Constants.INTENT_KEY;

public class PersonalActivity extends BaseActivity implements View.OnClickListener, PersonalView {
    public static final int REQUEST_CODE_SELECT = 8888;
    public static final int REQUEST_CODE = 6666;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.head_layout)
    RelativeLayout headLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.level)
    TextView level;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.project)
    TextView project;
    @BindView(R.id.photo)
    TextView photo;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.sex_layout)
    RelativeLayout sexLayout;

    @Inject
    PersonalPresenter presenter;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonalActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        showToolbarBack(toolBar, titleText, "个人资料");
        presenter.attachView(this);

        UserInfo userInfo = User.getInstance().getUserInfo();
        Glide.with(this)
                .load(User.getInstance().getUserInfo().getPerson().getFace())
                .placeholder(R.drawable.my_head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headIcon);
        name.setText(userInfo.getPerson().getName());
        sex.setText(userInfo.getPerson().getSex() == 1 ? "男" : "女");
        level.setText("LV" + userInfo.getPerson().getExperience() / 3500);
        createTime.setText(Utils.getStrTime(userInfo.getPerson().getDateline() + ""));
        if (userInfo.getProject() != null)
            project.setText(userInfo.getProject().getName());
        photo.setText(userInfo.getPerson().getMobile());
        email.setText(userInfo.getPerson().getEmail());
        address.setText(userInfo.getPerson().getAddress());
        addressLayout.setOnClickListener(v -> {
            AddressActivity.startActivityForResult(PersonalActivity.this, userInfo.getPerson().getAddress());
        });

        headLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        initPicker();
    }

    private void initPicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setFocusWidth(600);
        imagePicker.setFocusHeight(600);
        imagePicker.setShowCamera(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout:
                showPicDialog();
                break;

            case R.id.sex_layout:
                selectSex();
                break;
        }
    }

    private void selectSex() {
        new IOSDialog(this).builder()
                .setCancelable(true)
                .setTitle("男", v -> {
                    sex.setText("男");
                    presenter.changeSex(this, 1);
                })
                .setMsg("女", v -> {
                    sex.setText("女");
                    presenter.changeSex(this, 0);
                })
                .setMsgSize(R.dimen.dialog_msg_size)
                .setMsgColor("#333333")
                .setNegativeButton("取消", null)
                .show();
    }

    private void showPicDialog() {
        new IOSDialog(this).builder()
                .setCancelable(true)
                .setTitle("拍照", v -> {
                    takePhoto();
                })
                .setMsg("相册", v -> {
                    selectPhoto();
                })
                .setMsgSize(R.dimen.dialog_msg_size)
                .setMsgColor("#333333")
                .setNegativeButton("取消", null)
                .show();
    }

    private void takePhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {

                    //JMessage
                    JMessageClient.updateUserAvatar(new File(images.get(0).path), new BasicCallback() {
                        @Override
                        public void gotResult(final int status, final String desc) {

                            if (status == 0) {

                                presenter.changeHeadIcon(PersonalActivity.this, images.get(0).path);
                                //如果头像上传失败，删除剪裁后的文件
                            }else {
                                HandleResponseCode.onHandle(PersonalActivity.this, status, false);
                                File file = new File(images.get(0).path);
                                file.delete();


                            }
                        }
                    });
                }
            }
        }else if (resultCode == RESULT_OK){
            if (data != null && requestCode == REQUEST_CODE){
                address.setText(data.getStringExtra(INTENT_KEY));
            }
        }
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void getData(String s) {
        ToastUtil.show(s);
        Glide.with(this)
                .load(User.getInstance().getUserInfo().getPerson().getFace())
                .placeholder(R.drawable.ic_launcher_round)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headIcon);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
