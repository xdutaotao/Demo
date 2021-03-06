package com.xunao.diaodiao.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.CollectBean;
import com.xunao.diaodiao.Bean.NumberBean;
import com.xunao.diaodiao.Bean.PackageBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.CollectPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.CollectView;
import com.xunao.diaodiao.View.SystemBarTintManager;
import com.xunao.diaodiao.Widget.CustomTextWatcher;
import com.xunao.diaodiao.Widget.GlideImageLoader;
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

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

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
    public static final int REQUEST_SCAN_CODE = 2223;

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

    @BindView(R.id.name_layout)
    RelativeLayout nameLayout;

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

    private boolean isNumberSearch = false;
    private boolean numHasFocus = false;
    private RecyclerArrayAdapter<String> recyclerArrayAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String data) {
        Intent intent = new Intent(context, CollectActivity.class);
        intent.putExtra("SCAN_KEY", data);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, CollectBean bean) {
        Intent intent = new Intent(context, CollectActivity.class);
        intent.putExtra(INTENT_KEY, (Serializable) bean);
        context.startActivityForResult(intent, CollectHistoryActivity.REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //ProcessingNoticationbar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "采集");

        checkedColor = getResources().getColor(R.color.colorPrimary);
        normalColor = getResources().getColor(R.color.black);
        //drawable = getResources().getDrawable(R.drawable.cancle);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.select_photo_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        };
        recyclerView.setAdapter(adapter);
        submit.setOnClickListener(this);

        scrollView.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_MOVE && !numHasFocus) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }else{
                v.getParent().requestDisallowInterceptTouchEvent(false);
                scrollView.clearFocus();
            }

