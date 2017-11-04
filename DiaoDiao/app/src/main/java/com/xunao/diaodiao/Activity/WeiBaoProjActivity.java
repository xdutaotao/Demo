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
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.SignRes;
import com.xunao.diaodiao.Bean.WeiBaoProgRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.WeiBaoProjPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.WeiBaoProjView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_PROJECT_DONE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RECIEVE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.address;

/**
 * create by
 */
public class WeiBaoProjActivity extends BaseActivity implements WeiBaoProjView {

    @Inject
    WeiBaoProjPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<WeiBaoProgRes.WorkBean> adapter;
    private RecyclerArrayAdapter<String> itemAdapter;
    private RecyclerArrayAdapter<String> footerAdapter;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private TextView postText;
    private int who;

    public static void startActivity(Context context, int id, int who) {
        Intent intent = new Intent(context, WeiBaoProjActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("who", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_bao_proj);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "维保进度");
        who = getIntent().getIntExtra("who", 0);
        adapter = new RecyclerArrayAdapter<WeiBaoProgRes.WorkBean>(this, R.layout.sign_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, WeiBaoProgRes.WorkBean s) {
                RecyclerView recyclerView = (RecyclerView) baseViewHolder.getConvertView().findViewById(R.id.recycler_view_item);

                itemAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, String s) {
                        baseViewHolder.setImageUrl(R.id.image, s, R.drawable.head_icon_boby);
                    }
                };

                itemAdapter.setOnItemClickListener((view, i) -> {
                    if(s.getImages() != null && (s.getImages().size()>0))
                        PhotoActivity.startActivity(WeiBaoProjActivity.this, s.getImages().get(i),
                                s.getImages().get(i).contains("http"));
                });

                recyclerView.setAdapter(itemAdapter);
                itemAdapter.clear();
                itemAdapter.addAll(s.getImages());

                baseViewHolder.setText(R.id.time, Utils.strToDateLong(s.getSign_time())
                        + " 工作拍照");
                baseViewHolder.setText(R.id.address, s.getLocation());

                if (s.getPass() == 3){
                    //审核中
                    baseViewHolder.setText(R.id.time, Utils.getNowDateMonth(s.getSign_time())
                            + " 审核中");
                }else if (s.getPass() == 2){
                    //未通过审核
                    baseViewHolder.setText(R.id.time, Utils.getNowDateMonth(s.getSign_time())
                            + " 未通过审核");
                }else{
                    baseViewHolder.setText(R.id.time, Utils.getNowDateMonth(s.getSign_time())
                            + " 审核通过");
                }

            }
        };

            setFooter();

            footerAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
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

            footerAdapter.setOnItemClickListener((view, i) -> {
                view.findViewById(R.id.delete).setOnClickListener(v -> {
                    imageItems.remove(i);
                    footerAdapter.remove(i);
                    if (!footerAdapter.getAllData().contains(ADD)) {
                        footerAdapter.add(ADD);
                    }
                });

                if (TextUtils.equals(footerAdapter.getAllData().get(i), ADD)) {
                    //selectPhoto();
                    takePhoto();
                }
            });
            footerAdapter.add(ADD);





        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        initImagePicker();

        presenter.myAcceptMaintenanceWork(this, getIntent().getIntExtra(INTENT_KEY, 0), 0);


    }

    @Override
    public void getData(Object s) {
        ToastUtil.show("提交成功");
        finish();
    }

    @Override
    public void getList(WeiBaoProgRes list) {
        if (list.getWork() != null && list.getWork().size() > 0){
            adapter.addAll(list.getWork());
        }else{
            //recyclerView.showEmpty();
        }
    }

    private void setFooter(){
        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup viewGroup) {
                View view = LayoutInflater.from(WeiBaoProjActivity.this).inflate(R.layout.sign_footer, null);
                RecyclerView recyclerViewFooter = (RecyclerView) view.findViewById(R.id.recycler_view_image);
                recyclerViewFooter.setAdapter(footerAdapter);
                return view;
            }

            @Override
            public void onBindView(View view) {
                postText = (TextView) view.findViewById(R.id.post);
                TextView addr = (TextView) view.findViewById(R.id.address);
                addr.setText(address);
                TextView time = (TextView) view.findViewById(R.id.time);
                time.setText(Utils.getNowDateMonth() + " 工作拍照");
                postText.setText("提交审核");
                postText.setOnClickListener(v -> {
                    signAction();
                });
            }
        });
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

    private void signAction(){
        GetMoneyReq req = new GetMoneyReq();
        req.setLocation(Constants.address);
        if(who == SKILL_RECIEVE_JIANLI){
            req.setSupervisor_id(getIntent().getIntExtra(INTENT_KEY, 0));
        }else{
            req.setMaintenance_id(getIntent().getIntExtra(INTENT_KEY, 0));
        }

        req.setRemark("remark");
        req.setSign_time(System.currentTimeMillis()/1000);
        //是否申请打款
        req.setApply_type(2);
        req.setImages(pathList);
        presenter.myAcceptMaintenanceSubmit(this, req, who);
    }

    private void takePhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, IMAGE_PICKER);
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
                //imageItems.clear();
                imageItems.addAll(images);
                setResultToAdapter(imageItems);
            } else {
                ToastUtil.show("没有数据");
            }
        }
    }

    private void setResultToAdapter(ArrayList<ImageItem> images) {
        if (images.size() > 0){
            postText.setText("提交审核");
            postText.setVisibility(View.VISIBLE);
        }

        pathList.clear();
        Observable.from(images)
                .map(imageItem -> {
                    pathList.add(Utils.Bitmap2StrByBase64(imageItem.path));
                    return imageItem.path;
                })
                .toList()
                .subscribe(strings -> {
                    footerAdapter.clear();
                    footerAdapter.addAll(strings);
                    if (strings.size() != 10) {
                        footerAdapter.add(ADD);
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
