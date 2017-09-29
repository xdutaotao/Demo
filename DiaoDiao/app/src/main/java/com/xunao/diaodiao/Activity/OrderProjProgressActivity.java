package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.MyPublicOddFailReq;
import com.xunao.diaodiao.Bean.MyPublishOddWorkRes;
import com.xunao.diaodiao.Present.OrderProjProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderProjProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_LINGGONG_NO_PASS;

/**
 * 我发布的零工信息
 * create by
 */
public class OrderProjProgressActivity extends BaseActivity implements OrderProjProgressView {

    @Inject
    OrderProjProgressPresenter presenter;
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

    //审核中的 work
    private MyPublishOddWorkRes.WorkBean workBeanDoing;

    private RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean> adapter;
    private RecyclerArrayAdapter<String> imageAdapter;
    private LinearLayoutManager manager;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, OrderProjProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_proj_progress);
        setContentView(R.layout.skill_project_progress);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目进度");

        adapter = new RecyclerArrayAdapter<MyPublishOddWorkRes.WorkBean>(this, R.layout.project_progress_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyPublishOddWorkRes.WorkBean workBean) {

                if (workBean.getPass() == 3){
                    //审核中
                    workBeanDoing = workBean;
                    //baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " 审核");
                }else if (workBean.getPass() == 2){
                    //审核未通过
                    //baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " 暖通公司未通过");
                    //baseViewHolder.setTextColorRes(R.id.time, R.color.accept_btn_default);
                }else {
                    //审核通过
                    //baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " 拍照签到");
                }

                baseViewHolder.setText(R.id.time, Utils.strToDateLong(workBean.getSign_time()) + " "+workBean.getLocation());
                baseViewHolder.setText(R.id.content_time, Utils.strToDateLong(workBean.getSign_time()) + " "+workBean.getRemark());

                if (workBean.getImages() != null && workBean.getImages().size() > 0) {
                    RecyclerView recyclerViewImages = baseViewHolder.getView(R.id.recycler_view_item);

                    imageAdapter = new RecyclerArrayAdapter<String>(baseViewHolder.getContext(), R.layout.single_image) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, String s) {
                            baseViewHolder.setImageUrl(R.id.image, s, R.drawable.zhanwei);
                        }
                    };

                    recyclerViewImages.setAdapter(imageAdapter);
                    imageAdapter.clear();
                    imageAdapter.addAll(workBean.getImages());
                } else {
                    baseViewHolder.setVisible(R.id.recycler_view_item, false);
                }

            }
        };



        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        presenter.myPublishOddWorkProgress(getIntent().getIntExtra(INTENT_KEY, 0));

        pass.setOnClickListener(v -> {
//            MyPublicOddFailReq req = new MyPublicOddFailReq();
//            req.setWork_id(workBeanDoing.getWork_id());
//            req.setReason("reason");
//            req.setImages(imageAdapter.getAllData());
            //presenter.myPublishOddFail(req);

            GetMoneyReq req = new GetMoneyReq();
            req.setWork_id(workBeanDoing.getWork_id());
            AppealActivity.startActivity(this, req, SKILL_RELEASE_LINGGONG_NO_PASS);
        });

        giveMoney.setOnClickListener(v -> {
            presenter.myPublishOddSuccess(workBeanDoing.getWork_id());
        });


    }

    @Override
    public void getData(MyPublishOddWorkRes res) {
        adapter.addAll(res.getWork());
    }

    @Override
    public void passData(String s) {
        ToastUtil.show("审核不通过");
        finish();
    }

    @Override
    public void giveMoney(Object s) {
        ToastUtil.show("审核通过");
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
