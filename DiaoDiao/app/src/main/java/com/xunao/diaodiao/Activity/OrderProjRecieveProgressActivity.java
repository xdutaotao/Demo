package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyAcceptOddSubmitReq;
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderProjRecieveProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderProjRecieveProgressView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.address;

/**
 * create by
 */
public class OrderProjRecieveProgressActivity extends BaseActivity implements OrderProjRecieveProgressView {

    @Inject
    OrderProjRecieveProgressPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.give_money)
    TextView giveMoney;
    @BindView(R.id.recycler_view_layout)
    RelativeLayout recyclerViewLayout;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.recycler_view_image)
    RecyclerView recyclerViewImage;
    @BindView(R.id.sign_layout)
    LinearLayout signLayout;
    @BindView(R.id.post)
    TextView post;
    @BindView(R.id.apply_money)
    TextView applyMoney;

    private RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;
    private LinearLayoutManager manager;

    private RecyclerArrayAdapter<String> signAdapter;
    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    LinearLayoutManager linearLayoutManager ;
    private GetMoneyReq req = new GetMoneyReq();

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, OrderProjRecieveProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_proj_progress);
        setContentView(R.layout.skill_project_recieve_progress);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目进度");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        adapter = new RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean>(this, R.layout.project_recieve_progress_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyPublishOddWorkRes.WorkBean workBean) {
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " "
                        + workBean.getRemark());
                baseViewHolder.setText(R.id.address, workBean.getLocation());
                String content = "";
                if (workBean.getPass() == 1){
                    //审核通过
                    if (workBean.getPaid() == 1){
                        //已打款
                        baseViewHolder.setText(R.id.content, "已打款");
                    }else if (workBean.getPaid() == 2){
                        //未打款
                        baseViewHolder.setText(R.id.content, "未打款");
                    }else{
                        baseViewHolder.setText(R.id.content, "部分打款");
                    }

                }else if (workBean.getPass() == 2){
                    baseViewHolder.setText(R.id.content, "审核未通过");
                }else{
                    baseViewHolder.setText(R.id.content, "审核中");
                }


                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);
                    recyclerViewImages.setLayoutManager(linearLayoutManager);
                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.image_layout, false);
                    baseViewHolder.setVisible(R.id.content, true);
                }
            }
        };

        imageAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s, R.drawable.head_icon_boby);
            }
        };

        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        presenter.myAcceptOddWork(getIntent().getIntExtra(INTENT_KEY, 0));

        pass.setOnClickListener(v -> {

            AppealActivity.startActivity(
                    OrderProjRecieveProgressActivity.this, req, 0);
        });

        giveMoney.setOnClickListener(v -> {
            MyAcceptOddSubmitReq req = new MyAcceptOddSubmitReq();
            req.setOdd_id(getIntent().getIntExtra(INTENT_KEY, 0));
            req.setRemark("工作拍照");
            req.setSign_time(System.currentTimeMillis());
            req.setLocation(Constants.address);
            req.setImages(pathList);
            presenter.myAcceptOddSubmit(req);
        });

        signAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
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

        signAdapter.setOnItemClickListener((view, i) -> {
            view.findViewById(R.id.delete).setOnClickListener(v -> {
                imageItems.remove(i);
                signAdapter.remove(i);
                if (!signAdapter.getAllData().contains(ADD)) {
                    signAdapter.add(ADD);
                }
            });

            if (TextUtils.equals(signAdapter.getAllData().get(i), ADD)) {
                selectPhoto();
            }
        });

        recyclerViewImage.setAdapter(signAdapter);
        post.setVisibility(View.GONE);
        applyMoney.setVisibility(View.GONE);

        //申诉
        post.setOnClickListener(v -> {
            AppealActivity.startActivity(OrderProjRecieveProgressActivity.this, req, 0);
        });

        applyMoney.setOnClickListener(v -> {
            recyclerViewLayout.setVisibility(View.GONE);
            signLayout.setVisibility(View.VISIBLE);
            signAdapter.add(ADD);
            if (!TextUtils.isEmpty(Constants.address)){
                address.setText(Constants.address + " 工作拍照");
                time.setText(Utils.getNowDateMonth());
            }
        });

        initImagePicker();

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
    public void getData(MyPublishOddWorkRes res) {
        if (res.getWork() == null || res.getWork().size() == 0) {
            recyclerViewLayout.setVisibility(View.GONE);
            signLayout.setVisibility(View.VISIBLE);
            signAdapter.add(ADD);
            if (!TextUtils.isEmpty(Constants.address)){
                address.setText(Constants.address + " 工作拍照");
                time.setText(Utils.getNowDateMonth());
            }
        } else {
            recyclerViewLayout.setVisibility(View.VISIBLE);
            adapter.addAll(res.getWork());
        }

    }

    @Override
    public void passData(String s) {
        ToastUtil.show("审核不通过");
        finish();
    }

    @Override
    public void giveMoney(String s) {
        ToastUtil.show("审核通过");
        finish();
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
        if (images.size() > 0){
            post.setVisibility(View.VISIBLE);
            applyMoney.setVisibility(View.VISIBLE);
        }
        pathList.clear();
        Observable.from(images)
                .map(imageItem -> {
                    pathList.add(Utils.Bitmap2StrByBase64(imageItem.path));
                    return imageItem.path;})
                .toList()
                .subscribe(strings -> {
                    signAdapter.clear();
                    signAdapter.addAll(strings);
                    if (strings.size() != 10) {
                        signAdapter.add(ADD);
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
