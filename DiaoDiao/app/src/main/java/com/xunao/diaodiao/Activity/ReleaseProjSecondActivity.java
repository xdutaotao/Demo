package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Present.ReleaseProjSecondPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseProjSecondView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.type)
    EditText type;
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
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.dinuan_layout)
    RelativeLayout dinuanLayout;

    private ReleaseProjReq req;
    private IOSDialog dialog;
    private String dialogMianJi, dialogPrice;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private RecyclerArrayAdapter<String> adapter;
    private StringBuilder timeLong;

    public static void startActivity(Context context, ReleaseProjReq req) {
        Intent intent = new Intent(context, ReleaseProjSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
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
        select.setText(req.getProject_class());
        next.setOnClickListener(this);


        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)){
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.image, R.drawable.icon_paishe);
                }else{
                    baseViewHolder.setVisible(R.id.delete, true);
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
            }
        });
        adapter.add(ADD);
        recyclerView.setAdapter(adapter);


        addressDetailLayout.setOnClickListener(v -> {
            AddressActivity.startActivityForResult(this);
        });

        dinuanLayout.setOnClickListener(this);

        time.setOnClickListener(this);
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
                    pathList.add(Utils.Bitmap2StrByBase64(imageItem.path));
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
                if (TextUtils.isEmpty(title.getText())){
                    ToastUtil.show("标题不能为空");
                    return;
                }

//                if (TextUtils.isEmpty(type.getText())){
//                    ToastUtil.show("类型不能为空");
//                    return;
//                }

//                if (TextUtils.isEmpty(address.getText())){
//                    ToastUtil.show("选择地区");
//                    return;
//                }

                if (TextUtils.isEmpty(addressDetail.getText())){
                    ToastUtil.show("类型不能为空");
                    return;
                }

                if (TextUtils.isEmpty(name.getText())){
                    ToastUtil.show("联系人不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone.getText())){
                    ToastUtil.show("联系人电话不能为空");
                    return;
                }

                if (TextUtils.isEmpty(time.getText())){
                    ToastUtil.show("施工时间不能为空");
                    return;
                }

                if (TextUtils.isEmpty(content.getText())){
                    ToastUtil.show("描述不能为空");
                    return;
                }

                if (pathList.size() == 0){
                    ToastUtil.show("图纸不能为空");
                    return;
                }

                if (TextUtils.isEmpty(price.getText())){
                    ToastUtil.show("价格不能为空");
                    return;
                }

                if (TextUtils.isEmpty(dialogMianJi) || TextUtils.isEmpty(dialogPrice)){
                    ToastUtil.show("总价不能为空");
                    return;
                }

                req.setTitle(title.getText().toString());
                req.setAddress(addressDetail.getText().toString());
                req.setProvince(0);
                req.setCity(1);
                req.setDistrict(3);
                req.setContact(name.getText().toString());
                req.setContact_mobile(phone.getText().toString());
                req.setBuild_time(Utils.convert2long(time.getText().toString()));
                req.setImages(pathList);
                req.setDescribe(content.getText().toString());

                ReleaseProjThirdActivity.startActivity(this);
                break;

            case R.id.dinuan_layout:
                View view = LayoutInflater.from(this)
                        .inflate(R.layout.release_proj_dialog, null);
                ImageView cancle = (ImageView) view.findViewById(R.id.cancle_dialog);
                cancle.setOnClickListener(v1 -> {
                    if (dialog != null)
                        dialog.dismiss();
                });

                EditText mianji = (EditText) view.findViewById(R.id.mianji);
                EditText price = (EditText) view.findViewById(R.id.price);
                view.findViewById(R.id.sure).setOnClickListener(v1 -> {
                    if (TextUtils.isEmpty(mianji.getText().toString())){
                        ToastUtil.show("请输入面积");
                        return;
                    }

                    if (TextUtils.isEmpty(price.getText())){
                        ToastUtil.show("请输入单价");
                        return;
                    }

                    dialogMianJi = mianji.getText().toString();
                    dialogPrice = price.getText().toString();
                    if (dialog != null)
                        dialog.dismiss();

                });
                dialog = new IOSDialog(this).builder()
                        .setContentView(view);
                dialog.show();
                break;

            case R.id.time:
                DatePicker datePicker = new DatePicker(this);
                datePicker.show();

                TimePicker timePicker = new TimePicker(this);

                timeLong = new StringBuilder();
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        //ToastUtil.show(year+month+day);
                        timeLong.append(year+"-")
                                .append(month+"-")
                                .append(day);

                        timePicker.show();
                        timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                            @Override
                            public void onTimePicked(String hour, String minute) {
                                timeLong.append(" "+hour+":")
                                        .append(minute);

                                time.setText(timeLong.toString());
                            }
                        });

                    }
                });
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
