package com.demo.cworker.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.demo.cworker.Bean.CollectBean;
import com.demo.cworker.Bean.NumberBean;
import com.demo.cworker.Bean.PackageBean;
import com.demo.cworker.Model.User;
import com.demo.cworker.Present.CollectPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.JsonUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.Utils.Utils;
import com.demo.cworker.View.CollectView;
import com.demo.cworker.Widget.CustomTextWatcher;
import com.demo.cworker.Widget.GlideImageLoader;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class CollectActivity extends BaseActivity implements CollectView, View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {
    private static final String ADD = "add";
    private static final int IMAGE_PICKER = 8888;
    public static final int REQUEST_CODE = 6666;
    public static final int REQUEST_WRAP_CODE = 1111;
    public static final int REQUEST_TYPE_CODE = 1112;
    public static final int REQUEST_CHECK_CODE = 1113;
    public static final int REQUEST_RECOMMAND_CODE = 1114;
    public static final int REQUEST_TAKE_PHOTO_CODE = 2222;

    @Inject
    CollectPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.wrap_layout)
    RelativeLayout wrapLayout;
    @BindView(R.id.type_layout)
    RelativeLayout typeLayout;
    @BindView(R.id.modle_layout)
    RelativeLayout modleLayout;
    @BindView(R.id.length)
    EditText length;
    @BindView(R.id.width)
    EditText width;
    @BindView(R.id.height)
    EditText height;
    @BindView(R.id.weight)
    EditText weight;
    @BindView(R.id.all_length)
    EditText allLength;
    @BindView(R.id.all_width)
    EditText allWidth;
    @BindView(R.id.all_height)
    EditText allHeight;
    @BindView(R.id.data_layout)
    RelativeLayout dataLayout;
    @BindView(R.id.recommend_layout)
    RelativeLayout recommendLayout;
    @BindView(R.id.information)
    EditText information;
    @BindView(R.id.info_num)
    TextView infoNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.wrap_text)
    TextView wrapText;
    @BindView(R.id.type_txt)
    TextView typeTxt;
    @BindView(R.id.modle_num)
    EditText modleNum;
    @BindView(R.id.modle_tv)
    TextView modleTv;
    @BindView(R.id.number_tv)
    TextView numberTv;
    @BindView(R.id.length_tv)
    TextView lengthTv;
    @BindView(R.id.width_tv)
    TextView widthTv;
    @BindView(R.id.hight_tv)
    TextView hightTv;
    @BindView(R.id.weight_tv)
    TextView weightTv;
    @BindView(R.id.all_length_tv)
    TextView allLengthTv;
    @BindView(R.id.all_width_tv)
    TextView allWidthTv;
    @BindView(R.id.all_height_tv)
    TextView allHeightTv;
    @BindView(R.id.out_length_tv)
    TextView outLengthTv;
    @BindView(R.id.out_length)
    EditText outLength;
    @BindView(R.id.out_width_tv)
    TextView outWidthTv;
    @BindView(R.id.out_width)
    EditText outWidth;
    @BindView(R.id.out_height_tv)
    TextView outHeightTv;
    @BindView(R.id.out_height)
    EditText outHeight;
    @BindView(R.id.out_weight_tv)
    TextView outWeightTv;
    @BindView(R.id.out_weight)
    EditText outWeight;
    @BindView(R.id.single_tv)
    TextView singleTv;
    @BindView(R.id.single_switch)
    Switch singleSwitch;
    @BindView(R.id.out_layout)
    LinearLayout outLayout;
    @BindView(R.id.single_length_tv)
    TextView singleLengthTv;
    @BindView(R.id.single_length)
    EditText singleLength;
    @BindView(R.id.single_width_tv)
    TextView singleWidthTv;
    @BindView(R.id.single_width)
    EditText singleWidth;
    @BindView(R.id.single_height_tv)
    TextView singleHeightTv;
    @BindView(R.id.single_height)
    EditText singleHeight;
    @BindView(R.id.single_weight_tv)
    TextView singleWeightTv;
    @BindView(R.id.single_weight)
    EditText singleWeight;
    @BindView(R.id.single_layout)
    LinearLayout singleLayout;
    @BindView(R.id.check_tv)
    TextView checkTv;
    @BindView(R.id.recommend_tv)
    TextView recommendTv;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    /**
     * 图片adapter
     */
    private RecyclerArrayAdapter<String> adapter;
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    /**
     * 选中颜色，正常颜色
     */
    private int checkedColor, normalColor;
    /**
     * EditText 末尾 x号
     */
    private Drawable drawable;
    /**
     * 本页获取的网络数据
     */
    private PackageBean.ResultBean resultBean;
    /**
     * 选中的类型
     */
    private ArrayList<String> recommandChangeList = new ArrayList<>();
    /**
     * 地理位置
     */
    private LocationManager locationManager;
    private String locationProvider;

    private CollectBean collectBean;
    private List<String> historyPathList = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, CollectBean bean) {
        Intent intent = new Intent(context, CollectActivity.class);
        intent.putExtra(INTENT_KEY, (Serializable) bean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "采集");

        checkedColor = getResources().getColor(R.color.colorPrimary);
        normalColor = getResources().getColor(R.color.black);
        drawable = getResources().getDrawable(R.drawable.cancle);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.select_photo_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)) {
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.select_img, R.drawable.ic_launcher_round);
                } else {
                    baseViewHolder.setVisible(R.id.delete, true);
                    baseViewHolder.setImageUrl(R.id.select_img, s);
                }
            }
        };
        recyclerView.setAdapter(adapter);
        submit.setOnClickListener(this);
        adapter.add(ADD);
        if (getIntent().getSerializableExtra(INTENT_KEY) == null){
            adapter.setOnItemClickListener((view, i) -> {
                view.findViewById(R.id.delete).setOnClickListener(v -> {
                    imageItems.remove(i);
                    adapter.remove(i);
                    if (i == 9) {
                        adapter.add(ADD);
                    }
                });
                if (TextUtils.equals(adapter.getAllData().get(i), ADD)) {
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
                } else {

                }
            });
            initImagePicker();

            scrollView.setOnTouchListener((v, event) -> {
                scrollView.setFocusable(true);
                scrollView.setFocusableInTouchMode(true);
                scrollView.requestLayout();
                clearFocus();
                return false;
            });

            wrapLayout.setOnClickListener(this);
            typeLayout.setOnClickListener(this);

            modleNum.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            modleNum.setOnTouchListener(this);
            modleNum.setOnFocusChangeListener(this);
            modleNum.addTextChangedListener(new CustomTextWatcher(modleNum, drawable, false));

            number.setOnTouchListener(this);
            number.setOnFocusChangeListener(this);
            number.addTextChangedListener(new CustomTextWatcher(number, drawable, false));

            length.setOnTouchListener(this);
            length.setOnFocusChangeListener(this);
            length.addTextChangedListener(new CustomTextWatcher(length, drawable, true));

            width.setOnTouchListener(this);
            width.setOnFocusChangeListener(this);
            width.addTextChangedListener(new CustomTextWatcher(width, drawable, true));

            height.setOnTouchListener(this);
            height.setOnFocusChangeListener(this);
            height.addTextChangedListener(new CustomTextWatcher(height, drawable, true));

            weight.setOnTouchListener(this);
            weight.setOnFocusChangeListener(this);
            weight.addTextChangedListener(new CustomTextWatcher(weight, drawable, true));

            allLength.setOnTouchListener(this);
            allLength.setOnFocusChangeListener(this);
            allLength.addTextChangedListener(new CustomTextWatcher(allLength, drawable, true));

            allWidth.setOnTouchListener(this);
            allWidth.setOnFocusChangeListener(this);
            allWidth.addTextChangedListener(new CustomTextWatcher(allWidth, drawable, true));

            allHeight.setOnTouchListener(this);
            allHeight.setOnFocusChangeListener(this);
            allHeight.addTextChangedListener(new CustomTextWatcher(allHeight, drawable, true));

            outLength.setOnTouchListener(this);
            outLength.setOnFocusChangeListener(this);
            outLength.addTextChangedListener(new CustomTextWatcher(outLength, drawable, true));

            outWidth.setOnTouchListener(this);
            outWidth.setOnFocusChangeListener(this);
            outWidth.addTextChangedListener(new CustomTextWatcher(outWidth, drawable, true));

            outHeight.setOnTouchListener(this);
            outHeight.setOnFocusChangeListener(this);
            outHeight.addTextChangedListener(new CustomTextWatcher(outHeight, drawable, true));

            singleWeight.setOnTouchListener(this);
            singleWeight.setOnFocusChangeListener(this);
            singleWeight.addTextChangedListener(new CustomTextWatcher(singleWeight, drawable, true));

            singleLength.setOnTouchListener(this);
            singleLength.setOnFocusChangeListener(this);
            singleLength.addTextChangedListener(new CustomTextWatcher(singleLength, drawable, true));

            singleWidth.setOnTouchListener(this);
            singleWidth.setOnFocusChangeListener(this);
            singleWidth.addTextChangedListener(new CustomTextWatcher(singleWidth, drawable, true));

            singleHeight.setOnTouchListener(this);
            singleHeight.setOnFocusChangeListener(this);
            singleHeight.addTextChangedListener(new CustomTextWatcher(singleHeight, drawable, true));
            setEditTextChangedListener();

            singleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                singleLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            });

            dataLayout.setOnClickListener(v -> {
                CheckActivity.startActivityForResult(this, resultBean.getAts(), checkTv.getText().toString());
            });


            recommendLayout.setOnClickListener(v -> {
                RecommandActivity.startActivityForResult(this, resultBean.getPmts(), resultBean.getPms(), recommandChangeList);
            });
            presenter.packagingForm(this);
        }else{
            collectBean = (CollectBean) getIntent().getSerializableExtra(INTENT_KEY);
            number.setText(collectBean.getPartCode());
            name.setText(collectBean.getPartName());
            source.setText(collectBean.getSystemResource());
            wrapText.setText(collectBean.getPackageStypeName());
            typeTxt.setText(collectBean.getPartMaterialName());
            modleNum.setText(collectBean.getPackageModelCount()+"");
            length.setText(collectBean.getPartLength()+"");
            width.setText(collectBean.getPartWidth()+"");
            height.setText(collectBean.getPartHeigth()+"");
            weight.setText(collectBean.getNetWeight()+"");

            outLength.setText(collectBean.getPackageLength()+"");
            outWidth.setText(collectBean.getPackageWidth()+"");
            outHeight.setText(collectBean.getPackageHeight()+"");
            outWeight.setText(collectBean.getRoughWeight()+"");

            singleLength.setText(collectBean.getSinglePackageLength()+"");
            singleWidth.setText(collectBean.getSinglePackageWidth()+"");
            singleHeight.setText(collectBean.getSinglePackageHeight()+"");
            singleWeight.setText(collectBean.getSinglePackageWeight()+"");

            allLength.setText(collectBean.getAddedLength()+"");
            allWidth.setText(collectBean.getAddedWidth()+"");
            allHeight.setText(collectBean.getAddedHeight()+"");

            checkTv.setText(collectBean.getAuditType());
            recommendTv.setText(collectBean.getProcessRecommendation());
            information.setText(collectBean.getRemark());
            historyPathList.addAll(Arrays.asList(collectBean.getDocumentCodes().split(",")));
            adapter.addAll(historyPathList);
            if (historyPathList.size() < 10){
                adapter.add(ADD);
            }
        }

    }

    @Override
    public void getData(PackageBean.ResultBean bean) {
        resultBean = bean;
    }

    @Override
    public void getPostTxt(String s) {
        ToastUtil.show("上传成功");
        scrollView.scrollTo(0, 0);
        finish();
    }

    @Override
    public void getSearchData(NumberBean s) {
        if (s.getData() == null) {
            return;
        } else {
            name.setText(s.getData().getCnName());
            source.setText(s.getData().getSourceDistribution());
        }

        if (s.getHasOldData() == 1 && s.getOldData() != null) {
            wrapText.setText(s.getOldData().getPackageStypeName());
            typeTxt.setText(s.getOldData().getPartMaterialName());
            number.setText(s.getOldData().getPackageModelCount() + "");
            length.setText(String.valueOf(s.getOldData().getPartLength()));
            width.setText(String.valueOf(s.getOldData().getPartWidth()));
            height.setText(String.valueOf(s.getOldData().getPartHeight()));
            weight.setText(String.valueOf(s.getOldData().getNetWeight()));

            outLength.setText(String.valueOf(s.getOldData().getPackageLength()));
            outWidth.setText(String.valueOf(s.getOldData().getPackageWidth()));
            outHeight.setText(String.valueOf(s.getOldData().getPackageHeight()));
            outWeight.setText(String.valueOf(s.getOldData().getRoughWeight()));

            singleLength.setText(String.valueOf(s.getOldData().getSinglePackageLength()));
            singleWidth.setText(String.valueOf(s.getOldData().getSinglePackageWidth()));
            singleHeight.setText(String.valueOf(s.getOldData().getSinglePackageHeight()));
            singleWeight.setText(String.valueOf(s.getOldData().getSinglePackageWeight()));
            singleSwitch.setChecked(true);

            checkTv.setText(s.getOldData().getAuditType());
            recommendTv.setText(s.getOldData().getProcessRecommendation());
            information.setText(s.getOldData().getRemark());
            List<String> paths = JsonUtils.getInstance().JsonToList(s.getOldData().getDocumentCodes());
            Observable.from(paths)
                    .map(s1 -> {
                        ImageItem imageItem = new ImageItem();
                        imageItem.path = s1;
                        return imageItem;
                    }).toList()
                    .subscribe(imageItems1 -> {
                        imageItems.clear();
                        imageItems.addAll(imageItems1);
                        setResultToAdapter(imageItems);
                    });
        }
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
        } else if (resultCode == RESULT_OK) {
            if (data != null && requestCode == REQUEST_CODE) {
                String s = data.getStringExtra(INTENT_KEY);
                number.setText(s);
            } else if (data != null && requestCode == REQUEST_WRAP_CODE) {
                PackageBean.ResultBean.PssBean bean = (PackageBean.ResultBean.PssBean) data.getSerializableExtra(INTENT_KEY);
                wrapText.setText(bean.getName());
                if (bean.getHasOutPackage() == 0) {
                    outLayout.setVisibility(View.GONE);
                } else {
                    outLayout.setVisibility(View.VISIBLE);
                }
            } else if (data != null && requestCode == REQUEST_TAKE_PHOTO_CODE) {
                ArrayList<ImageItem> list = (ArrayList<ImageItem>) data.getSerializableExtra(INTENT_KEY);
                imageItems.addAll(list);
                setResultToAdapter(imageItems);
            } else if (data != null && requestCode == REQUEST_TYPE_CODE) {
                PackageBean.ResultBean.MtsBean bean = (PackageBean.ResultBean.MtsBean) data.getSerializableExtra(INTENT_KEY);
                typeTxt.setText(bean.getName());
            } else if (data != null && requestCode == REQUEST_CHECK_CODE) {
                PackageBean.ResultBean.AtsBean bean = (PackageBean.ResultBean.AtsBean) data.getSerializableExtra(INTENT_KEY);
                checkTv.setText(bean.getName());
            } else if (data != null && requestCode == REQUEST_RECOMMAND_CODE) {
                recommandChangeList.clear();
                recommandChangeList.addAll((List<String>) data.getSerializableExtra(INTENT_KEY));
                String tempTwo = recommandChangeList.toString().replace("[", "").replace("]", "");
                recommendTv.setText(tempTwo);
            }
        }
    }

    private void setResultToAdapter(ArrayList<ImageItem> images) {
        Observable.from(images)
                .map(imageItem -> imageItem.path)
                .toList()
                .subscribe(strings -> {
                    adapter.clear();
                    adapter.addAll(strings);
                    if (strings.size() != 10) {
                        adapter.add(ADD);
                    }
                });
    }


    private void actionScan() {
        ScanActivity.startActivityForResult(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (getIntent().getSerializableExtra(INTENT_KEY) == null){
                    postCollectTxt();
                }else{
                    postHistoryCollect();
                }

                break;

            case R.id.wrap_layout:
                WrapActivity.startActivityForResult(this, resultBean.getPss(), wrapText.getText().toString());
                break;

            case R.id.type_layout:
                TypeActivity.startActivityForResult(this, resultBean.getMts(), typeTxt.getText().toString());
                break;
        }
    }

    private void postHistoryCollect(){
        presenter.postCollectData(this, collectBean, historyPathList);
    }

    private void postCollectTxt() {
        String dialogMsg = "";

        if (TextUtils.isEmpty(numberTv.getText())) {
            ToastUtil.show("零件号不能为空");
            return;
        }
//        if (TextUtils.isEmpty(name.getText())) {
//            ToastUtil.show("零件中文名称不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(source.getText())) {
//            ToastUtil.show("系统来源分配不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(wrapText.getText())) {
            ToastUtil.show("到货包装形式不能为空");
            return;
        }
        if (TextUtils.isEmpty(typeTxt.getText())) {
            ToastUtil.show("零件材料类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(modleNum.getText())) {
            ToastUtil.show("零件包装模数不能为空");
            return;
        }
        if (outLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(outLength.getText())) {
                ToastUtil.show("外包装长不能为空");
                return;
            }
            if (TextUtils.isEmpty(outWidth.getText())) {
                ToastUtil.show("外包装宽不能为空");
                return;
            }
            if (TextUtils.isEmpty(outHeight.getText())) {
                ToastUtil.show("外包装高不能为空");
                return;
            }
            if (TextUtils.isEmpty(outHeight.getText())) {
                ToastUtil.show("毛重不能为空");
                return;
            }

            if (Utils.subText(outLength.getText().toString()) < Utils.subText(outWidth.getText().toString())
                    || Utils.subText(outWidth.getText().toString()) < Utils.subText(outHeight.getText().toString())) {
                dialogMsg = "外包装长宽高不满足测量规则！";
            }

        }

        if (singleLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(singleLength.getText())) {
                ToastUtil.show("单个包装长不能为空");
                return;
            }
            if (TextUtils.isEmpty(singleWidth.getText())) {
                ToastUtil.show("单个包装宽不能为空");
                return;
            }
            if (TextUtils.isEmpty(singleHeight.getText())) {
                ToastUtil.show("单个包装高不能为空");
                return;
            }
            if (TextUtils.isEmpty(singleWeight.getText())) {
                ToastUtil.show("单个包装重不能为空");
                return;
            }

            if (Utils.subText(singleLength.getText().toString()) < Utils.subText(singleWidth.getText().toString())
                    || Utils.subText(singleWidth.getText().toString()) < Utils.subText(singleHeight.getText().toString())) {
                dialogMsg = "单个包装长宽高不满足测量规则！";
            }
        }

        if (TextUtils.isEmpty(length.getText())) {
            ToastUtil.show("零件长不能为空");
            return;
        }
        if (TextUtils.isEmpty(width.getText())) {
            ToastUtil.show("零件宽不能为空");
            return;
        }
        if (TextUtils.isEmpty(height.getText())) {
            ToastUtil.show("零件高不能为空");
            return;
        }
        if (TextUtils.isEmpty(weight.getText())) {
            ToastUtil.show("净重不能为空");
            return;
        }

        if (Utils.subText(length.getText().toString()) < Utils.subText(width.getText().toString())
                || Utils.subText(width.getText().toString()) < Utils.subText(height.getText().toString())) {
            dialogMsg = "零件长宽高不满足测量规则！";
        }

        if (TextUtils.isEmpty(allLength.getText())) {
            ToastUtil.show("叠加长度不能为空");
            return;
        }
        if (TextUtils.isEmpty(allWidth.getText())) {
            ToastUtil.show("叠加宽度不能为空");
            return;
        }
        if (TextUtils.isEmpty(allHeight.getText())) {
            ToastUtil.show("叠加高度不能为空");
            return;
        }

        if (Utils.subText(allLength.getText().toString()) < Utils.subText(allWidth.getText().toString())
                || Utils.subText(allWidth.getText().toString()) < Utils.subText(allHeight.getText().toString())) {
            dialogMsg = "叠加长宽高不满足测量规则！";
        }

        if (TextUtils.isEmpty(checkTv.getText())) {
            ToastUtil.show("数据核查不能为空");
            return;
        }

        if (TextUtils.isEmpty(recommendTv.getText())) {
            ToastUtil.show("工艺推荐不能为空");
            return;
        }

        CollectBean bean = new CollectBean();
        bean.setPartCode(number.getText().toString());
        bean.setPartName(name.getText().toString());
        bean.setPackageStypeName(wrapText.getText().toString());
        bean.setPartMaterialName(typeTxt.getText().toString());
        bean.setPackageModelCount(Integer.valueOf(modleNum.getText().toString()));

        if (outLayout.getVisibility() == View.VISIBLE) {
            bean.setPackageLength(Double.valueOf(Utils.subText(outLength.getText().toString())));
            bean.setPackageWidth(Double.valueOf(Utils.subText(outWidth.getText().toString())));
            bean.setPackageHeight(Double.valueOf(Utils.subText(outHeight.getText().toString())));
            bean.setRoughWeight(Double.valueOf(Utils.subText(outWeight.getText().toString())));
        }

        bean.setPartLength(Double.valueOf(Utils.subText(length.getText().toString())));
        bean.setPartWidth(Double.valueOf(Utils.subText(width.getText().toString())));
        bean.setPartHeigth(Double.valueOf(Utils.subText(height.getText().toString())));
        bean.setNetWeight(Double.valueOf(Utils.subText(weight.getText().toString())));

        if (singleLayout.getVisibility() == View.VISIBLE) {
            bean.setSinglePackageLength(Double.valueOf(Utils.subText(singleLength.getText().toString())));
            bean.setSinglePackageWidth(Double.valueOf(Utils.subText(singleWidth.getText().toString())));
            bean.setSinglePackageHeight(Double.valueOf(Utils.subText(singleHeight.getText().toString())));
            bean.setSinglePackageWeight(Double.valueOf(Utils.subText(singleWeight.getText().toString())));
        }

        bean.setAddedLength(Double.valueOf(Utils.subText(allLength.getText().toString())));
        bean.setAddedWidth(Double.valueOf(Utils.subText(allWidth.getText().toString())));
        bean.setAddedHeight(Double.valueOf(Utils.subText(allHeight.getText().toString())));

        bean.setSystemResource(source.getText().toString());
        bean.setAuditType(checkTv.getText().toString());
        bean.setProcessRecommendation(recommandChangeList.toString().replace("[", "").replace("]", ""));
        bean.setRemark(information.getText().toString());
        bean.setIsHistory(0);
        bean.setProject(User.getInstance().getUserInfo().getPerson().getProject());
        bean.setLocations(getLocation());

        List<String> pathList = new ArrayList<>();
        Observable.from(imageItems)
                .map(imageItem -> imageItem.path)
                .toList()
                .subscribe(list -> {
                    pathList.addAll(list);
                });

        if (TextUtils.isEmpty(dialogMsg)) {
            dialogMsg = "数据将上传到用户所在的项目组中";
        }

        new IOSDialog(this).builder()
                .setTitle("是否确定上传当前数据？")
                .setMsg(dialogMsg)
                .setPositiveButton("确定", v -> {
                    presenter.postCollectData(this, bean, pathList);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private String getLocation() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }

        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            String locationStr = "" + location.getLatitude() + ","
                    + location.getLongitude();
            return locationStr;
        }
        return "";
    }

    private void setEditTextChangedListener() {
        information.addTextChangedListener(new CustomTextWatcher(modleNum, drawable, false) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = information.getText().toString().length();
                if (num > 0) {
                    if (num < 100) {
                        infoNum.setText(num + "/100");
                        infoNum.setTextColor(getResources().getColor(R.color.nav_gray));
                    } else {
                        infoNum.setText("100/100");
                        infoNum.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                }
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.modle_num:
                if (!hasFocus) {
                    modleTv.setTextColor(normalColor);
                }
                break;

            case R.id.number:
                if (!hasFocus) {
                    numberTv.setTextColor(normalColor);
                    if (!TextUtils.isEmpty(number.getText().toString()))
                        presenter.getPartInfoByCode(this, number.getText().toString(), User.getInstance().getUserInfo().getPerson().getProject());
                }
                break;

            case R.id.length:
                setEditText(length, lengthTv, hasFocus);
                break;

            case R.id.width:
                setEditText(width, widthTv, hasFocus);
                break;

            case R.id.height:
                setEditText(height, hightTv, hasFocus);
                break;

            case R.id.weight:
                setEditText(weight, weightTv, hasFocus);
                break;

            case R.id.all_length:
                setEditText(allLength, allLengthTv, hasFocus);
                break;

            case R.id.all_width:
                setEditText(allWidth, allWidthTv, hasFocus);
                break;

            case R.id.all_height:
                setEditText(allHeight, allHeightTv, hasFocus);
                break;

            case R.id.out_length:
                setEditText(outLength, outLengthTv, hasFocus);
                break;

            case R.id.out_width:
                setEditText(outWidth, outWidthTv, hasFocus);
                break;

            case R.id.out_height:
                setEditText(outHeight, outHeightTv, hasFocus);
                break;

            case R.id.single_weight:
                setEditText(singleWeight, singleWeightTv, hasFocus);
                break;

            case R.id.single_length:
                setEditText(singleLength, singleLengthTv, hasFocus);
                break;

            case R.id.single_width:
                setEditText(singleWidth, singleWidthTv, hasFocus);
                break;

            case R.id.single_height:
                setEditText(singleHeight, singleHeightTv, hasFocus);
                break;
        }
        clearImg(v);
        if (hasFocus){
            v.post(new Runnable() {
                @Override
                public void run() {
                    ((EditText)v).setSelection(((EditText) v).getText().length());
                }
            });

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.modle_num:
                    changeUI(event, modleNum, modleTv);
                    break;

                case R.id.number:
                    changeUI(event, number, numberTv);
                    break;

                case R.id.length:
                    changeUI(event, length, lengthTv);
                    break;

                case R.id.width:
                    changeUI(event, width, widthTv);
                    break;

                case R.id.height:
                    changeUI(event, height, hightTv);
                    break;

                case R.id.weight:
                    changeUI(event, weight, weightTv);
                    break;

                case R.id.all_length:
                    changeUI(event, allLength, allLengthTv);
                    break;

                case R.id.all_width:
                    changeUI(event, allWidth, allWidthTv);
                    break;

                case R.id.all_height:
                    changeUI(event, allHeight, allHeightTv);
                    break;

                case R.id.out_length:
                    changeUI(event, outLength, outLengthTv);
                    break;

                case R.id.out_width:
                    changeUI(event, outWidth, outWidthTv);
                    break;

                case R.id.out_height:
                    changeUI(event, outHeight, outHeightTv);
                    break;

                case R.id.single_weight:
                    changeUI(event, singleWeight, singleWeightTv);
                    break;

                case R.id.single_length:
                    changeUI(event, singleLength, singleLengthTv);
                    break;

                case R.id.single_width:
                    changeUI(event, singleWidth, singleWidthTv);
                    break;

                case R.id.single_height:
                    changeUI(event, singleHeight, singleHeightTv);
                    break;
            }
        }

        v.post(new Runnable() {
            @Override
            public void run() {
                ((EditText)v).setSelection(((EditText) v).getText().length());
            }
        });

        return false;
    }

    /**
     * 获取焦点和失去焦点事件
     * @param et
     * @param tv
     * @param hasFocus
     */
    private void setEditText(EditText et, TextView tv, boolean hasFocus) {
        if (TextUtils.equals("净重", tv.getText().toString()) ||
                TextUtils.equals("毛重", tv.getText().toString())) {
            if (!hasFocus) {
                tv.setTextColor(normalColor);
                if (et.getText().length() > 0) {
                    et.setText(et.getText() + "kg");
                } else {
                    et.setHint("kg");
                }
            } else {
                if (et.getText().length() > 0) {
                    String s = et.getText().toString();
                    int index = s.indexOf("kg");
                    String temp = s.substring(0, index);
                    et.setText(temp);
                }
            }
            return;
        }
        if (!hasFocus) {
            tv.setTextColor(normalColor);
            if (et.getText().length() > 0) {
                et.setText(et.getText() + "mm");
            } else {
                et.setHint("mm");
            }
        } else {
            if (et.getText().length() > 0) {
                String s = et.getText().toString();
                int index = s.indexOf("mm");
                String temp = s.substring(0, index);
                et.setText(temp);
            }
        }
    }

    /**
     * 失去焦点 去除右边 x 号
     * @param view
     */
    private void clearImg(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

    }

    /**
     * EditText 获取焦点，改变字体颜色
     * @param event
     * @param et
     * @param tv
     */
    private void changeUI(MotionEvent event, EditText et, TextView tv) {
        if ((event.getX() > et.getWidth() - et.getPaddingRight() - drawable.getIntrinsicWidth())
                && (et.getCompoundDrawables()[2] != null) && (et.getCompoundDrawables()[2].isVisible())) {
            et.setText("");
        }
        if (tv.getCurrentTextColor() != checkedColor) {
            showSoftKeyboard(et, CollectActivity.this);
            tv.setTextColor(checkedColor);
            if (et.getText().length() > 0) {
                et.setCompoundDrawablePadding(20);
                et.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                et.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    /**
     * 照相图片选择
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(10);
    }

    /**
     * scrollView 获取焦点，所有edittext 失去焦点
     * @return
     */
    private void clearFocus(){
        number.clearFocus();
        modleNum.clearFocus();
        length.clearFocus();
        width.clearFocus();
        height.clearFocus();
        weight.clearFocus();
        outLength.clearFocus();
        outWidth.clearFocus();
        outHeight.clearFocus();
        outWeight.clearFocus();
        singleLength.clearFocus();
        singleWidth.clearFocus();
        singleHeight.clearFocus();
        singleWeight.clearFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                actionScan();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailure() {
        ToastUtil.show("上传失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