//                scrollView.setFocusable(true);
//                scrollView.setFocusableInTouchMode(true);
//                scrollView.requestLayout();
//                clearFocus();
            return false;
        });

        if (getIntent().getSerializableExtra(INTENT_KEY) == null){
            adapter.add(ADD);
            adapter.setOnItemClickListener((view, i) -> {
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

            outWeight.setOnTouchListener(this);
            outWeight.setOnFocusChangeListener(this);
            outWeight.addTextChangedListener(new CustomTextWatcher(outWeight, drawable, true));

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
                if (resultBean != null)
                    CheckActivity.startActivityForResult(this, resultBean.getAts(), checkTv.getText().toString());
            });


            recommendLayout.setOnClickListener(v -> {
                //if (resultBean != null)
                    //RecommandActivity.startActivityForResult(this, resultBean.getPmts(), resultBean.getPms(), recommandChangeList);
            });
            presenter.packagingForm();
        }else{
            collectBean = (CollectBean) getIntent().getSerializableExtra(INTENT_KEY);
            number.setText(collectBean.getPartCode());
            name.setText(collectBean.getPartName());
            source.setText(collectBean.getSystemResource());
            wrapText.setText(collectBean.getPackageStypeName());
            typeTxt.setText(collectBean.getPartMaterialName());
            modleNum.setText(collectBean.getPackageModelCount()+"");

            outLayout.setVisibility(View.VISIBLE);
            singleSwitch.setChecked(true);

            length.setText(Utils.formatData(collectBean.getPartLength())+"mm");
            width.setText(Utils.formatData(collectBean.getPartWidth())+"mm");
            height.setText(Utils.formatData(collectBean.getPartHeigth())+"mm");
            weight.setText(Utils.formatData(collectBean.getNetWeight())+"kg");

            outLength.setText(Utils.formatData(collectBean.getPackageLength())+"mm");
            outWidth.setText(Utils.formatData(collectBean.getPackageWidth())+"mm");
            outHeight.setText(Utils.formatData(collectBean.getPackageHeight())+"mm");
            outWeight.setText(Utils.formatData(collectBean.getRoughWeight())+"kg");

            singleLength.setText(Utils.formatData(collectBean.getSinglePackageLength())+"mm");
            singleWidth.setText(Utils.formatData(collectBean.getSinglePackageWidth())+"mm");
            singleHeight.setText(Utils.formatData(collectBean.getSinglePackageHeight())+"mm");
            singleWeight.setText(Utils.formatData(collectBean.getSinglePackageWeight())+"kg");

            allLength.setText(Utils.formatData(collectBean.getAddedLength())+"mm");
            allWidth.setText(Utils.formatData(collectBean.getAddedWidth())+"mm");
            allHeight.setText(Utils.formatData(collectBean.getAddedHeight())+"mm");

            checkTv.setText(collectBean.getAuditType());
            recommendTv.setText(collectBean.getProcessRecommendation());
            information.setText(collectBean.getRemark());
            infoNum.setText(information.getText().length()+"/100");
            historyPathList.addAll(Arrays.asList(collectBean.getDocumentCodes().split(",")));
            adapter.addAll(historyPathList);
//            if (historyPathList.size() < 10){
//                adapter.add(ADD);
//            }

            singleSwitch.setEnabled(false);

            number.setFocusable(false);
            number.setEnabled(false);
            modleNum.setFocusable(false);
            modleNum.setEnabled(false);
            length.setFocusable(false);
            length.setEnabled(false);
            height.setFocusable(false);
            height.setEnabled(false);

            width.setFocusable(false);
            width.setEnabled(false);
            weight.setFocusable(false);
            weight.setEnabled(false);
            allLength.setFocusable(false);
            allLength.setEnabled(false);
            allWidth.setFocusable(false);
            allWidth.setEnabled(false);

            allHeight.setFocusable(false);
            allHeight.setEnabled(false);
            singleLength.setFocusable(false);
            singleLength.setEnabled(false);
            singleHeight.setFocusable(false);
            singleHeight.setEnabled(false);
            singleWidth.setFocusable(false);
            singleWidth.setEnabled(false);

            singleWeight.setFocusable(false);
            singleWeight.setEnabled(false);
            outWidth.setFocusable(false);
            outWidth.setEnabled(false);
            outHeight.setFocusable(false);
            outHeight.setEnabled(false);
            outLength.setFocusable(false);
            outLength.setEnabled(false);
            outWeight.setFocusable(false);
            outWeight.setEnabled(false);

            information.setFocusable(false);
            information.setEnabled(false);



        }

        number.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId==EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                clearData();
                number.clearFocus();
                isNumberSearch = true;
                return true;
            }else{
                return false;
            }
        });

        name.setOnClickListener(v -> {
            if (TextUtils.isEmpty(name.getText().toString())){
                return;
            }else{
                new IOSDialog(CollectActivity.this).builder()
                        .setTitle("详情")
                        .setMsg(name.getText().toString())
                        .setNegativeButton("确定", null)
                        .builder();
            }
        });

        source.setOnClickListener(v -> {
            if (TextUtils.isEmpty(source.getText().toString())){
                return;
            }else{
                new IOSDialog(CollectActivity.this).builder()
                        .setTitle("详情")
                        .setMsg(source.getText().toString())
                        .setNegativeButton("确定", null)
                        .builder();
            }
        });

        nameLayout.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(name.getText().toString())){
                new IOSDialog(CollectActivity.this).builder()
                        .setTitle("详情")
                        .setMsg(name.getText().toString())
                        .setPositiveButton("确定", null)
                        .show();
            }
        });

        name.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(name.getText().toString())){
                new IOSDialog(CollectActivity.this).builder()
                        .setTitle("详情")
                        .setMsg(name.getText().toString())
                        .setPositiveButton("确定", null)
                        .show();
            }
        });

        recommendTv.setOnClickListener(v -> {
            String recommandData = recommendTv.getText().toString();
            if (!TextUtils.isEmpty(recommandData)){
                View recommandView = LayoutInflater.from(CollectActivity.this)
                        .inflate(R.layout.recommand_list, null);
                RecyclerView recyclerView = (RecyclerView) recommandView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(CollectActivity.this));
                recyclerArrayAdapter.clear();
                recyclerArrayAdapter.addAll(recommandData.split(","));
                recyclerView.setAdapter(recyclerArrayAdapter);

                new IOSDialog(CollectActivity.this).builder()
                        .setTitle("详情")
                        .setContentView(recommandView)
                        .setPositiveButton("确定", null)
                        .show();
            }

        });

        recyclerArrayAdapter =
                new RecyclerArrayAdapter<String>(CollectActivity.this, R.layout.recommand_item_text) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, String s) {
                        baseViewHolder.setText(R.id.recommend_item_text, s);
                    }
                };
    }

    private void clearData(){
        //number.setText("");
        name.setText("");
        source.setText("");
        wrapText.setText("");
        typeTxt.setText("");
        modleNum.setText("");
        outLength.setText("");
        outHeight.setText("");
        outWeight.setText("");
        outWidth.setText("");

        singleHeight.setText("");
        singleLength.setText("");
        singleWeight.setText("");
        singleWidth.setText("");


        length.setText("");
        width.setText("");
        height.setText("");
        weight.setText("");

        allHeight.setText("");
        allLength.setText("");
        allWidth.setText("");

        checkTv.setText("");
        information.setText("");
        recommendTv.setText("");
        infoNum.setText("0/100");

        adapter.clear();
        adapter.add(ADD);
    }

    @Override
    public void getData(PackageBean.ResultBean bean) {
        resultBean = bean;
    }

    @Override
    public void getPostTxt(String s) {
        ToastUtil.show("上传成功");

        scrollView.scrollTo(0, 0);
        number.setText("");
        clearData();
    }

    @Override
    public void getSearchData(NumberBean s) {
        name.setText("");
        source.setText("");
        if (s.getData() != null) {
            name.setText(s.getData().getCnName());
            source.setText(s.getData().getSourceDistribution());
        }

        if (s.getHasOldData() == 1 && s.getOldData() != null && isNumberSearch) {
            clearData();
            isNumberSearch = false;
            new IOSDialog(this).builder()
                    .setTitle("该零件号已被"+s.getOldData().getCollectorName()+"于"+Utils.strToDateLong(s.getOldData().getDateline()*1000) + "采集!")
                    .setMsg("是否重新采集当前数据?")
                    .setPositiveButton("确定", v -> {
                        wrapText.setText(s.getOldData().getPackageStypeName());
                        typeTxt.setText(s.getOldData().getPartMaterialName());
                        modleNum.setText(s.getOldData().getPackageModelCount() + "");
                        clearImg(modleNum);
                        length.setText(Utils.formatData(s.getOldData().getPartLength()) +"mm");
                        clearImg(length);
                        width.setText(Utils.formatData(s.getOldData().getPartWidth())+"mm");
                        clearImg(width);
                        height.setText(Utils.formatData(s.getOldData().getPartHeight())+"mm");
                        clearImg(height);
                        weight.setText(Utils.formatData(s.getOldData().getNetWeight())+"kg");
                        clearImg(weight);

                        if (Math.abs(s.getOldData().getAddedLength()) > 0.0001 ||
                                Math.abs(s.getOldData().getAddedWidth()) > 0.0001 ||
                                    Math.abs(s.getOldData().getAddedHeight()) > 0.0001){
                            outLayout.setVisibility(View.VISIBLE);
                        }

                        if (Math.abs(s.getOldData().getSinglePackageLength()) > 0.0001 ||
                                Math.abs(s.getOldData().getSinglePackageWidth()) > 0.0001 ||
                                    Math.abs(s.getOldData().getSinglePackageHeight()) > 0.0001 ||
                                        Math.abs(s.getOldData().getSinglePackageWeight()) > 0.0001){
                            singleSwitch.setChecked(true);
                        }

                        allLength.setText(Utils.formatData(s.getOldData().getAddedLength()) + "mm");
                        clearImg(allLength);
                        allWidth.setText(Utils.formatData(s.getOldData().getAddedWidth()) + "mm");
                        clearImg(allWidth);
                        allHeight.setText(Utils.formatData(s.getOldData().getAddedHeight()) + "mm");
                        clearImg(allHeight);

                        outLength.setText(Utils.formatData(s.getOldData().getPackageLength())+"mm");
                        clearImg(outLength);
                        outWidth.setText(Utils.formatData(s.getOldData().getPackageWidth())+"mm");
                        clearImg(outWidth);
                        outHeight.setText(Utils.formatData(s.getOldData().getPackageHeight())+"mm");
                        clearImg(outHeight);
                        outWeight.setText(Utils.formatData(s.getOldData().getRoughWeight())+"kg");
                        clearImg(outWeight);

                        singleLength.setText(Utils.formatData(s.getOldData().getSinglePackageLength())+"mm");
                        clearImg(singleLength);
                        singleWidth.setText(Utils.formatData(s.getOldData().getSinglePackageWidth())+"mm");
                        clearImg(singleWidth);
                        singleHeight.setText(Utils.formatData(s.getOldData().getSinglePackageHeight())+"mm");
                        clearImg(singleHeight);
                        singleWeight.setText(Utils.formatData(s.getOldData().getSinglePackageWeight())+"kg");
                        clearImg(singleWeight);
                        singleSwitch.setChecked(true);

                        checkTv.setText(s.getOldData().getAuditType());
                        recommendTv.setText(s.getOldData().getProcessRecommendation());
                        information.setText(s.getOldData().getRemark());
                        List<String> paths = Arrays.asList(s.getOldData().getDocumentCodes().split(","));
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
                    })
                    .setNegativeButton("取消", v -> {
                        number.setText("");
                    })
                    .show();

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
            }else if (data != null && requestCode == REQUEST_SCAN_CODE){
                String result = data.getStringExtra(INTENT_KEY);
                number.setText(result);

            }
        }
    }

    @Override
    public void postTxtError(){
        scrollView.scrollTo(0, 0);
        number.setText("");
        clearData();
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
                if (resultBean != null)
                    WrapActivity.startActivityForResult(this, resultBean.getPss(), wrapText.getText().toString());
                break;

            case R.id.type_layout:
                if (resultBean != null)
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

        if (imageItems.size() <= 3){
            ToastUtil.show("照片数量要大于3个");
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
            if (TextUtils.isEmpty(outWeight.getText())) {
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

        if (outLayout.getVisibility() == View.VISIBLE &&
                singleLayout.getVisibility() == View.GONE){
            if ((Utils.subText(outLength.getText().toString()) <  Utils.subText(length.getText().toString())) ||
                    (Utils.subText(outHeight.getText().toString()) <  Utils.subText(height.getText().toString())) ||
                    (Utils.subText(outWidth.getText().toString()) <  Utils.subText(width.getText().toString())) ||
                    (Utils.subText(outWeight.getText().toString()) <  Utils.subText(weight.getText().toString()))
                    ){
                ToastUtil.show("外包装应大于零件包装");
                return;
            }
        }

        if (outLayout.getVisibility() == View.VISIBLE && singleLayout.getVisibility() == View.VISIBLE){
            if ((Utils.subText(outLength.getText().toString()) < Utils.subText(singleLength.getText().toString())) ||
                    (Utils.subText(singleLength.getText().toString()) < Utils.subText(length.getText().toString())) ||
                    (Utils.subText(outHeight.getText().toString()) < Utils.subText(singleHeight.getText().toString())) ||
                    (Utils.subText(singleHeight.getText().toString()) < Utils.subText(height.getText().toString())) ||
                    (Utils.subText(outWidth.getText().toString()) < Utils.subText(singleWidth.getText().toString())) ||
                    (Utils.subText(singleWidth.getText().toString()) < Utils.subText(width.getText().toString())) ||
                    (Utils.subText(outWeight.getText().toString()) < Utils.subText(singleWeight.getText().toString())) ||
                    (Utils.subText(singleWeight.getText().toString()) < Utils.subText(weight.getText().toString()))
                    ){
                ToastUtil.show("外包装大于单个包装，单个包装大于零件包装");
                return;
            }
        }

        CollectBean bean = new CollectBean();
        bean.setPartCode(number.getText().toString());
        bean.setPartName(name.getText().toString());
        bean.setPackageStypeName(wrapText.getText().toString());
        bean.setPartMaterialName(typeTxt.getText().toString());
        bean.setPackageModelCount(Integer.valueOf(modleNum.getText().toString()));

        if (outLayout.getVisibility() == View.VISIBLE) {
            bean.setPackageLength(Float.valueOf(Utils.subText(outLength.getText().toString())));
            bean.setPackageWidth(Float.valueOf(Utils.subText(outWidth.getText().toString())));
            bean.setPackageHeight(Float.valueOf(Utils.subText(outHeight.getText().toString())));
            bean.setRoughWeight(Float.valueOf(Utils.subText(outWeight.getText().toString())));
        }

        bean.setPartLength(Float.valueOf(Utils.subText(length.getText().toString())));
        bean.setPartWidth(Float.valueOf(Utils.subText(width.getText().toString())));
        bean.setPartHeigth(Float.valueOf(Utils.subText(height.getText().toString())));
        bean.setNetWeight(Float.valueOf(Utils.subText(weight.getText().toString())));

        if (singleLayout.getVisibility() == View.VISIBLE) {
            bean.setSinglePackageLength(Float.valueOf(Utils.subText(singleLength.getText().toString())));
            bean.setSinglePackageWidth(Float.valueOf(Utils.subText(singleWidth.getText().toString())));
            bean.setSinglePackageHeight(Float.valueOf(Utils.subText(singleHeight.getText().toString())));
            bean.setSinglePackageWeight(Float.valueOf(Utils.subText(singleWeight.getText().toString())));
        }

        bean.setAddedLength(Float.valueOf(Utils.subText(allLength.getText().toString())));
        bean.setAddedWidth(Float.valueOf(Utils.subText(allWidth.getText().toString())));
        bean.setAddedHeight(Float.valueOf(Utils.subText(allHeight.getText().toString())));

        bean.setSystemResource(source.getText().toString());
        bean.setAuditType(checkTv.getText().toString());
        bean.setProcessRecommendation(recommandChangeList.toString().replace("[", "").replace("]", ""));
        bean.setRemark(information.getText().toString());
        bean.setIsHistory(0);
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

                    StringBuilder sb = new StringBuilder();
                    Observable.from(pathList)
                            .map(s -> {
                                sb.append(s).append(",");
                                String paths = sb.toString().substring(0, sb.toString().length()-1);
                                return paths;
                            })
                            .subscribe(s -> bean.setDocumentCodes(s));
                    if (TextUtils.isEmpty(bean.getTime()))
                        bean.setTime(Utils.getNowTime());
                    presenter.postCollectData(this, bean, pathList);

                })
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
                    if (!TextUtils.isEmpty(number.getText().toString())){
                        if (!isNumberSearch && numHasFocus) {
                            numHasFocus = false;
                            clearData();
                        }
                    }else{
                        clearData();
                    }
                }else{
                    v.getParent().requestDisallowInterceptTouchEvent(true);
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

            case R.id.out_weight:
                setEditText(outWeight, outWeightTv, hasFocus);
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

        if (v.getId() != R.id.number){
            numHasFocus = false;
            if (hasFocus){
                isNumberSearch = false;
            }
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
                    numHasFocus = true;
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
                TextUtils.equals("毛重", tv.getText().toString()) ||
                    TextUtils.equals("单个包装重", tv.getText().toString())) {
            if (!hasFocus) {
                tv.setTextColor(normalColor);
                if (et.getText().length() > 0) {
                    InputFilter[] filters = {new InputFilter.LengthFilter(et.getText().length()+2)};
                    et.setFilters(filters);
                    int length = et.getText().length();
                    if (TextUtils.equals(et.getText().subSequence(length-1, length), ".")){
                        et.setText(et.getText().subSequence(0, length-1) + "kg");
                    }else{
                        et.setText(et.getText() + "kg");
                    }

                } else {
                    et.setHint("kg");
                }
            } else {
                if (et.getText().length() > 0) {
                    String s = et.getText().toString();
                    int index = s.indexOf("kg");
                    if (index > 0){
                        String temp = s.substring(0, index);
                        et.setText(temp);
                    }

                }
            }
            return;
        }
        if (!hasFocus) {
            tv.setTextColor(normalColor);
            if (et.getText().length() > 0) {
                InputFilter[] filters = {new InputFilter.LengthFilter(et.getText().length()+2)};
                et.setFilters(filters);

                int length = et.getText().length();
                if (TextUtils.equals(et.getText().subSequence(length-1, length), ".")){
                    et.setText(et.getText().subSequence(0, length-1) + "mm");
                }else{
                    et.setText(et.getText() + "mm");
                }

            } else {
                et.setHint("mm");
            }
        } else {
            if (et.getText().length() > 0) {
                String s = et.getText().toString();
                int index = s.indexOf("mm");
                if (index > 0){
                    String temp = s.substring(0, index);
                    et.setText(temp);
                }

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
    public void onFailure() {
        clearData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void ProcessingNoticationbar() {
        setTranslucentStatus(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorAccent);//通知栏所需颜色
    }
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
