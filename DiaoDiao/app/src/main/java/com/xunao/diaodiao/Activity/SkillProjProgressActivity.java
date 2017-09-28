package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.defaultInterface.DefaultRecyclerViewItem;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.SkillProjProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.SkillProjProgressView;
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
import static com.xunao.diaodiao.Common.Constants.NO_PASS;
import static com.xunao.diaodiao.Common.Constants.address;

/**
 * 审核 项目进度
 * create by
 */
public class SkillProjProgressActivity extends BaseActivity implements SkillProjProgressView {

    @Inject
    SkillProjProgressPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.no_pass)
    TextView noPass;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;

    private RecyclerArrayAdapter<SkillProjProgPhotoRes.InfoBean> adapter;
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
    //工作ID
    private int worksid;
    private int workid;
    private EditText postRemark;
    private int stage = 0;
    private int who;
    private boolean canPost = true;
    private SkillProjProgPhotoRes.InfoBean noPassInfoBean;

    public static void startActivity(Context context, int id, int worksid, int stage, int who) {
        Intent intent = new Intent(context, SkillProjProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("WORKSID", worksid);
        intent.putExtra("STAGE", stage);
        intent.putExtra("WHO", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_proj_prog_photo);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "定位及隐藏工程施工");

        who = getIntent().getIntExtra("WHO", 0);
        stage = getIntent().getIntExtra("STAGE", 0);
        worksid = getIntent().getIntExtra("WORKSID", 0);
        adapter = new RecyclerArrayAdapter<SkillProjProgPhotoRes.InfoBean>(this, R.layout.sign_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, SkillProjProgPhotoRes.InfoBean s) {
                RecyclerView recyclerView = (RecyclerView) baseViewHolder.getConvertView().findViewById(R.id.recycler_view_item);
                itemAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, String s) {
                        baseViewHolder.setImageUrl(R.id.image, s, R.drawable.head_icon_boby);
                    }
                };

                recyclerView.setAdapter(itemAdapter);
                itemAdapter.clear();
                itemAdapter.addAll(s.getImages());
                baseViewHolder.setText(R.id.time, Utils.getNowDateMonth(s.getDate())
                        + " 工作拍照");
                baseViewHolder.setText(R.id.address, s.getLocation());
                if (s.getAudit_status() == 3 && (s.getAudit() == 1 || s.getAudit() == 2)){
                    workid = s.getWork_id();
                }
                if (!TextUtils.isEmpty(s.getRemark())){
                    baseViewHolder.setVisible(R.id.remark, true);
                    baseViewHolder.setText(R.id.remark, s.getRemark());
                }

                if (s.getAudit_status() == 3){
                    //审核中
                    canPost = false;
                }else if (s.getAudit_status() == 2){
                    //审核不通过
                    bottomBtnLayout.setVisibility(View.VISIBLE);
                    post.setVisibility(View.GONE);
                    noPass.setText("电话申诉");
                    pass.setText("再次提交");
                    adapter.removeAllFooter();
                    noPassInfoBean = s;
                }
            }
        };

        if (who == COMPANY_RELEASE_PROJECT_DOING || who == COMPANY_RELEASE_PROJECT_DONE) {
            bottomBtnLayout.setVisibility(View.VISIBLE);

            adapter.addFooter(new DefaultRecyclerViewItem() {
                @Override
                public View onCreateView(ViewGroup viewGroup) {
                    View view = LayoutInflater.from(SkillProjProgressActivity.this)
                            .inflate(R.layout.company_project_footer, null);
                    return view;
                }
            });

            noPass.setOnClickListener(v -> {
                GetMoneyReq req = new GetMoneyReq();
                req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
                req.setWorks_id(worksid);
                req.setStage(stage);
                req.setWork_id(workid);
                AppealActivity.startActivity(SkillProjProgressActivity.this,
                        req, NO_PASS);
            });

            pass.setOnClickListener(v -> {
                GetMoneyReq req = new GetMoneyReq();
                presenter.myProjectWorkPass(SkillProjProgressActivity.this, req);
            });

        }else {
            AdapterAddFooter();

            //电话投诉
            noPass.setOnClickListener(v -> {
                Utils.startCallActivity(this, "12345678900");
            });

            //再次提交
            pass.setOnClickListener(v -> {
//                GetMoneyReq req = new GetMoneyReq();
//                req.setLocation(noPassInfoBean.getLocation());
//                req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
//                List<String> path = new ArrayList<>();
//                for(String url: noPassInfoBean.getImages()){
//                    path.add(Utils.Bitmap2StrByBase64(url));
//                }
//                req.setImages(path);
//                req.setWorks_id(worksid);
//                req.setSign_time(noPassInfoBean.getDate());
//                req.setRemark(noPassInfoBean.getRemark());
//                req.setAudit(noPassInfoBean.getAudit());
//                presenter.myAcceptProjectWorkSub(this, req);

                bottomBtnLayout.setVisibility(View.GONE);
                canPost = true;
                AdapterAddFooter();
            });

        }

        footerAdapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
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

        footerAdapter.setOnItemClickListener((view, i) -> {
            view.findViewById(R.id.delete).setOnClickListener(v -> {
                imageItems.remove(i);
                footerAdapter.remove(i);
                if (!footerAdapter.getAllData().contains(ADD)) {
                    footerAdapter.add(ADD);
                }
            });

            if (TextUtils.equals(footerAdapter.getAllData().get(i), ADD)) {
                selectPhoto();
            }
        });
        footerAdapter.add(ADD);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        initImagePicker();

        presenter.myAcceptProjectWorkList(this,
                getIntent().getIntExtra(INTENT_KEY, 0), worksid, who);

        post.setOnClickListener(v -> {
            //第一阶段 第二阶段
            signAction(stage);
        });

        if (stage == 2) {
            post.setText("第二阶段提交审核");
        } else {
            post.setText("第一阶段提交审核");
        }
    }

    private void AdapterAddFooter(){
        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup viewGroup) {
                View view = LayoutInflater.from(SkillProjProgressActivity.this).inflate(R.layout.skill_proj_prog_footer, null);
                RecyclerView recyclerViewFooter = (RecyclerView) view.findViewById(R.id.recycler_view_image);
                recyclerViewFooter.setAdapter(footerAdapter);
                return view;
            }

            @Override
            public void onBindView(View view) {
                postText = (TextView) view.findViewById(R.id.post);
                postRemark = (EditText) view.findViewById(R.id.remark);
                TextView time = (TextView) view.findViewById(R.id.time);
                time.setText(Utils.getNowDateMonth());
                TextView address = (TextView) view.findViewById(R.id.address);
                address.setText(Constants.address);
                View headLine = view.findViewById(R.id.head_line);
                if (adapter.getAllData().size() == 0) {
                    headLine.setBackgroundColor(getResources().getColor(R.color.activity_background));
                } else {
                    headLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                postText.setOnClickListener(v -> {
                    signAction(0);
                });
            }
        });
    }

    @Override
    public void getData(String s) {
        ToastUtil.show("提交审核成功");
        finish();
    }

    @Override
    public void getList(SkillProjProgPhotoRes list) {
        if (list.getInfo() != null && list.getInfo().size() > 0) {
            adapter.addAll(list.getInfo());
        }else{
            pass.setVisibility(View.GONE);
            noPass.setVisibility(View.GONE);
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(10);
        imagePicker.setOutPutX(100);
        imagePicker.setOutPutY(100);
    }

    private void signAction(int audit) {
        if (!canPost){
            ToastUtil.show("上次提交，正在审核中...");
            return;
        }
        if (pathList.size() ==0){
            ToastUtil.show("请选择照片");
            return;
        }
        GetMoneyReq req = new GetMoneyReq();
        req.setLocation(address);
        req.setProject_id(getIntent().getIntExtra(INTENT_KEY, 0));
        req.setImages(pathList);
        req.setWorks_id(worksid);
        req.setSign_time(System.currentTimeMillis());
        req.setRemark(postRemark.getText().toString());
        req.setAudit(audit);
        presenter.myAcceptProjectWorkSub(this, req);
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
        if (images.size() > 0) {
            postText.setText("确认签到");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        MenuItem item = menu.findItem(R.id.action_contact);
        if (who == COMPANY_RELEASE_PROJECT_DOING || who == COMPANY_RELEASE_PROJECT_DONE){
            item.setTitle("申诉");
        }else{
            item.setTitle("");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                ToastUtil.show("申诉");
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
