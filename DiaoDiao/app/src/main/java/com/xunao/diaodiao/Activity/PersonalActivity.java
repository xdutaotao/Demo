package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.HeadIconRes;
import com.xunao.diaodiao.Bean.PersonalRes;
import com.xunao.diaodiao.Present.PersonalPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.View.PersonalView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.xunao.diaodiao.Common.Constants.COMPANY_TYPE;
import static com.xunao.diaodiao.Common.Constants.CUSTOM_TYPE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_TYPE;
import static com.xunao.diaodiao.Common.Constants.STATUS;
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
    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.work_year)
    TextView workYear;
    @BindView(R.id.work_year_layout)
    RelativeLayout workYearLayout;
    @BindView(R.id.work_num)
    TextView workNum;
    @BindView(R.id.work_num_layout)
    RelativeLayout workNumLayout;
    @BindView(R.id.project)
    TextView project;
    @BindView(R.id.project_layout)
    RelativeLayout projectLayout;
    @BindView(R.id.work)
    TextView work;
    @BindView(R.id.work_layout)
    RelativeLayout workLayout;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.phone_layout)
    RelativeLayout phoneLayout;
    @BindView(R.id.hide_layout)
    LinearLayout hideLayout;


    private int type;
    private int status;
    private PersonalRes.CompanyInfo companyInfo;
    private PersonalRes.TechnicianInfo technicianInfo;
    private PersonalRes.FamilyInfo familyInfo;

    public static void startActivity(Context context, String path) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(INTENT_KEY, path);
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

        if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY))) {
            Glide.with(this)
                    .load(getIntent().getStringExtra(INTENT_KEY))
                    .placeholder(R.drawable.head_icon_boby)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(headIcon);
        }


        editPersonal.setOnClickListener(this);
        headLayout.setOnClickListener(this);
        presenter.getPersonalInfo(this);
        type = ShareUtils.getValue(TYPE_KEY, 0);
        switch (type) {
            case COMPANY_TYPE:
                nameText.setText("公司名称");
                hideLayout.setVisibility(View.GONE);
                break;

            case SKILL_TYPE:
                nameText.setText("姓名");
                break;

            case CUSTOM_TYPE:
                nameText.setText("用户名");
                hideLayout.setVisibility(View.GONE);
                break;
        }

        status = ShareUtils.getValue(STATUS, 0);
        if (status == 1){
            editPersonal.setText("审核通过");
        }else if (status == 2){
            editPersonal.setText("审核中");
        }else if (status == 3){
            editPersonal.setText("审核未通过");
        }else {
            editPersonal.setText("未完善资料");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout:
                showPicDialog();
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
    public void getData(HeadIconRes s) {
        Glide.with(this)
                .load(s.getHead_img())
                .placeholder(R.drawable.head_icon_boby)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(headIcon);

    }

    @Override
    public void getPersonalData(PersonalRes s) {

        switch (type) {
            case COMPANY_TYPE:
                companyInfo = s.getCompanyInfo();
                name.setText(companyInfo.getName());
                phone.setText(companyInfo.getTel());
                address.setText(companyInfo.getAddress());
                addressDetail.setText(companyInfo.getCity() + companyInfo.getDistrict() + companyInfo.getProvince() + "");

                break;

            case SKILL_TYPE:
                technicianInfo = s.getTechnicianInfo();
                name.setText(technicianInfo.getName());
                phone.setText(technicianInfo.getMobile());
                address.setText(technicianInfo.getAddress());
                addressDetail.setText(technicianInfo.getCity() + technicianInfo.getDistrict() + technicianInfo.getProvince() + "");
                workYear.setText(technicianInfo.getExperience());
                workNum.setText(technicianInfo.getTeam_number()+"");
                project.setText(technicianInfo.getProject_type());
                work.setText(technicianInfo.getExperience());
                break;

            case CUSTOM_TYPE:
                familyInfo = s.getFamilyInfo();
                name.setText(familyInfo.getName());
                phone.setText(familyInfo.getMobile());
                address.setText(familyInfo.getAddress());
                addressDetail.setText(familyInfo.getCity() + familyInfo.getDistrict() + familyInfo.getProvince() + "");

                break;
        }
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
                switch (type) {
                    case COMPANY_TYPE:
                        EditCompanyActivity.startActivity(this, companyInfo);
                        break;

                    case SKILL_TYPE:
                        EditSkillActivity.startActivity(this, technicianInfo);
                        break;

                    case CUSTOM_TYPE:
                        EditPersonalActivity.startActivity(this, familyInfo);
                        break;
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFailure() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
