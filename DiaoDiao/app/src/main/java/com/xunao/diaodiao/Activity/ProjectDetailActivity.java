package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Present.ProjectDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ProjectDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ProjectDetailActivity extends BaseActivity implements ProjectDetailView {

    @Inject
    ProjectDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.proj_type)
    TextView projType;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.build_time)
    TextView buildTime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.post)
    Button post;
    @BindView(R.id.detail)
    TextView detail;

    public static void startActivity(Context context, int id, int type) {
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        intent.putExtra("TYPE", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目详情");

        presenter.getFindProjDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));
        post.setOnClickListener(v -> {

        });
    }

    @Override
    public void getData(FindProjDetailRes res) {
        title.setText(res.getProject_info().getTitle());
        companyName.setText(res.getProject_info().getCompany_name());
        time.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getCreate_time())));
        projType.setText(res.getProject_info().getType());
        address.setText(res.getProject_info().getDistrict());
        addressDetail.setText(res.getProject_info().getAddress());
        buildTime.setText(Utils.strToDateLong(Long.valueOf(res.getProject_info().getBuild_time())));
        projDetail.setText(res.getProject_info().getDescribe());
        price.setText("￥ " + res.getProject_info().getProject_fee());
        detail.setText(res.getProject_info().getDescribe());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proj_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:

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
