package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.ApplyPassReq;
import com.xunao.diaodiao.Present.ApplyDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.ApplyDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

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
    GRecyclerView recyclerView;
    @BindView(R.id.contact_hi)
    TextView contactHi;
    @BindView(R.id.agree)
    TextView agree;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, ApplyDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
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

        presenter.getApplyDetail(getIntent().getIntExtra(INTENT_KEY, 0));
        agree.setOnClickListener(v -> {
            ApplyPassReq req = new ApplyPassReq();
            presenter.getApplyPass(req);
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

    @Override
    public void getData(ApplyDetailRes res) {

    }

    @Override
    public void getPass(String s) {

    }
}
