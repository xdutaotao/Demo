package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.ExpensesInfoRes;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseProjReqTemp;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseProjSecondPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseProjSecondView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.math.BigDecimal;
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

/**
 * create by
 */
public class ReleaseProjSecondActivity extends BaseActivity implements ReleaseProjSecondView, View.OnClickListener {

    @Inject
    ReleaseProjSecondPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.address_detail_layout)
    RelativeLayout addressDetailLayout;
    @BindView(R.id.select)
    TextView select;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.type_recycler_view)
    RecyclerView typeRecyclerView;

    private ReleaseProjReq req;
    private IOSDialog dialog;
    private RecyclerArrayAdapter<ReleaseProjReq.ExpensesBean> typeAdapter;
    private List<ReleaseProjReq.ExpensesBean> tempList = new ArrayList<>();
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
    private int provinceId, cityId, districtId;
    private AddressPicker picker;

    private boolean flag = false;

    public static void startActivity(Context context, ReleaseProjReq req) {
        Intent intent = new Intent(context, ReleaseProjSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ReleaseProjReq req,  boolean flag) {
        Intent intent = new Intent(context, ReleaseProjSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_proj_second);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布项目信息");
        req = (ReleaseProjReq) getIntent().getSerializableExtra(INTENT_KEY);
        select.setText(req.getProject_type_class());
        next.setOnClickListener(this);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)) {
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.image, R.drawable.icon_paishe);
                } else {
                    baseViewHolder.setVisible(R.id.delete, flag? false: true);
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
                PhotoActivity.startActivity(this, pathList.get(i), pathList.get(i).contains("http"));
            }
        });
        adapter.add(ADD);
        recyclerView.setAdapter(adapter);

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 200){
                    content.setText(s.subSequence(0, 200));
                }else{
                    contentNum.setText(s.length() + " / 200");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        typeAdapter = new RecyclerArrayAdapter<ReleaseProjReq.ExpensesBean>(this, R.layout.res_proj_type_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ReleaseProjReq.ExpensesBean s) {
                baseViewHolder.setText(R.id.name, s.getName());
                if (TextUtils.isEmpty(s.getAmount())) {
                    baseViewHolder.setVisible(R.id.type_detail, false);
                    baseViewHolder.setVisible(R.id.type_temp, true);
                } else {
                    baseViewHolder.setVisible(R.id.type_detail, true);
                    baseViewHolder.setVisible(R.id.type_temp, false);
                    baseViewHolder.setText(R.id.type_detail,
                            s.getUnit_price() + "元 * " + s.getAmount() + s.getUnit());
                }
            }
        };

        typeAdapter.setOnItemClickListener((view, i) -> {
            showDialog(typeAdapter.getAllData().get(i));
        });
        LinearLayoutManager manager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        typeRecyclerView.setLayoutManager(manager);
        typeRecyclerView.setAdapter(typeAdapter);

        addressDetailLayout.setOnClickListener(v -> {
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


        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });

        time.setText(Utils.getNowDate());

        flag = getIntent().getBooleanExtra("flag", false);
        if (flag){
            //再次编辑
            title.setText(req.getTitle());
            address.setText(req.getRegion());
            addressDetail.setText(req.getAddress());
            name.setText(req.getContact());
            phone.setText(req.getContact_mobile());
            time.setText(Utils.millToYearString(req.getBuild_time()));
            content.setText(req.getDescribe());
            adapter.clear();
            adapter.addAll(req.getImages());
            tempList.clear();
            tempList.addAll(req.getExpenses());
            typeAdapter.addAll(tempList);

            addressDetailLayout.setOnClickListener(null);
            addressDetail.setFocusable(false);
            time.setOnClickListener(null);
            content.setFocusable(false);
            typeAdapter.setOnItemClickListener(null);

            RxBus.getInstance().toObservable(String.class)
                    .filter(s -> TextUtils.equals(s, "update_project"))
                    .subscribe(s -> {
                       finish();
                    });

        }else{
            presenter.typeExpenses(req.getProject_class());
            time.setOnClickListener(this);
            initImagePicker();
            presenter.getPercent();
            presenter.getAddressData(this);
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
    public void getData(ExpensesInfoRes res) {
        tempList.clear();
        tempList.addAll(res.getExpenses_info());
        typeAdapter.addAll(res.getExpenses_info());
    }

    @Override
    public void getPercent(GetPercentRes res) {
        percent = res.getPercent();
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
                        address.setText(province.getAreaName()+"-"+city.getAreaName());
                        //ToastUtil.show(province.getAreaName() + city.getAreaName());
                    } else {
                        provinceId = Integer.valueOf(province.getAreaId());
                        cityId = Integer.valueOf(city.getAreaId());
                        districtId = Integer.valueOf(county.getAreaId());
                        address.setText(province.getAreaName()+"-"
                                +city.getAreaName()+"-"
                                +county.getAreaName());

                        //ToastUtil.show(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                    req.setRegion(address.getText().toString());
                }
            });
        }
    }

    private void showDialog(ReleaseProjReq.ExpensesBean temp) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.release_proj_dialog, null);
        ImageView cancle = (ImageView) view.findViewById(R.id.cancle_dialog);
        cancle.setOnClickListener(v1 -> {
            if (dialog != null)
                dialog.dismiss();
        });

        EditText amount = (EditText) view.findViewById(R.id.amount);
        EditText price = (EditText) view.findViewById(R.id.price);
        TextView unit = (TextView) view.findViewById(R.id.unit);
        unit.setText(temp.getUnit());

        price.setHint("最小输入" + temp.getCost());

        TextView warning = (TextView) view.findViewById(R.id.warning);
        if(select.getText().toString().contains("水泥回填")){
            warning.setVisibility(View.VISIBLE);
        }else{
            warning.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(temp.getAmount())){
            amount.setText(temp.getAmount());
        }

        if (TextUtils.isEmpty(temp.getUnit_price())){
            price.setText(temp.getUnit_price());
        }

        view.findViewById(R.id.sure).setOnClickListener(v1 -> {
            if (TextUtils.isEmpty(amount.getText().toString())) {
                ToastUtil.show("请输入数量");
                return;
            }

            if (TextUtils.isEmpty(price.getText())) {
                ToastUtil.show("请输入单价");
                return;
            }

            if (Float.valueOf(temp.getCost()) > Float.valueOf(price.getText().toString())) {
                ToastUtil.show("输入单价太小");
                return;
            }

            temp.setAmount(amount.getText().toString());
            temp.setUnit_price(price.getText().toString());
            typeAdapter.notifyDataSetChanged();
            if (dialog != null)
                dialog.dismiss();

        });
        dialog = new IOSDialog(this).builder()
                .setContentView(view);
        dialog.show();
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

                if (TextUtils.isEmpty(address.getText())){
                    ToastUtil.show("选择地区");
                    return;
                }

                if (TextUtils.isEmpty(addressDetail.getText())) {
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

                if (TextUtils.isEmpty(time.getText())) {
                    ToastUtil.show("施工时间不能为空");
                    return;
                }

                if (TextUtils.isEmpty(content.getText())) {
                    ToastUtil.show("描述不能为空");
                    return;
                }

                if (adapter.getAllData().size() == 0) {
                    ToastUtil.show("图纸不能为空");
                    return;
                }

                if (!flag){
                    List<ReleaseProjReq.ExpensesBean> releaseProjReqs = new ArrayList<>();
                    req.setProject_fee("0");
                    for (ReleaseProjReq.ExpensesBean temp : tempList) {
                        if (TextUtils.isEmpty(temp.getAmount())) {
                            ToastUtil.show("请输入价格和数量");
                            return;
                        }

                        ReleaseProjReq.ExpensesBean bean = new ReleaseProjReq.ExpensesBean();
                        bean.setExpenses_id(temp.getExpenses_id());
                        bean.setUnit_price(temp.getUnit_price());
                        bean.setAmount(temp.getAmount());
                        BigDecimal bigDecimal = new BigDecimal(Float.valueOf(temp.getUnit_price())
                                * Float.valueOf(temp.getAmount()));
                        bigDecimal.setScale(2, 4);
                        req.setProject_fee(String.valueOf(Float.valueOf(req.getProject_fee()) +
                                bigDecimal.floatValue()));
                        String totalPrice = String.valueOf(bigDecimal.floatValue());
                        bean.setTotal_price(totalPrice);
                        bean.setName(temp.getName());
                        bean.setUnit(temp.getUnit());
                        releaseProjReqs.add(bean);
                    }

                    if (TextUtils.isEmpty(percent)){
                        percent = "10";
                    }
                    BigDecimal serviceFee = new BigDecimal(Float.valueOf(req.getProject_fee())/Integer.valueOf(percent));
                    serviceFee.setScale(2, 4);
                    req.setService_cost(String.valueOf(serviceFee.floatValue()));
                    BigDecimal allFee = serviceFee.add(new BigDecimal(Float.valueOf(req.getProject_fee())));
                    allFee.setScale(2, 4);
                    req.setTotal_price(String.valueOf(allFee.floatValue()));

                    req.setAddress(addressDetail.getText().toString());
                    req.setProvince(provinceId);
                    req.setCity(cityId);
                    req.setDistrict(districtId);
                    req.setBuild_time(Utils.convert2long(time.getText().toString()));
                    req.setBuild_time_string(time.getText().toString());
                    if(pathList.size() == 0){
                        ToastUtil.show("请添加图片");
                        return;
                    }
                    req.setImages(pathList);
                    req.setDescribe(content.getText().toString());
                    req.setExpenses(releaseProjReqs);
                }

                req.setTitle(title.getText().toString());
                req.setContact(name.getText().toString());
                req.setContact_mobile(phone.getText().toString());

                ReleaseProjThirdActivity.startActivity(this, req, flag);
                break;

            case R.id.time:
                DatePicker datePicker = new DatePicker(this);

                String[] dates = Utils.getNowDate().split("-");
                datePicker.setRangeStart(Integer.valueOf(dates[0]),
                        Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                timeLong = new StringBuilder();
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        //ToastUtil.show(year+month+day);
                        timeLong.append(year + "-")
                                .append(month + "-")
                                .append(day);
                        time.setText(timeLong.toString());
                        req.setBuild_time_string(timeLong.toString());
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

                break;
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
