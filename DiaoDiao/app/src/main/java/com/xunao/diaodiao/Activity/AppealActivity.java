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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Present.AppealPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AppealView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * create by
 */
public class AppealActivity extends BaseActivity implements AppealView {

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

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;

    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private String[] reasonList = {"工程质量不合格", "没完工"};
    private static final String ADD = "add";
    private static final int IMAGE_PICKER = 8888;
    List<String> pathList = new ArrayList<>();

    private List<String> skillsName = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AppealActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "申诉");

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

        adapter.setOnItemClickListener((view, i) -> {
            String skillItem = adapter.getAllData().get(i);
            if (skillsName.toString().contains(skillItem)) {
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsName.remove(skillItem);
            } else {
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsName.add(skillItem);
            }

        });


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
                contentNum.setText(s.length() + " / 200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post.setOnClickListener(v -> {
            if (content.getText().toString().length() == 0){
                ToastUtil.show("请填写原因");
                return;
            }


        });

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
        Observable.from(images)
                .map(imageItem -> imageItem.path)
                .toList()
                .subscribe(strings -> {
                    imageAdapter.clear();
                    imageAdapter.addAll(strings);
                    if (strings.size() != 10) {
                        imageAdapter.add(ADD);
                    }
                    pathList.clear();
                    pathList.addAll(strings);
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
