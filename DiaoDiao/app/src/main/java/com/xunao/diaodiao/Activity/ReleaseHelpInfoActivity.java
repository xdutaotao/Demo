package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseHelpInfoPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseHelpInfoView;
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
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ReleaseHelpInfoActivity extends BaseActivity implements ReleaseHelpInfoView, View.OnClickListener {

    @Inject
    ReleaseHelpInfoPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.next)
    Button next;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private RecyclerArrayAdapter<String> adapter;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;

    private ReleaseSkillReq req;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseHelpInfoActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ReleaseSkillReq req) {
        Intent intent = new Intent(context, ReleaseSkillActivity.class);
        intent.putExtra(INTENT_KEY, req);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_help_info);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布互助信息");

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 200) {
                    content.setText(s.subSequence(0, 200));
                } else {
                    contentNum.setText(content.getText().length() + " / 200");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)) {
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.image, R.drawable.icon_paishe);
                } else {
                    baseViewHolder.setVisible(R.id.delete, req!=null ? false: true);
                    baseViewHolder.setImageUrl(R.id.image, s);
                }
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            view.findViewById(R.id.delete).setOnClickListener(v -> {
                imageItems.remove(i);
                adapter.remove(i);
                if (!adapter.getAllData().contains(ADD)) {
                    adapter.add(ADD);
                }
            });

            if (TextUtils.equals(adapter.getAllData().get(i), ADD)) {
                selectPhoto();
            }else{
                if(pathList.size() > 0)
                    PhotoActivity.startActivity(this, pathList.get(i), pathList.get(i).contains("http"));
            }

        });
        adapter.add(ADD);
        recyclerView.setAdapter(adapter);

        initImagePicker();

        if(Constants.addressResult.size() == 0){
            presenter.getAddressData(this);
        }else{
            getAddressData(Constants.addressResult);
        }

        addressLayout.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.address_layout:
                if (picker != null){
                    if (TextUtils.isEmpty(address.getText())){
                        picker.setSelectedItem("上海", "上海", "长宁");
                    }else{
                        String[] addresss = address.getText().toString().split("-");
                        if (addresss.length == 3){
                            picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                        }else{
                            picker.setSelectedItem(addresss[0], addresss[1], addresss[1]);
                        }

                    }
                    picker.show();
                }
                break;

            case R.id.next:
                if(Utils.isFastDoubleClick()){
                    return;
                }

                if (TextUtils.isEmpty(title.getText())) {
                    ToastUtil.show("标题不能为空");
                    return;
                }

                if (TextUtils.isEmpty(contact.getText())) {
                    ToastUtil.show("联系人不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone.getText())) {
                    ToastUtil.show("联系人电话不能为空");
                    return;
                }

                if(!Utils.isPhone(phone.getText().toString())){
                    ToastUtil.show("手机号输入错误");
                    return;
                }

                if (TextUtils.isEmpty(address.getText())){
                    ToastUtil.show("选择地区");
                    return;
                }

                if (TextUtils.isEmpty(addressDetail.getText())) {
                    ToastUtil.show("地址不能为空");
                    return;
                }

                if (TextUtils.isEmpty(content.getText())) {
                    ToastUtil.show("描述不能为空");
                    return;
                }

                if (pathList.size() == 0) {
                    ToastUtil.show("图纸不能为空");
                    return;
                }

                ReleaseSkillReq req = new ReleaseSkillReq();
                req.setTitle(title.getText().toString());
                req.setContact(contact.getText().toString());
                req.setContact_mobile(phone.getText().toString());
                req.setAddress(addressDetail.getText().toString());
                req.setProvince(provinceId);
                req.setCity(cityId);
                req.setDistrict(districtId);
                req.setDescribe(content.getText().toString());

                if(pathList.get(0).length() < 100){
                    List<String> list = new ArrayList<>();
                    for(String s : pathList){
                        list.add(Utils.Bitmap2StrByBase64(s));
                    }
                    req.setImages(list);
                }

                presenter.publishMutual(this, req);

                break;
        }
    }

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
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

    @Override
    public void getData(ReleaseProjRes res) {
        new IOSDialog(this).builder()
                .setContentView(R.layout.pay_dialog)
                .setNegativeBtnColor(R.color.light_gray)
                .setNegativeButton("再次发布", v1 -> {
                    ReleaseHelpInfoActivity.startActivity(this);
                    finish();
                })
                .setPositiveBtnColor(R.color.light_blank)
                .setPositiveButton("查看订单", v1 -> {
                    WebViewActivity.startActivity(this, res.getUrl());
                    finish();
                })
                .show();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(10);
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                imageItems.clear();
                imageItems.addAll(images);
                setResultToAdapter(imageItems);
            } else {
                ToastUtil.show("没有数据");
            }
        }
    }

    private void setResultToAdapter(ArrayList<ImageItem> images) {

        pathList.clear();
        Observable.from(images)
                .map(imageItem -> {
                    pathList.add(imageItem.path);
                    return imageItem.path;
                })
                .toList()
                .subscribe(strings -> {
                    adapter.clear();
                    adapter.addAll(strings);
                    if (strings.size() != 10) {
                        adapter.add(ADD);
                    }

                });
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
