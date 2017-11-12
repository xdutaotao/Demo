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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseHelpReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseSkillInforPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseSkillInforView;
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
import cn.qqtheme.framework.picker.TimePicker;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.addressResult;

/**
 * create by
 */
public class ReleaseSkillInforActivity extends BaseActivity implements ReleaseSkillInforView, View.OnClickListener {

    @Inject
    ReleaseSkillInforPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.region)
    TextView region;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.fee)
    EditText fee;
    @BindView(R.id.build_time)
    TextView buildTime;
    @BindView(R.id.build_time_layout)
    RelativeLayout buildTimeLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private RecyclerArrayAdapter<String> adapter;
    private StringBuilder timeLong;
    private boolean flag;
    private AddressPicker picker;
    private int provinceId, cityId, districtId;
    private ReleaseHelpReq req;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseSkillInforActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ReleaseHelpReq req, boolean flag) {
        Intent intent = new Intent(context, ReleaseSkillInforActivity.class);
        intent.putExtra(INTENT_KEY, flag);
        intent.putExtra("req", req);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_infor);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布保养信息");

        flag = getIntent().getBooleanExtra(INTENT_KEY, false);
        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)) {
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.image, R.drawable.icon_paishe);
                } else {
                    baseViewHolder.setVisible(R.id.delete, !flag);
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
            } else {
                if (pathList.size() > 0)
                    PhotoActivity.startActivity(this, pathList.get(i), pathList.get(i).contains("http"));
            }
        });
        adapter.add(ADD);
        recyclerView.setAdapter(adapter);

        type.setText(ReleaseHelpActivity.projectTypeName + "-" +
                ReleaseHelpActivity.projectClassName + "-" +
                ReleaseHelpActivity.projectBrandName);

        addressLayout.setOnClickListener(this);
        buildTimeLayout.setOnClickListener(this);

        next.setOnClickListener(this);

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

        req = (ReleaseHelpReq) getIntent().getSerializableExtra("req");
        if (req == null)
            req = new ReleaseHelpReq();

        if (flag) {
            //再次编辑
            title.setText(req.getTitle());
            region.setText(req.getRegion());
            address.setText(req.getAddress());
            name.setText(req.getContact());
            phone.setText(req.getContact_mobile());
            buildTime.setText(req.getBuildTimeString());
            fee.setText(req.getDoor_fee());
            content.setText(req.getDescribe());
            adapter.clear();
            adapter.addAll(req.getImages());

            address.setFocusable(false);
            addressLayout.setOnClickListener(null);
            buildTimeLayout.setOnClickListener(null);
            content.setFocusable(false);
            fee.setFocusable(false);

            type.setText(req.getEquip_type());
            req.setTypeString(req.getEquip_type());

        } else {
            initImagePicker();
            if (addressResult.size() == 0)
                presenter.getAddressData(this);
            else
                getAddressData(addressResult);
        }


        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });

        presenter.publishMaintenancePrice(this);

        if(ReleaseHelpActivity.project_type == 1){
            //请详细描述故障情况
            content.setHint("请详细描述故障情况");
        }else{
            //保养
            content.setHint("请详细描述您的需求");
        }

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

    @Override
    public void getAddressData(ArrayList<Province> result) {
        if (result.size() > 0) {
            addressResult.addAll(result);
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
                        region.setText(province.getAreaName() + "-"
                                + city.getAreaName() + "-"
                                + county.getAreaName());

                        //ToastUtil.show(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                    req.setRegion(region.getText().toString());
                }
            });
        }

    }

    @Override
    public void getData(GetPercentRes res) {
        fee.setHint(res.getPrice());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (TextUtils.isEmpty(title.getText())) {
                    ToastUtil.show("标题不能为空");
                    return;
                }

                if (TextUtils.isEmpty(region.getText())) {
                    ToastUtil.show("选择地区");
                    return;
                }

                if (TextUtils.isEmpty(address.getText())) {
                    ToastUtil.show("地址不能为空");
                    return;
                }

                if (TextUtils.isEmpty(name.getText())) {
                    ToastUtil.show("联系人不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone.getText())) {
                    ToastUtil.show("联系人电话不能为空");
                    return;
                }

                if (TextUtils.isEmpty(buildTime.getText())) {
                    ToastUtil.show("上门时间不能为空");
                    return;
                }

                if (TextUtils.isEmpty(content.getText())) {
                    ToastUtil.show("描述不能为空");
                    return;
                }

                if (TextUtils.isEmpty(fee.getText())) {
                    ToastUtil.show("上门费不能为空");
                    return;
                }

                if (adapter.getAllData().size() == 0) {
                    ToastUtil.show("图纸不能为空");
                    return;
                }

                req.setTitle(title.getText().toString());
                req.setContact(name.getText().toString());
                req.setContact_mobile(phone.getText().toString());
                if (!flag) {
                    req.setAddress(address.getText().toString());
                    req.setDescribe(content.getText().toString());
                    req.setDoor_fee(fee.getText().toString());
                    req.setTypeString(ReleaseHelpActivity.projectTypeName + "-" +
                            ReleaseHelpActivity.projectClassName + "-" +
                            ReleaseHelpActivity.projectBrandName);
                    req.setEquip_type(ReleaseHelpActivity.projectTypeName + "-" +
                            ReleaseHelpActivity.projectClassName + "-" +
                            ReleaseHelpActivity.projectBrandName);
                    req.setBuildTimeString(buildTime.getText().toString());
                    req.setProvince(provinceId);
                    req.setCity(cityId);
                    req.setDistrict(districtId);
                    req.setDoor_time(Utils.convertTime2long(buildTime.getText().toString()));
                    req.setProject_class(ReleaseHelpActivity.project_class);
                    req.setProject_brand(ReleaseHelpActivity.project_brand);
                    req.setProject_type(ReleaseHelpActivity.project_type);
                    req.setImages(pathList);
                    ReleaseSkillSureInfoActivity.startActivity(this, req);
                }else{
                    ReleaseSkillSureInfoActivity.startActivity(this, req, true);
                }

                break;

            case R.id.address_layout:

                if (picker != null) {
                    if (TextUtils.isEmpty(address.getText())) {
                        picker.setSelectedItem("上海", "上海", "长宁");
                    } else {
                        String[] addresss = address.getText().toString().split("-");
                        if (addresss.length == 3) {
                            picker.setSelectedItem(addresss[0], addresss[1], addresss[2]);
                        } else {
                            picker.setSelectedItem(addresss[0], addresss[1], addresss[1]);
                        }

                    }
                    picker.show();
                }

                break;

            case R.id.build_time_layout:

                DatePicker datePicker = new DatePicker(this);

                String[] dates = Utils.getNowDate().split("-");
                datePicker.setRangeStart(Integer.valueOf(dates[0]),
                        Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                datePicker.setRange(Integer.valueOf(dates[0]), 2100);


                timeLong = new StringBuilder();
                TimePicker timePicker = new TimePicker(this);
                timePicker.setRangeStart(0, 0);

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        //ToastUtil.show(year+month+day);
                        timeLong.append(year + "-")
                                .append(month + "-")
                                .append(day);
//                        time.setText(timeLong.toString());
//                        req.setBuild_time_string(timeLong.toString());

                        timePicker.show();
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

                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        timeLong.append(" " + hour + ":" + minute);
                        buildTime.setText(timeLong.toString());
                        req.setBuildTimeString(timeLong.toString());
                    }
                });

                break;
        }
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
