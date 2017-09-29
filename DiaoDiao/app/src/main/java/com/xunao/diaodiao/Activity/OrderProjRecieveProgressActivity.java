package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.defaultInterface.DefaultRecyclerViewItem;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyAcceptOddSubmitReq;
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
import static com.xunao.diaodiao.Common.Constants.NO_PASS;

/**
 * 技术员  我接的  零工
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
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;
    @BindView(R.id.recycler_view_layout)
    RelativeLayout recyclerViewLayout;

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

    private GetMoneyReq req = new GetMoneyReq();
    private TextView post, applyMoney;
    private MyPublishOddWorkRes.WorkBean workBeanNoPass;

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

        adapter = new RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean>(this, R.layout.project_recieve_progress_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyPublishOddWorkRes.WorkBean workBean) {
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " "
                        + workBean.getRemark());
                baseViewHolder.setText(R.id.address, workBean.getLocation());
                String content = "";
                if (workBean.getPass() == 1) {
                    //审核通过
                    if (workBean.getPaid() == 1) {
                        //已打款
                        baseViewHolder.setText(R.id.content, "已打款");
                    } else if (workBean.getPaid() == 2) {
                        //未打款
                        baseViewHolder.setText(R.id.content, "未打款");
                    } else {
                        baseViewHolder.setText(R.id.content, "部分打款");
                    }
                    bottomBtnLayout.setVisibility(View.GONE);
                } else if (workBean.getPass() == 2) {
                    baseViewHolder.setText(R.id.content, "审核未通过");
                    bottomBtnLayout.setVisibility(View.VISIBLE);
                    adapter.removeAllFooter();
                    workBeanNoPass = workBean;
                } else {
                    baseViewHolder.setText(R.id.content, "审核中");
                    bottomBtnLayout.setVisibility(View.GONE);
                }


                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);
                    imageAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, String s) {
                            baseViewHolder.setImageUrl(R.id.image, s, R.drawable.head_icon_boby);
                        }
                    };

                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.image_layout, false);
                    baseViewHolder.setVisible(R.id.content, true);
                }
            }
        };

        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.addFooter(new DefaultRecyclerViewItem() {
            @Override
            public View onCreateView(ViewGroup viewGroup) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_recieve_ling_footer, null);
                RecyclerView recyclerViewImage = (RecyclerView) view.findViewById(R.id.recycler_view_image);

                signAdapter = new RecyclerArrayAdapter<String>(viewGroup.getContext(), R.layout.single_image_delete) {
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

                signAdapter.setOnItemClickListener((view1, i) -> {
                    view1.findViewById(R.id.delete).setOnClickListener(v -> {
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
                signAdapter.clear();
                signAdapter.add(ADD);
                recyclerViewImage.setAdapter(signAdapter);

                post = (TextView) view.findViewById(R.id.post);
                applyMoney = (TextView) view.findViewById(R.id.apply_money);
                post.setOnClickListener(v -> {
                    //提交进度
                    postProgress(2);
                });

                applyMoney.setOnClickListener(v -> {
                    //申请打款
                    postProgress(1);
                });

                return view;
            }
        });



        presenter.myAcceptOddWork(getIntent().getIntExtra(INTENT_KEY, 0));

        pass.setOnClickListener(v -> {
            //申诉
            req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
            //零工
            req.setProject_type(3);
            req.setImages(pathList);
            AppealActivity.startActivity(
                    OrderProjRecieveProgressActivity.this, req, NO_PASS);
        });

        giveMoney.setOnClickListener(v -> {
            //再次提交
            postProgress(2);
        });
        initImagePicker();

    }

    private void postProgress(int apply_type){
        MyAcceptOddSubmitReq req = new MyAcceptOddSubmitReq();
        req.setOdd_id(getIntent().getIntExtra(INTENT_KEY, 0));
        req.setRemark("工作拍照");
        req.setApply_type(apply_type);
        req.setSign_time(System.currentTimeMillis());
        req.setLocation(Constants.address);
        req.setImages(pathList);
        presenter.myAcceptOddSubmit(req);
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
            bottomBtnLayout.setVisibility(View.VISIBLE);

        } else {
            adapter.addAll(res.getWork());
        }

    }

    @Override
    public void passData(String s) {
        ToastUtil.show("提交成功");
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
        if (images.size() > 0) {
            post.setVisibility(View.VISIBLE);
            applyMoney.setVisibility(View.VISIBLE);
        }
        pathList.clear();
        Observable.from(images)
                .map(imageItem -> {
                    pathList.add(Utils.Bitmap2StrByBase64(imageItem.path));
                    return imageItem.path;
                })
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
