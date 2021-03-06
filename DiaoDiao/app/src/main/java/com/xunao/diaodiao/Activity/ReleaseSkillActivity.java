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
import com.xunao.diaodiao.Bean.ProjectTypeRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseSkillPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseSkillView;
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
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.picker.TimePicker;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ReleaseSkillActivity extends BaseActivity implements ReleaseSkillView, View.OnClickListener {

    @Inject
    ReleaseSkillPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.project_type)
    TextView projectType;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.fee)
    EditText fee;
    @BindView(R.id.days)
    EditText days;
    @BindView(R.id.proj_time_layout)
    RelativeLayout projTimeLayout;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.project_type_layout)
    RelativeLayout projectTypeLayout;
    @BindView(R.id.address_layout)
    RelativeLayout addressLayout;
    @BindView(R.id.num)
    EditText num;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private RecyclerArrayAdapter<String> adapter;
    private StringBuilder timeLong;
    private String percent;

    private RecyclerArrayAdapter<ProjectTypeRes.TypeBean> textAdapter;
    //private CustomPopWindow popWindow;
    private ProjectTypeRes res;
    private List<String> data = new ArrayList<>();
    private SinglePicker<String> singlePicker;

    private int provinceId, cityId, districtId;
    private AddressPicker picker;

    private ReleaseSkillReq req;

    private String price;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseSkillActivity.class);
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
        setContentView(R.layout.activity_release_skill);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布零工信息");

        req = (ReleaseSkillReq) getIntent().getSerializableExtra(INTENT_KEY);

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
                    baseViewHolder.setVisible(R.id.delete, req != null ? false : true);
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

        textAdapter = new RecyclerArrayAdapter<ProjectTypeRes.TypeBean>(this, R.layout.single_recycler_item_pop) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ProjectTypeRes.TypeBean s) {
                baseViewHolder.setText(R.id.text, s.getName());
            }
        };

        textAdapter.setOnItemClickListener((view, i) -> {
            //popWindow.dissmiss();
            projectType.setText(textAdapter.getAllData().get(i).getName());
        });

        next.setOnClickListener(this);


        addressLayout.setOnClickListener(v -> {
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
        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });


        if (req != null) {
            title.setText(req.getTitle());
            address.setText(req.getRegion());
            addressDetail.setText(req.getAddress());

            //projectType.setText(req.getProject_type());
            contact.setText(req.getContact());
            phone.setText(req.getContact_mobile());
            fee.setText(req.getDaily_wage());
            days.setText(req.getTotal_day());
            time.setText(Utils.millToYearString(req.getBuild_time()));
            req.setBuild_time_string(time.getText().toString());
            content.setText(req.getDescribe());
            adapter.clear();
            adapter.addAll(req.getImages());
            num.setText(req.getPeople_numbers()+"");

            addressLayout.setOnClickListener(null);
            addressDetail.setFocusable(false);
            days.setFocusable(false);
            fee.setFocusable(false);
            content.setFocusable(false);
            num.setFocusable(false);

            RxBus.getInstance().toObservable(String.class)
                    .filter(s -> TextUtils.equals(s, "update_project"))
                    .subscribe(s -> {
                        finish();
                    });

        } else {
            projTimeLayout.setOnClickListener(this);
            projectTypeLayout.setOnClickListener(this);
            initImagePicker();
            presenter.getPercent(this);
            if (Constants.addressResult.size() == 0) {
                presenter.getAddressData(this);
            } else {
                getAddressData(Constants.addressResult);
            }

        }

        presenter.publishOddType();
        presenter.publishOddPrice(this);
    }

    @Override
    public void getData(GetPercentRes res) {
        percent = res.getPercent();
    }

    @Override
    public void getPrice(GetPercentRes res) {
        price = res.getPrice();
        fee.setHint("填写日薪(不低于"+price+"元)");
    }

    @Override
    public void getProjectType(ProjectTypeRes res) {
        this.res = res;

        for (ProjectTypeRes.TypeBean bean : res.getTypes()) {
            data.add(bean.getName());
            if (req != null) {
                if (bean.getId() == Integer.valueOf(req.getProject_type())) {
                    projectType.setText(bean.getName());
                    req.setProject_type_string(bean.getName());
                }
            }
        }
        singlePicker = new SinglePicker<>(this, data);
        singlePicker.setCanceledOnTouchOutside(false);
        singlePicker.setSelectedIndex(1);
        singlePicker.setCycleDisable(true);
        singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                //ToastUtil.show(item);
                projectType.setText(item);
            }
        });

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
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


                if (this.req == null) {
                    if (TextUtils.isEmpty(projectType.getText())) {
                        ToastUtil.show("类型不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(address.getText())) {
                        ToastUtil.show("选择地区");
                        return;
                    }

                    if (TextUtils.isEmpty(addressDetail.getText())) {
                        ToastUtil.show("地址不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(fee.getText())) {
                        ToastUtil.show("日薪不能为空");
                        return;
                    }

                    try {
                        if (Float.valueOf(price) > Float.valueOf(fee.getText().toString().trim())) {
                            ToastUtil.show("日薪不能低于" + price);
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    if (TextUtils.isEmpty(days.getText())) {
                        ToastUtil.show("工作天数不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(num.getText())) {
                        ToastUtil.show("零工人数不能为空");
                        return;
                    }

                    if (TextUtils.equals(time.getText().toString(),
                            "到时间自动取消，工费退款到余额")) {
                        ToastUtil.show("施工时间不能为空");
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
                    req.setProvince(provinceId);
                    req.setCity(cityId);
                    req.setDistrict(districtId);
                    req.setRegion(address.getText().toString());
                    req.setAddress(addressDetail.getText().toString());
                    req.setPeople_numbers(Integer.valueOf(num.getText().toString()));

                    for (ProjectTypeRes.TypeBean bean : res.getTypes()) {
                        if (TextUtils.equals(bean.getName(), projectType.getText().toString())) {
                            req.setProject_type(bean.getId() + "");
                        }
                    }

                    req.setProject_type_string(projectType.getText().toString());
                    req.setContact(contact.getText().toString());
                    req.setContact_mobile(phone.getText().toString());
                    req.setDaily_wage(fee.getText().toString());
                    req.setTotal_day(days.getText().toString());
                    req.setBuild_time_string(time.getText().toString());
                    req.setBuild_time(Utils.convertTime2long(time.getText().toString()));
                    req.setDescribe(content.getText().toString());
                    req.setDescribe(content.getText().toString());
                    req.setImages(pathList);
                    ReleaseSkillSecondActivity.startActivity(this, req);
                } else {
                    req.setTitle(title.getText().toString());
                    req.setContact(contact.getText().toString());
                    req.setContact_mobile(phone.getText().toString());
                    ReleaseSkillSecondActivity.startActivity(this, req, true);
                }


                break;

            case R.id.proj_time_layout:
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
                        //time.setText(timeLong.toString());
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
                        time.setText(timeLong.toString());
                        //req.setBuild_time_string(timeLong.toString());
                    }
                });

                break;

            case R.id.project_type_layout:
                showPop();
                break;
        }
    }

    private void showPop() {
        singlePicker.show();
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
