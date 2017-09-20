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
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Present.OrderProjRecieveProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderProjRecieveProgressView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

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
    @BindView(R.id.recycler_view_item)
    RecyclerView recyclerViewItem;
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
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

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
                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    baseViewHolder.setVisible(R.id.recycler_view_item, true);
                    baseViewHolder.setVisible(R.id.content, false);
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);
                    recyclerViewImages.setLayoutManager(manager);
                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.recycler_view_item, false);
                    baseViewHolder.setVisible(R.id.content, true);
                }
            }
        };

        imageAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
            }
        };

        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        presenter.myAcceptOddWork(getIntent().getIntExtra(INTENT_KEY, 0));

        pass.setOnClickListener(v -> {
            MyPublicOddFailReq req = new MyPublicOddFailReq();
            req.setWork_id(0);
            req.setReason("reason");
            req.setImages(imageAdapter.getAllData());
            presenter.myPublishOddFail(req);
        });

        giveMoney.setOnClickListener(v -> {
            presenter.myPublishOddSuccess(0);
        });

        signAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                if (TextUtils.equals(ADD, s)){
                    baseViewHolder.setVisible(R.id.delete, false);
                    baseViewHolder.setImageUrl(R.id.image, s);
                }else{
                    baseViewHolder.setVisible(R.id.delete, true);
                    baseViewHolder.setImageUrl(R.id.select_img, s);
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

        recyclerViewItem.setAdapter(adapter);
        post.setVisibility(View.GONE);
        applyMoney.setVisibility(View.GONE);

        post.setOnClickListener(v -> {

        });

        applyMoney.setOnClickListener(v -> {

        });




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
        } else {
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
        Observable.from(images)
                .map(imageItem -> imageItem.path)
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
        recyclerViewLayout.setVisibility(View.GONE);
        signLayout.setVisibility(View.VISIBLE);
        signAdapter.add(ADD);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
