package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.cworker.Present.CollectPresenter;
import com.demo.cworker.R;
import com.demo.cworker.Utils.LogUtils;
import com.demo.cworker.Utils.ToastUtil;
import com.demo.cworker.View.CollectView;
import com.demo.cworker.Widget.CustomTextWatcher;
import com.demo.cworker.Widget.GlideImageLoader;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class CollectActivity extends BaseActivity implements CollectView, View.OnClickListener, View.OnLayoutChangeListener {
    private static final String ADD = "add";
    private static final int IMAGE_PICKER = 8888;
    public static final int REQUEST_CODE = 6666;
    public static final int REQUEST_WRAP_CODE = 1111;
    public static final int REQUEST_TYPE_CODE = 1112;
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

    private RecyclerArrayAdapter<String> adapter;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private View activityRootView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectActivity.class);
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

        activityRootView = findViewById(android.R.id.content);

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

        recyclerView.setAdapter(adapter);
        submit.setOnClickListener(this);
        adapter.add(ADD);
        initImagePicker();

        information.addTextChangedListener(new CustomTextWatcher() {
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



        modleNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawable = modleNum.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                LogUtils.d("getx" + event.getX());
                LogUtils.d("modleNum" + (modleNum.getWidth()
                        - modleNum.getPaddingRight()
                        - drawable.getIntrinsicWidth()));

                if (event.getX() > modleNum.getWidth()
                        - modleNum.getPaddingRight()){
                    modleNum.setText("");
                }
                return false;
            }
        });

        modleNum.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (modleNum.getText().length() > 0){
                    modleNum.setCompoundDrawablePadding(20);
                    modleNum.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.cancle), null);
                }else{
                    modleNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });

        modleNum.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        modleLayout.setOnClickListener(this);
        modleNum.setOnClickListener(this);
        wrapLayout.setOnClickListener(this);
        typeLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modleNum.addOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (v instanceof AppCompatEditText){
            ToastUtil.show(((EditText)v).getText().toString());
        }
        //ToastUtil.show("3333");
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
                wrapText.setText(data.getStringExtra(INTENT_KEY));
            } else if (data != null && requestCode == REQUEST_TAKE_PHOTO_CODE) {
                ArrayList<ImageItem> list = (ArrayList<ImageItem>) data.getSerializableExtra(INTENT_KEY);
                imageItems.addAll(list);
                setResultToAdapter(imageItems);
            } else if (data != null && requestCode == REQUEST_TYPE_CODE) {
                String s = data.getStringExtra(INTENT_KEY);
                typeTxt.setText(s);
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

                break;

            case R.id.wrap_layout:
                WrapActivity.startActivityForResult(this, wrapText.getText().toString());
                break;

            case R.id.type_layout:
                TypeActivity.startActivityForResult(this, typeTxt.getText().toString());
                break;

            case R.id.modle_layout:
            case R.id.modle_num:
                ToastUtil.show("dddd");
                modleTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
