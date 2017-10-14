package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Present.AppealPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.AppealView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.COMPANY_PROJECT_NO_PASS;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.NO_PASS;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_NO_PASS;

/**
 * create by
 */
public class AppealActivity extends BaseActivity implements AppealView, CompoundButton.OnCheckedChangeListener {

    @Inject
    AppealPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view_reason)
    RecyclerView recyclerViewReason;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.first)
    CheckBox first;
    @BindView(R.id.second)
    CheckBox second;
    @BindView(R.id.appeal_type)
    LinearLayout appealType;

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;
    public static final String APPEAL = "appeal";

    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private String[] reasonList = {"工程质量不合格", "没完工"};
    private static final String ADD = "add";
    private static final int IMAGE_PICKER = 8888;
    List<String> pathList = new ArrayList<>();

    private List<String> skillsName = new ArrayList<>();
    private GetMoneyReq req;
    private int who;

    public static void startActivity(Context context, GetMoneyReq req, int who) {
        Intent intent = new Intent(context, AppealActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("WHO", who);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        req = (GetMoneyReq) getIntent().getSerializableExtra(INTENT_KEY);
        who = getIntent().getIntExtra("WHO", 0);
        if (who == SKILL_RELEASE_LINGGONG_NO_PASS) {
            showToolbarBack(toolBar, titleText, "输入不通过原因");
            post.setText("提交审核");
            reason.setText("不通过的原因");
            appealType.setVisibility(View.GONE);
            content.setHint("请输入不通过原因");
        } else if (who == NO_PASS){
            showToolbarBack(toolBar, titleText, "申诉");
            appealType.setVisibility(View.VISIBLE);
            first.setOnCheckedChangeListener(this);
            second.setOnCheckedChangeListener(this);
        }else if(who == COMPANY_PROJECT_NO_PASS){
            showToolbarBack(toolBar, titleText, "输入不通过原因");
            post.setText("提交审核");
            reason.setText("不通过的原因");
            appealType.setVisibility(View.GONE);
            content.setHint("请输入不通过原因");
        }

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsName.toString().contains(s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    if (skillsName.toString().contains(s)) {
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
                        skillsName.remove(s);
                    } else {
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView) v).setTextColor(Color.WHITE);
                        skillsName.add(s);
                    }
                });
            }
        };


        imageAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)) {
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageResource(R.id.image, R.drawable.icon_paishe);
                } else {
                    baseViewHolder.setVisible(R.id.delete, true);
                    baseViewHolder.setImageUrl(R.id.image, s);
                }
            }
        };

        imageAdapter.setOnItemClickListener((view, i) -> {
            view.findViewById(R.id.delete).setOnClickListener(v -> {
                imageItems.remove(i);
                imageAdapter.remove(i);
                if (!imageAdapter.getAllData().contains(ADD)) {
                    imageAdapter.add(ADD);
                }
            });

            if (TextUtils.equals(imageAdapter.getAllData().get(i), ADD)) {
                selectPhoto();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewReason.setLayoutManager(manager);
        recyclerViewReason.setAdapter(adapter);

        recyclerView.setAdapter(imageAdapter);
        imageAdapter.add(ADD);
        initImagePicker();

        adapter.addAll(reasonList);


        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 200){
                    content.setText(s.subSequence(0, 200));
                    contentNum.setText("200 / 200");
                }else{
                    contentNum.setText(s.length() + " / 200");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post.setOnClickListener(v -> {
            if (content.getText().toString().length() == 0) {
                ToastUtil.show("请填写原因");
                return;
            }

            if (who == NO_PASS){
                req.setContent(content.getText().toString());

            }else if (who == SKILL_RELEASE_LINGGONG_NO_PASS){
                req.setReason(content.getText().toString());
            } else if(who == COMPANY_PROJECT_NO_PASS){
                req.setReason(content.getText().toString());
            }

            req.setImages(pathList);

            presenter.myProjectWorkFail(AppealActivity.this, req, who);
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked){
            return;
        }

        switch (buttonView.getId()){
            case R.id.first:
                first.setChecked(isChecked);
                second.setChecked(!isChecked);
                break;

            case R.id.second:
                first.setChecked(!isChecked);
                second.setChecked(isChecked);
                break;
        }
        req.setAppeal_operate(buttonView.getText().toString());

    }

    @Override
    public void getData(String s) {
        ToastUtil.show(s);
        RxBus.getInstance().post("appeal");
        finish();
    }

    private void selectPhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, IMAGE_PICKER);
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
                    imageAdapter.clear();
                    imageAdapter.addAll(strings);
                    if (strings.size() != 10) {
                        imageAdapter.add(ADD);
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
