package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ApplyDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ApplyDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_JIANLI_DONE;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DOING;
import static com.xunao.diaodiao.Common.Constants.COMPANY_RELEASE_WEIBAO_DONE;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.SKILL_RELEASE_WEIBAO;

/**
 * create by
 */
public class ApplyDetailActivity extends BaseActivity implements ApplyDetailView {

    @Inject
    ApplyDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.work_year)
    TextView workYear;
    @BindView(R.id.skill_content)
    TextView skillContent;
    @BindView(R.id.project_exper)
    TextView projectExper;
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.contact_hi)
    TextView contactHi;
    @BindView(R.id.agree)
    TextView agree;

    private ApplyPassReq req;
    private ApplyDetailRes res;

    private RecyclerArrayAdapter<ApplyDetailRes.EvaluateBean> adapter;

    public static void startActivity(Context context, ApplyPassReq req) {
        Intent intent = new Intent(context, ApplyDetailActivity.class);
        intent.putExtra(INTENT_KEY, req);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "人员详情");

        req = (ApplyPassReq) getIntent().getSerializableExtra(INTENT_KEY);
        presenter.getApplyDetail(this, req.getTechnician_id());
        agree.setOnClickListener(v -> {
            presenter.getApplyPass(req);
        });

        int who = req.getProject_id();
        if(who == 1000 || who == COMPANY_RELEASE_JIANLI_DONE ||
                who == COMPANY_RELEASE_JIANLI_DOING || who == COMPANY_RELEASE_WEIBAO_DOING
                    || who == COMPANY_RELEASE_WEIBAO_DONE || who == SKILL_RELEASE_WEIBAO){
            agree.setVisibility(View.GONE);
        }


        adapter = new RecyclerArrayAdapter<ApplyDetailRes.EvaluateBean>(this, R.layout.apply_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ApplyDetailRes.EvaluateBean s) {
                baseViewHolder.setText(R.id.name, s.getName());
                baseViewHolder.setImageUrl(R.id.head_icon, s.getHead_img(), R.drawable.head_icon_boby);
                baseViewHolder.setText(R.id.content, s.getContent());
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //recyclerView.setNestedScrollingEnabled(false);

        contactHi.setOnClickListener(v -> {
            new IOSDialog(ApplyDetailActivity.this).builder()
                    .setMsg(res.getMobile())
                    .setNegativeButton("呼叫", v1 -> {
                        Utils.startCallActivity(this, res.getMobile());
                    })
                    .setPositiveButton("取消", null)
                    .show();

        });
    }

    @Override
    public void getData(ApplyDetailRes res) {
        this.res = res;
        name.setText(res.getName());
        ratingStar.setRating(Float.valueOf(res.getPoint()));
        ratingStar.setIsIndicator(true);
        time.setText(res.getPoint());
        workYear.setText(res.getExperience()+"年");
        skillContent.setText(res.getProject_type());
        projectExper.setText(res.getProject_amount()+"年");
        projDetail.setText(res.getEvaluate());
        adapter.addAll(res.getEvaluates());
    }

    @Override
    public void getPass(Object s) {
        ToastUtil.show("申请成功");
        finish();
    }


    @Override
    public void onFailure() {
        adapter.stopMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


}
