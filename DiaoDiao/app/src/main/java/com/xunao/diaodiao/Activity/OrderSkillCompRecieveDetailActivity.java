package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.SkillProjDetailRes;
import com.xunao.diaodiao.Bean.SkillProjRecieveDetailRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.OrderSkillCompDetailPresenter;
import com.xunao.diaodiao.Present.OrderSkillCompRecieveDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.OrderSkillCompDetailView;
import com.xunao.diaodiao.View.OrderSkillCompRecieveDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class OrderSkillCompRecieveDetailActivity extends BaseActivity implements OrderSkillCompRecieveDetailView {

    @Inject
    OrderSkillCompRecieveDetailPresenter presenter;
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
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.days)
    TextView days;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.proj_detail)
    TextView projDetail;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.contact_btn)
    Button contactBtn;

    private RecyclerArrayAdapter<String> adapter;
    private int who;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, OrderSkillCompRecieveDetailActivity.class);
        intent.putExtra(INTENT_KEY, id);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_skill_comp_recieve_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        who = getIntent().getIntExtra("WHO", 0);

        if (who == Constants.SKILL_RECIEVE_LINGGONG){
            showToolbarBack(toolBar, title, "零工详情");
        }else if (who == Constants.SKILL_RECIEVE_PROJECT){
            showToolbarBack(toolBar, title, "项目详情");
        }


        presenter.mySkillProjRecieveDetail(this, getIntent().getIntExtra(INTENT_KEY, 0));

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        contactBtn.setOnClickListener(v -> {

        });

    }

    @Override
    public void getData(SkillProjRecieveDetailRes s) {
        if (s != null && s.getOdd() != null) {
            SkillProjRecieveDetailRes.OddBean oddBean = s.getOdd().get(0);

            title.setText(oddBean.getTitle());
            address.setText(oddBean.getRegion());
            addressDetail.setText(oddBean.getAddress());
            type.setText(oddBean.getType());
            name.setText(oddBean.getContact());
            phone.setText(oddBean.getContact_mobile());
            money.setText(oddBean.getDaily_wage() + "元");
            days.setText(oddBean.getTotal_day() + "天");
            time.setText(Utils.strToDateLong(oddBean.getClose_time()));
            projDetail.setText(oddBean.getDescribe());

            adapter.clear();
            adapter.addAll(oddBean.getImages());
        }
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
