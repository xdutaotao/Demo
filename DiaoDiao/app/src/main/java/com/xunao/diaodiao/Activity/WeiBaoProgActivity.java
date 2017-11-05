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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.defaultInterface.DefaultRecyclerViewItem;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Bean.SkillProjProgPhotoRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.WeiBaoProgPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.WeiBaoProgView;
import com.xunao.diaodiao.Widget.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_NO_PASS;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class WeiBaoProgActivity extends BaseActivity implements WeiBaoProgView {

    @Inject
    WeiBaoProgPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.no_pass)
    TextView noPass;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.bottom_btn_layout)
    LinearLayout bottomBtnLayout;

    private int maintenanceId;

    private RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;
    private RecyclerArrayAdapter<String> footerAdapter;

    private String ADD = "ADD";
    List<String> pathList = new ArrayList<>();
    /**
     * 图片返回的items
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static final int IMAGE_PICKER = 8888;

    private MyPublishOddWorkRes.WorkBean workBeanDoing;
    private int who;

    public static void startActivity(Context context, int id, int who) {
        Intent intent = new Intent(context, WeiBaoProgActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("who", who);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_bao_prog);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "维保进度");

        maintenanceId = getIntent().getIntExtra(INTENT_KEY, 0);
        who = getIntent().getIntExtra("who", 0);

        adapter = new RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean>(this, R.layout.weibao_progress_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyPublishOddWorkRes.WorkBean workBean) {

                baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " 维保人员申请完成");
                baseViewHolder.setText(R.id.address, workBean.getLocation());

                if (workBean.getPass() == 3) {
                    //审核中

                } else if (workBean.getPass() == 2) {
                    //审核未通过
                    baseViewHolder.setText(R.id.content, "审核未通过");
                    baseViewHolder.setTextColorRes(R.id.content, R.color.accept_btn_default);
                    baseViewHolder.setText(R.id.content_time, Utils.strToDateLong(workBean.getSign_time()) + " 审核");
                    baseViewHolder.setTextColorRes(R.id.content_time, R.color.nav_gray);
                } else {
                    //审核通过
                    if (workBean.getApply() == 1) {
                        baseViewHolder.setText(R.id.content, "已确认打款");
                        baseViewHolder.setTextColorRes(R.id.content, R.color.accept_btn_default);
                        baseViewHolder.setText(R.id.content_time, Utils.strToDateLong(workBean.getSign_time()) + " 审核");
                        baseViewHolder.setTextColorRes(R.id.content_time, R.color.nav_gray);
                    } else {
                        baseViewHolder.setText(R.id.content, "审核通过");
                        baseViewHolder.setTextColorRes(R.id.content, R.color.accept_btn_default);
                        baseViewHolder.setText(R.id.content_time, Utils.strToDateLong(workBean.getSign_time()) + " 审核");
                        baseViewHolder.setTextColorRes(R.id.content_time, R.color.nav_gray);
                    }

                }

                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);

                    imageAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, String s) {
                            baseViewHolder.setImageUrl(R.id.image, s, R.drawable.zhanwei);
                        }
                    };

                    imageAdapter.setOnItemClickListener((view, i) -> {
                        PhotoActivity.startActivity(WeiBaoProgActivity.this, workBean.getImages().get(i),
                                workBean.getImages().get(i).contains("http"));
                    });

                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.recycler_view_item, false);
                }

            }
        };


        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        presenter.myPublishMaintenanceWork(this, maintenanceId, who);

        noPass.setOnClickListener(v -> {
            GetMoneyReq req = new GetMoneyReq();
            req.setWork_id(workBeanDoing.getWork_id());
            if(who == COMPANY_RELEASE_JIANLI_DOING){
                AppealActivity.startActivity(this, req, COMPANY_RELEASE_JIANLI);
            }else{
                AppealActivity.startActivity(this, req, COMPANY_RELEASE_WEIBAO);
            }

        });

        pass.setOnClickListener(v -> {

            new IOSDialog(this).builder()
                    .setTitle("点击确认打款给对方")
                    .setMsg("打款给对方了")
                    .setPositiveButton("确认", v1 -> {
                        presenter.myPublishMaintenanceSuccess(this, workBeanDoing.getWork_id(), who);
                    })
                    .setNegativeButton("取消", v1 -> {

                    })
                    .show();



        });

    }

    @Override
    public void getList(MyPublishOddWorkRes list) {
        if (list.getWork() != null && list.getWork().size() > 0) {
            adapter.addAll(list.getWork());

            workBeanDoing = list.getWork().get(list.getWork().size() - 1);
            if (workBeanDoing.getPass() == 1) {
                //已确认打款
                bottomBtnLayout.setVisibility(View.GONE);
            }else if(workBeanDoing.getPass() == 2){
                bottomBtnLayout.setVisibility(View.GONE);
            }else if(workBeanDoing.getPass() == 3){
                //审核中
//                adapter.addFooter(new DefaultRecyclerViewItem() {
//                    @Override
//                    public View onCreateView(ViewGroup viewGroup) {
//                        View view = LayoutInflater.from(WeiBaoProgActivity.this)
//                                .inflate(R.layout.company_weibao_footer, null);
//                        return view;
//                    }
//                });
            }
        }else {
            recyclerView.showEmpty();
            bottomBtnLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void passData(Object res) {
        ToastUtil.show("打款成功");
        finish();
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
