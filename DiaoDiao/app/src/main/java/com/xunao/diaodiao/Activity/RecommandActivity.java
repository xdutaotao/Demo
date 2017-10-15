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
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.EvaluateReq;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Present.SuggestPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.SuggestView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class RecommandActivity extends BaseActivity implements SuggestView {

    @Inject
    SuggestPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.percent)
    TextView percent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.content_num)
    TextView contentNum;
    @BindView(R.id.type_recycler_view)
    RecyclerView typeRecyclerView;
    private int projectId;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<TypeInfoRes.Type_Info> typeAdapter;
    private int projectType;

    private List<String> skillsName = new ArrayList<>();

    public static void startActivity(Context context, int id, int projType) {
        Intent intent = new Intent(context, RecommandActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("PROJECTTYPE", projType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        showToolbarBack(toolBar, titleText, "我的评价");

        projectId = getIntent().getIntExtra(INTENT_KEY, 0);
        projectType = getIntent().getIntExtra("PROJECTTYPE", 0);
        post.setOnClickListener(v -> {
            if (content.getText().length() == 0) {
                ToastUtil.show("请输入内容");
                return;
            }
            EvaluateReq req = new EvaluateReq();
            req.setContent(content.getText().toString());
            req.setProject_id(projectId);
            req.setProject_type(projectType);
            req.setPoint(percent.getText().toString());
            req.setImages(pathList);
            presenter.toEvaluate(this, req);
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentNum.setText(s.toString().toString().length() + " / 200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                percent.setText(String.valueOf(rating));
            }
        });

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
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


        recyclerView.setAdapter(adapter);
        initImagePicker();
        adapter.add(ADD);



        typeAdapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info s) {
                baseViewHolder.setText(R.id.skill_text, s.getTitle());

                if (skillsName.toString().contains(s.getId())){
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                }else{
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    if (skillsName.toString().contains(s.getId())){
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView)v).setTextColor(getResources().getColor(R.color.gray));
                        skillsName.remove(s.getId());
                    }else{
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView)v).setTextColor(Color.WHITE);
                        skillsName.add(s.getId());
                    }
                });
            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        typeRecyclerView.setLayoutManager(linearLayoutManager);
        typeRecyclerView.setAdapter(typeAdapter);

        presenter.getTypeInfo(this);
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
    public void onFailure() {

    }

    @Override
    public void getData(String s) {
        ToastUtil.show("提交成功");
        finish();
    }

    @Override
    public void getData(TypeInfoRes s) {
        typeAdapter.addAll(s.getType_info());
    }
}
