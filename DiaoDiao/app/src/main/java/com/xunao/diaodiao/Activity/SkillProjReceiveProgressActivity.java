package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.MyAcceptProjectWorkRes;
import com.xunao.diaodiao.Present.SkillProjReceiveProgressPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.SkillProjReceiveProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.address;

/**
 * create by
 */
public class SkillProjReceiveProgressActivity extends BaseActivity implements SkillProjReceiveProgressView {

    @Inject
    SkillProjReceiveProgressPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sign)
    Button sign;

    private RecyclerArrayAdapter<MyAcceptProjectWorkRes.WorkBean> adapter;
    private String phone;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, SkillProjReceiveProgressActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_proj_progress);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "项目进度");

        adapter = new RecyclerArrayAdapter<MyAcceptProjectWorkRes.WorkBean>(this, R.layout.skill_proj_receive_proj_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyAcceptProjectWorkRes.WorkBean workBean) {
                baseViewHolder.setText(R.id.title, workBean.getTitle());
                if (workBean.getStage1() == 0){
                    baseViewHolder.setText(R.id.progress, "第一阶段未开始");
                }else if (workBean.getStage1() == 1){
                    baseViewHolder.setText(R.id.progress, "第一阶段进行中");
                }else if( workBean.getStage1() == 2){
                    baseViewHolder.setText(R.id.progress, "第一阶段已完成");
                }else{
                    baseViewHolder.setText(R.id.progress, "第一阶段审核中");
                }

                if (workBean.getStage2() == 0){
                    baseViewHolder.setText(R.id.status, "未开始");
                }else if (workBean.getStage2() == 1){
                    baseViewHolder.setText(R.id.status, "进行中");
                }else if( workBean.getStage2() == 2){
                    baseViewHolder.setText(R.id.status, "已完成");
                }else{
                    baseViewHolder.setText(R.id.status, "审核中");
                }

                baseViewHolder.setOnClickListener(R.id.middle_btn, v -> {
                    SkillProjProgressActivity.startActivity(SkillProjReceiveProgressActivity.this,
                            getIntent().getIntExtra(INTENT_KEY, 0), workBean.getWorks_id());
                });

            }
        };

        presenter.myAcceptProjectWork(this, getIntent().getIntExtra(INTENT_KEY, 0));

        sign.setOnClickListener(v -> {
            SignDetailActivity.startActivity(SkillProjReceiveProgressActivity.this,
                    getIntent().getIntExtra(INTENT_KEY, 0));

//            SkillProjProgressActivity.startActivity(SkillProjReceiveProgressActivity.this,
//                    getIntent().getIntExtra(INTENT_KEY, 0), 0);
        });
    }

    @Override
    public void getData(MyAcceptProjectWorkRes res) {
        if (res.getWorks() != null && res.getWorks().size() > 0){
            MyAcceptProjectWorkRes.WorksBean worksBean = res.getWorks().get(0);
            title.setText(worksBean.getTitle());
            address.setText(worksBean.getAddress());
            addressDetail.setText(worksBean.getRegion());
            adapter.addAll(worksBean.getWork());
            phone = worksBean.getContact_mobile();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                if (!TextUtils.isEmpty(phone)){
                    Utils.startCallActivity(this, phone);
                }
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
