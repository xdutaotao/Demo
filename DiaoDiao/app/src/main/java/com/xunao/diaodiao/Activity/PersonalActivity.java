package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xunao.diaodiao.Present.PersonalPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.PersonalView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.CUSTOM_TYPE;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

public class PersonalActivity extends BaseActivity implements View.OnClickListener, PersonalView {
    public static final int REQUEST_CODE_SELECT = 8888;
    public static final int REQUEST_CODE = 6666;

    @Inject
    PersonalPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.edit_personal)
    TextView editPersonal;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.head_layout)
    RelativeLayout headLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.sex_layout)
    RelativeLayout sexLayout;
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

//        UserInfo userInfo = User.getInstance().getUserInfo();
//        Glide.with(this)
//                .load()
//                .placeholder(R.drawable.ic_launcher_round)
//                .bitmapTransform(new CropCircleTransformation(this))
//                .into(headIcon);


        editPersonal.setOnClickListener(this);

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

            case R.id.edit_personal:
                int type = ShareUtils.getValue(TYPE_KEY, 0);
                switch (type){
                    case COMPANY_TYPE:
                        EditCompanyActivity.startActivity(this);
                        break;

                    case SKILL_TYPE:
                        EditSkillActivity.startActivity(this);
                        break;

                    case CUSTOM_TYPE:
                        EditPersonalActivity.startActivity(this);
                        break;
                }

                break;
        }
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
                    presenter.changeHeadIcon(PersonalActivity.this, images.get(0).path);

                }
            }
        } else if (resultCode == RESULT_OK) {
            if (data != null && requestCode == REQUEST_CODE) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                EditPersonalActivity.startActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void getData(String s) {
        ToastUtil.show(s);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
